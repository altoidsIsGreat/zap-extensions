# Active scanner rules (beta) #

The beta quality Active Scanner rules.

The rules include

### Anti CSRF tokens scanner ###
### Session Fixation ###
### LDAP Injection ###
### SQL Injection (Hypersonic SQL, Time based) ###
This scanner uses Hypersonic-specific SQL syntax to attempt to induce time delays in the SQL statement called by the page.  If the unmodified query is not affected by a time delay, and the modified query's delay can be controlled, it is indicative of a time-based SQL Injection vulnerability in a Hypersonic SQL database.  This scanner is time sensitive, and should only be used in an attempt find find stubborn and un-obvious SQL injection vulnerabilities in a suspected Hypersonic database.  For this reason, the number of active scan threads should be set to the minimum when using this scanner, to minimise load on the web server, application server, and database, in order to avoid false positives caused by load delays rather than by SQL injection delays.  The scanner tests only for time-based SQL injection vulnerabilities.

### SQL Injection (MySQL, Time based) ###
Similar to the Hypersonic scanner, but specific to the MySQL RDBMS and SQL syntax.

### SQL Injection (Oracle, Time based) ###
Similar to the Hypersonic scanner, but specific to the Oracle RDBMS and SQL syntax.

### SQL Injection (PostgreSQL, Time based) ###
Similar to the Hypersonic scanner, but specific to the PostgreSQL RDBMS and SQL syntax.

### HTTP Parameter Pollution scanner ###
### Possible Username Enumeration ###