/**
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.zaproxy.zap.extension.sqlinjectionscan;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.core.scanner.AbstractAppPlugin;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.core.scanner.Category;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;


/**
 * TODO: implement the Risk level check / do not do dangerous operations unless the level is right!
 * TODO: implement checks in Header fields (currently does Cookie values, form fields, and url parameters)
 * TODO: change the Alert Titles.
 * TODO: maybe implement a more specific UNION based check for Hypersonic (with table names)
 * 
 * The SQLInjectionHypersonic plugin identifies Hypersonic specific SQL Injection vulnerabilities
 * using Hypersonic specific syntax.  If it doesn't use Hypersonic specific syntax, it belongs in the generic SQLInjection class! 
 * Note the ordering of checks, for efficiency is : 
 * 1) Error based (N/A)
 * 2) Boolean Based (N/A - uses standard syntax) 
 * 3) UNION based (TODO)
 * 4) Stacked (N/A - uses standard syntax)
 * 5) Blind/Time Based (Yes)
 * 
 * See the following for some great (non-Hypersonic specific) specific tricks which could be integrated here
 * http://www.websec.ca/kb/sql_injection
 * http://pentestmonkey.net/cheat-sheet/sql-injection/oracle-sql-injection-cheat-sheet
 * 
 * Important Notes for the Hypersonic database (and useful in the code):
 * - takes -- style comments
 * - requires a table name in normal select statements (like Oracle: cannot just say "select 1" or "select 2" like in most RDBMSs
 * - requires a table name in "union select" statements (like Oracle). 
 * - allows stacked queries via JDBC driver. 
 * - Constants in select must be in single quotes, not doubles (like Oracle).
 * - supports UDFs in the form of Java code (very interesting!!)
 * - 5 second delay select statement: select "java.lang.Thread.sleep"(5000) from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME'
 * - metadata select statement: select TABLE_NAME, COLUMN_NAME, TYPE_NAME, COLUMN_SIZE, DECIMAL_DIGITS, IS_NULLABLE from INFORMATION_SCHEMA.SYSTEM_COLUMNS		
 * 
 *  @author Colm O'Flaherty, Encription Ireland Ltd
 */
public class SQLInjectionHypersonic extends AbstractAppPlugin {
	
	////debug variables.. used to skip over certain logic to get to the rest quickly! 
	////(some SQL Injection vulns would be picked up by multiple types of checks, and we skip out after the first alert for a URL)
	//private boolean debugDoErrorBased = true;
	//private boolean debugDoBooleanBased=true;
	//private boolean debugDoUnionBased = true;
	//private boolean debugDoStackedBased=true;
	private boolean debugDoTimeBased=true;
	

	/**
	 * Hypersonic one-line comment
	 */
	public static final String SQL_ONE_LINE_COMMENT = " -- ";

	/**
	 * create a map of SQL related error message fragments, and map them back to the RDBMS that they are associated with
	 * keep the ordering the same as the order in which the values are inserted, to allow the more (subjectively judged) common cases to be tested first
	 * Note: these should represent actual (driver level) error messages for things like syntax error, 
	 * otherwise we are simply guessing that the string should/might occur.
	 */
	private static final Map<String, String> SQL_ERROR_TO_DBMS = new LinkedHashMap<String, String>();
	static {
		SQL_ERROR_TO_DBMS.put("org.hsql", "Hypersonic SQL");  
		SQL_ERROR_TO_DBMS.put("hSql.", "Hypersonic SQL");
		SQL_ERROR_TO_DBMS.put("Unexpected token , requires FROM in statement", "Hypersonic SQL");
		SQL_ERROR_TO_DBMS.put("Unexpected end of command in statement", "Hypersonic SQL");
		SQL_ERROR_TO_DBMS.put("Column count does not match in statement", "Hypersonic SQL"); 
		SQL_ERROR_TO_DBMS.put("Table not found in statement", "Hypersonic SQL"); 
		SQL_ERROR_TO_DBMS.put("Unexpected token:", "Hypersonic SQL");
		//Note: only Hypersonic mappings here.
	}


	/**
	 * the 5 second sleep function in Hypersonic SQL
	 */
	private static String SQL_HYPERSONIC_TIME_FUNCTION = "\"java.lang.Thread.sleep\"(5000)";

	
	/**
	 * Hypersonic specific time based injection strings. each for 5 seconds
	 */
	
