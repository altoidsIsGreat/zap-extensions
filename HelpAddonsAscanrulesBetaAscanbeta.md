# Active Scan Rules - beta #
The following beta quality active scan rules are included in this add-on:
## CSRF Token ##
## HPP ##
## LDAP Injection ##
LDAP Injection may be possible. It may be possible for an attacker to bypass authentication controls,
and to view and modify arbitrary data in the LDAP directory.
## Session Fixation ##
Session Fixation may be possible. If this issue occurs with a login URL (where the user authenticates
themselves to the application), then the URL may be given by an attacker, along with a fixed session
id, to a victim, in order to later assume the identity of the victim using the given session id. If the
issue occurs with a non-login page, the URL and fixed session id may only be used by an attacker to track
an unauthenticated user's actions. If the vulnerability occurs on a cookie field or a form field (POST
parameter) rather than on a URL (GET) parameter, then some other vulnerability may also be required in
order to set the cookie field on the victim's browser, to allow the vulnerability to be exploited.
## SQL Injection - Hypersonic, time based ##
This scanner uses Hypersonic-specific SQL syntax to attempt to induce time delays in the SQL statement
called by the page.<br>If the unmodified query is not affected by a time delay, and the modified query's<br>
delay can be controlled, it is indicative of a time-based SQL Injection vulnerability in a Hypersonic<br>
SQL database. <br>This scanner is time sensitive, and should only be used in an attempt find find stubborn<br>
and un-obvious SQL injection vulnerabilities in a suspected Hypersonic database. <br>For this reason, the<br>
number of active scan threads should be set to the minimum when using this scanner, to minimise load<br>
on the web server, application server, and database, in order to avoid false positives caused by load<br>
delays rather than by SQL injection delays. <br>The scanner tests only for time-based SQL injection vulnerabilities.<br>
<h2>SQL Injection - MySQL, time based</h2>
Similar to the Hypersonic scanner, but specific to the MySQL RDBMS and SQL syntax.<br>
<h2>SQL Injection - Oracle, time based</h2>
Similar to the Hypersonic scanner, but specific to the Oracle RDBMS and SQL syntax.<br>
<h2>SQL Injection - Postgresql, time based</h2>
Similar to the Hypersonic scanner, but specific to the PostgreSQL RDBMS and SQL syntax.<br>
<h2>Username Enumeration</h2>
It may be possible to enumerate usernames, based on differing HTTP responses when valid and invalid usernames<br>
are provided. This would greatly increase the probability of success of password brute-forcing attacks<br>
against the system. Note that false positives may sometimes be minimised by increasing the 'Attack Strength'<br>
Option in ZAP. Please manually check the 'Other Info' field to confirm if this is actually an issue.<br>
<h2>Xpath Injection</h2>
<h2>XXE</h2>
<h2>Padding Oracle</h2>