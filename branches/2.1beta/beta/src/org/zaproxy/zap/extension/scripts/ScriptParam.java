/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2012 The ZAP development team
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
package org.zaproxy.zap.extension.scripts;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.log4j.Logger;
import org.parosproxy.paros.common.AbstractParam;

public class ScriptParam extends AbstractParam {

	private static final String SCRIPTS_BASE_KEY = "script";

	private static final String PARAM_DEFAULT_SCRIPT = SCRIPTS_BASE_KEY + ".defaultScript";
	private static final String PARAM_DEFAULT_DIR = SCRIPTS_BASE_KEY + ".defaultDir";
	
    private static final String ALL_SCRIPTS_KEY = SCRIPTS_BASE_KEY + ".scripts";

    private static final String SCRIPT_NAME_KEY = "name";
    private static final String SCRIPT_DESC_KEY = "description";
    private static final String SCRIPT_ENGINE_KEY = "engine";
    private static final String SCRIPT_TYPE_KEY = "type";
    private static final String SCRIPT_FILE_KEY = "file";
    private static final String SCRIPT_ENABLED_KEY = "enabled";

    private static final Logger logger = Logger.getLogger(ScriptParam.class);

	private String defaultScript = null;
	private String defaultDir = null;
	private Set<ScriptWrapper> scripts;

	@Override
	protected void parse() {
		defaultScript = getConfig().getString(PARAM_DEFAULT_SCRIPT, "");
		defaultDir = getConfig().getString(PARAM_DEFAULT_DIR, "");
		
        try {
            List<HierarchicalConfiguration> fields = ((HierarchicalConfiguration) getConfig()).configurationsAt(ALL_SCRIPTS_KEY);
            this.scripts = new HashSet<>(fields.size());
            List<String> tempListNames = new ArrayList<>(fields.size());
            for (HierarchicalConfiguration sub : fields) {
                String name = sub.getString(SCRIPT_NAME_KEY, "");
                if (!"".equals(name) && !tempListNames.contains(name)) {
                    tempListNames.add(name);
                    
                    File file = new File(sub.getString(SCRIPT_FILE_KEY));
                    if ( ! file.exists()) {
                        logger.error("Script '" + file.getAbsolutePath() + "' does not exist");
                    	continue;
                    }
                    
                    ScriptWrapper script = new ScriptWrapper(
                        sub.getString(SCRIPT_NAME_KEY),
                        sub.getString(SCRIPT_DESC_KEY),
                        sub.getString(SCRIPT_ENGINE_KEY),
                        ScriptWrapper.Type.valueOf(sub.getString(SCRIPT_TYPE_KEY)),
                        sub.getBoolean(SCRIPT_ENABLED_KEY),
                        file);
                    
                    script.setLoadOnStart(true);	// Because it was saved ;)
                    
                    scripts.add(script);
                }
            }
        } catch (ConversionException e) {
            logger.error("Error while loading the auto tag scanners: " + e.getMessage(), e);
        }

	}
	
	public void addScript (ScriptWrapper script) {
		this.scripts.add(script);
	}

	public void removeScript (ScriptWrapper script) {
		this.scripts.remove(script);
	}

	public void saveScripts() {
        ((HierarchicalConfiguration) getConfig()).clearTree(ALL_SCRIPTS_KEY);

        int i=0;
        for (ScriptWrapper script : scripts) {
        	if (script.isLoadOnStart()) {
	            String elementBaseKey = ALL_SCRIPTS_KEY + "(" + i + ").";
	            getConfig().setProperty(elementBaseKey + SCRIPT_NAME_KEY, script.getName());
	            getConfig().setProperty(elementBaseKey + SCRIPT_DESC_KEY, script.getDescription());
	            getConfig().setProperty(elementBaseKey + SCRIPT_ENGINE_KEY, script.getEngineName());
	            getConfig().setProperty(elementBaseKey + SCRIPT_TYPE_KEY, script.getType().toString());
	            getConfig().setProperty(elementBaseKey + SCRIPT_ENABLED_KEY, Boolean.valueOf(script.isEnabled()));
	            getConfig().setProperty(elementBaseKey + SCRIPT_FILE_KEY, script.getFile().getAbsolutePath());
	            i++;
        	}
        }
	}
	
	public Set<ScriptWrapper> getScripts() {
		return scripts;
	}

	public String getDefaultScript() {
		return defaultScript;
	}

	public void setDefaultScript(String defaultScript) {
		this.defaultScript = defaultScript;
		getConfig().setProperty(PARAM_DEFAULT_SCRIPT, this.defaultScript);
	}

	public String getDefaultDir() {
		return defaultDir;
	}

	public void setDefaultDir(String defaultDir) {
		this.defaultDir = defaultDir;
		getConfig().setProperty(PARAM_DEFAULT_DIR, this.defaultDir);
	}

	
}