	//issue with "+" symbols in here: 
	//we cannot encode them here as %2B, as then the database gets them double encoded as %252B
	//we cannot leave them as unencoded '+' characters either, as then they are NOT encoded by the HttpMessage.setGetParams (x) or by AbstractPlugin.sendAndReceive (HttpMessage)
	//and are seen by the database as spaces :(
	//in short, we cannot use the "+" character in parameters, unless we mean to use it as a space character!!!! Particularly Nasty.
	//Workaround: use RDBMS specific functions like "CONCAT(a,b,c)" which mean parsing the original value into the middle of the parameter value to be passed, 
	//rather than just appending to it
	//Issue: this technique does not close the open ' or " in the query.. so do not use it..
	//Note: <<<<ORIGINALVALUE>>>> is replaced with the original parameter value at runtime in these examples below (see * comment)
	//TODO: maybe add support for ')' after the original value, before the sleeps
	private static String[] SQL_HYPERSONIC_TIME_REPLACEMENTS = {
		"; select "+ SQL_HYPERSONIC_TIME_FUNCTION+ " from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME'"+ SQL_ONE_LINE_COMMENT,
		"'; select "+ SQL_HYPERSONIC_TIME_FUNCTION+ " from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME'"+ SQL_ONE_LINE_COMMENT,
		"\"; select "+ SQL_HYPERSONIC_TIME_FUNCTION+ " from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME'"+ SQL_ONE_LINE_COMMENT,
		"); select "+ SQL_HYPERSONIC_TIME_FUNCTION+ " from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME'"+ SQL_ONE_LINE_COMMENT,
		SQL_HYPERSONIC_TIME_FUNCTION,	
		"<<<<ORIGINALVALUE>>>> / "+SQL_HYPERSONIC_TIME_FUNCTION+" ",
		"<<<<ORIGINALVALUE>>>>' / "+SQL_HYPERSONIC_TIME_FUNCTION+" / '",
		"<<<<ORIGINALVALUE>>>>\" / "+SQL_HYPERSONIC_TIME_FUNCTION+" / \"",			
		"<<<<ORIGINALVALUE>>>> and exists ( select "+SQL_HYPERSONIC_TIME_FUNCTION+" from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME')"+ SQL_ONE_LINE_COMMENT, 	// Param in WHERE clause somewhere
		"<<<<ORIGINALVALUE>>>>' and exists ( select "+SQL_HYPERSONIC_TIME_FUNCTION+" from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME')"+ SQL_ONE_LINE_COMMENT, 	// Param in WHERE clause somewhere
		"<<<<ORIGINALVALUE>>>>\" and exists ( select "+SQL_HYPERSONIC_TIME_FUNCTION+" from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME')"+ SQL_ONE_LINE_COMMENT, 	// Param in WHERE clause somewhere
		"<<<<ORIGINALVALUE>>>>) and exists ( select "+SQL_HYPERSONIC_TIME_FUNCTION+" from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME')"+ SQL_ONE_LINE_COMMENT, 	// Param in WHERE clause somewhere
		"<<<<ORIGINALVALUE>>>> or exists ( select "+SQL_HYPERSONIC_TIME_FUNCTION+" from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME')"+ SQL_ONE_LINE_COMMENT, 	// Param in WHERE clause somewhere
		"<<<<ORIGINALVALUE>>>>' or exists ( select "+SQL_HYPERSONIC_TIME_FUNCTION+" from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME')"+ SQL_ONE_LINE_COMMENT, 	// Param in WHERE clause somewhere
		"<<<<ORIGINALVALUE>>>>\" or exists ( select "+SQL_HYPERSONIC_TIME_FUNCTION+" from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME')"+ SQL_ONE_LINE_COMMENT, 	// Param in WHERE clause somewhere
		"<<<<ORIGINALVALUE>>>>) or exists ( select "+SQL_HYPERSONIC_TIME_FUNCTION+" from INFORMATION_SCHEMA.SYSTEM_COLUMNS where TABLE_NAME = 'SYSTEM_COLUMNS' and COLUMN_NAME = 'TABLE_NAME')"+ SQL_ONE_LINE_COMMENT, 	// Param in WHERE clause somewhere
		};
	

	/**
	 * plugin dependencies (none! not even "SQL Injection")
	 */
	private static final String[] dependency = {};    	

	/**
	 * for logging.
	 */
	private static Logger log = Logger.getLogger(SQLInjectionHypersonic.class);

	/**
	 * determines if we should output Debug level logging
	 */
	private boolean debugEnabled = log.isDebugEnabled();


	/**
	 * contains the internationalisation (i18n) messages. Must be statically initialised, since messages is accessed before the plugin is initialised (using init)
	 */
	private ResourceBundle messages = ResourceBundle.getBundle(
			this.getClass().getPackage().getName() + ".Messages", Constant.getLocale());

	/**
	 * gets the internationalised message corresponding to the key
	 * @param key the key to look up the internationalised message
	 * @return the internationalised message corresponding to the key
	 */
	public String getString(String key) {
		try {
			return messages.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * gets the internationalised message corresponding to the key, using the parameters supplied
	 * @param key the key to look up the internationalised message
	 * @param params the parameters used to internationalise the message
	 * @return the internationalised message corresponding to the key, using the parameters supplied
	 */
	public String getString(String key, Object... params  ) {
		try {
			return MessageFormat.format(messages.getString(key), params);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/* 
	 * returns the plugin id
	 * @see org.parosproxy.paros.core.scanner.Test#getId()
	 */
	@Override
	public int getId() {
		return 40020;
	}

	/* returns the plugin name
	 * @see org.parosproxy.paros.core.scanner.Test#getName()
	 */
	@Override
	public String getName() {
		return getString("sqlinjection.hypersonic.name");
	}

	/* returns the plugin dependencies
	 * @see org.parosproxy.paros.core.scanner.Test#getDependency()
	 */
	@Override
	public String[] getDependency() {        
		return dependency;
	}

	/* returns the plugin description
	 * @see org.parosproxy.paros.core.scanner.Test#getDescription()
	 */
	@Override
	public String getDescription() {
		return getString("sqlinjection.desc");
	}

	/* returns the type of plugin
	 * @see org.parosproxy.paros.core.scanner.Test#getCategory()
	 */
	@Override
	public int getCategory() {
		return Category.INJECTION;
	}

	/* (non-Javadoc)
	 * @see org.parosproxy.paros.core.scanner.Test#getSolution()
	 */
	@Override
	public String getSolution() {
		return getString("sqlinjection.soln");
	}

	/* returns references for the plugin
	 * @see org.parosproxy.paros.core.scanner.Test#getReference()
	 */
	@Override
	public String getReference() {
		return getString("sqlinjection.refs");  
	}

	/* initialise
	 * @see org.parosproxy.paros.core.scanner.AbstractTest#init()
	 */
	@Override
	public void init() {
		//DEBUG: turn on for debugging
		//TODO: turn this off
		//log.setLevel(org.apache.log4j.Level.DEBUG);
		//this.debugEnabled = true;

		if ( this.debugEnabled ) log.debug("Initialising");
	}


	/**
	 * scans for SQL Injection vulnerabilities, using Hypersonic specific syntax.  If it doesn't use specifically Hypersonic syntax, it does not belong in here, but in SQLInjection 
	 */
	@Override
	public void scan() {

		//as soon as we find a single SQL injection on the url, skip out. Do not look for SQL injection on a subsequent parameter on the same URL
		//for performance reasons.
		boolean sqlInjectionFoundForUrl = false;
		
		//DEBUG only
		//log.setLevel(org.apache.log4j.Level.DEBUG);
		//this.debugEnabled = true;

		try {
			//Timing Baseline check: we need to get the time that it took the original query, to know if the time based check is working correctly..
			HttpMessage msgTimeBaseline = getNewMsg();
			long originalTimeStarted = System.currentTimeMillis();
			try {
				sendAndReceive(msgTimeBaseline); }
			catch (java.net.SocketTimeoutException e) {
				//to be expected occasionally, if the base query was one that contains some parameters exploiting time based SQL injection?
				if ( this.debugEnabled ) log.debug("The Base Time Check timed out on ["+msgTimeBaseline.getRequestHeader().getMethod()+"] URL ["+msgTimeBaseline.getRequestHeader().getURI().getURI()+"]");
			}
			long originalTimeUsed = System.currentTimeMillis() - originalTimeStarted;
			//end of timing baseline check
			
			
			TreeSet<HtmlParameter> htmlParams = new TreeSet<HtmlParameter> (); 
			htmlParams.addAll(getBaseMsg().getFormParams());  //add in the POST params
			htmlParams.addAll(getBaseMsg().getUrlParams()); //add in the GET params

			//for each parameter in turn
			for (Iterator<HtmlParameter> iter = htmlParams.iterator(); iter.hasNext() && ! sqlInjectionFoundForUrl; ) {

				HtmlParameter currentHtmlParameter = iter.next();
				if ( this.debugEnabled ) log.debug("Scanning URL ["+ getBaseMsg().getRequestHeader().getMethod()+ "] ["+ getBaseMsg().getRequestHeader().getURI() + "], ["+ currentHtmlParameter.getType()+"] field ["+ currentHtmlParameter.getName() + "] with value ["+currentHtmlParameter.getValue()+"] for SQL Injection");    			
				
				//Check 3: check for time based SQL Injection
				//Hypersonic specific time based SQL injection checks

				for (int timeBasedSQLindex = 0; timeBasedSQLindex < SQL_HYPERSONIC_TIME_REPLACEMENTS.length && ! sqlInjectionFoundForUrl && debugDoTimeBased; timeBasedSQLindex ++) {
					HttpMessage msg3 = getNewMsg();
					String newTimeBasedInjectionValue = SQL_HYPERSONIC_TIME_REPLACEMENTS[timeBasedSQLindex].replace ("<<<<ORIGINALVALUE>>>>", currentHtmlParameter.getValue());
					
					if ( currentHtmlParameter.getType().equals (HtmlParameter.Type.url)) {
						TreeSet <HtmlParameter> requestParams = msg3.getUrlParams(); //get parameters
						requestParams.remove(currentHtmlParameter);
						requestParams.add(new HtmlParameter(currentHtmlParameter.getType(), currentHtmlParameter.getName(), newTimeBasedInjectionValue)); 
						msg3.setGetParams(requestParams); //url parameters       		        			        			        		
					}  //end of the URL parameter code
					else if ( currentHtmlParameter.getType().equals (HtmlParameter.Type.form)) {
						TreeSet <HtmlParameter> requestParams = msg3.getFormParams(); //form parameters
						requestParams.remove(currentHtmlParameter);
						//new HtmlParameter ();
						requestParams.add(new HtmlParameter(currentHtmlParameter.getType(), currentHtmlParameter.getName(), newTimeBasedInjectionValue));
						msg3.setFormParams(requestParams); //form parameters       		        			        			        		
					}  //end of the URL parameter code
					else if ( currentHtmlParameter.getType().equals (HtmlParameter.Type.cookie)) {
						TreeSet <HtmlParameter> requestParams = msg3.getCookieParams(); //cookie parameters
						requestParams.remove(currentHtmlParameter);
						requestParams.add(new HtmlParameter(currentHtmlParameter.getType(), currentHtmlParameter.getName(), newTimeBasedInjectionValue));
						msg3.setCookieParams(requestParams); //cookie parameters
					}

					//send it.
					long modifiedTimeStarted = System.currentTimeMillis();
					try {
						sendAndReceive(msg3);
						}
					catch (java.net.SocketTimeoutException e) {
						//this is to be expected, if we start sending slow queries to the database.  ignore it in this case.. and just get the time.
						if ( this.debugEnabled ) log.debug("The time check query timed out on ["+msgTimeBaseline.getRequestHeader().getMethod()+"] URL ["+msgTimeBaseline.getRequestHeader().getURI().getURI()+"] on ["+currentHtmlParameter.getType()+"] field: ["+currentHtmlParameter.getName()+"]");
					}
					long modifiedTimeUsed = System.currentTimeMillis() - modifiedTimeStarted;

					if ( this.debugEnabled ) log.debug ("Time Based SQL Injection test: ["+ newTimeBasedInjectionValue + "] on ["+currentHtmlParameter.getType()+"] field: ["+currentHtmlParameter.getName()+"] with value ["+newTimeBasedInjectionValue+"] took "+ modifiedTimeUsed + "ms, where the original took "+ originalTimeUsed + "ms");

					if (modifiedTimeUsed >= (originalTimeUsed + 5000)) {
						//takes more than 5 extra seconds => likely time based SQL injection. Raise it 
						String extraInfo = getString("sqlinjection.alert.timebased.extrainfo", newTimeBasedInjectionValue, modifiedTimeUsed, currentHtmlParameter.getValue(), originalTimeUsed);
						String attack = getString("sqlinjection.alert.booleanbased.attack", currentHtmlParameter.getType(), currentHtmlParameter.getName(), newTimeBasedInjectionValue);

						//raise the alert
						bingo(Alert.RISK_HIGH, Alert.WARNING, getName() + " - Time Based", getDescription(), 
								"", //url
								"["+currentHtmlParameter.getType()+"] "+ currentHtmlParameter.getName(),  attack, 
								extraInfo, getSolution(), msg3);

						log.info("A likely Time Based SQL Injection Vulnerability has been found with ["+msg3.getRequestHeader().getMethod()+"] URL ["+msg3.getRequestHeader().getURI().getURI()+"] on "+currentHtmlParameter.getType()+" field: ["+currentHtmlParameter.getName()+"]");

						sqlInjectionFoundForUrl = true; 
						continue;
					} //query took longer than the amount of time we attempted to retard it by						
				}  //for each time based SQL index
				//end of Check 3: end of check for time based SQL Injection

			} //end of the for loop around the parameter list

		} catch (Exception e) {
			//Do not try to internationalise this.. we need an error message in any event.. 
			//if it's in English, it's still better than not having it at all. 
			log.error("An error occurred checking a url for Hypersonic SQL Injection vulnerabilities", e);
		}
	}	
}


