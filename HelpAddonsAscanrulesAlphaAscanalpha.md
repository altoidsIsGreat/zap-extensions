# Active Scan Rules - alpha #
The following alpha quality active scan rules are included in this add-on:
## Backup File Disclosure ##
Scans for commonly-named backup copies of files on the web server, which may reveal sensitive information
## Example File Active Scanner ##
This implements an example active scan rule that loads strings from a file that the user can edit.<br>For<br>
more details see: <a href='http://zaproxy.blogspot.co.uk/2014/04/hacking-zap-4-active-scan-rules.html'>http://zaproxy.blogspot.co.uk/2014/04/hacking-zap-4-active-scan-rules.html</a>
<h2>Example Simple Active Scanner</h2>
This implements a very simple example active scan rule.<br>For more details see: <a href='http://zaproxy.blogspot.co.uk/2014/04/hacking-zap-4-active-scan-rules.html'>http://zaproxy.blogspot.co.uk/2014/04/hacking-zap-4-active-scan-rules.html</a>
<h2>Expression Language Injection</h2>
The software constructs all or part of an expression language (EL) statement in a Java Server Page (JSP)<br>
using externally-influenced input from an upstream component, but it does not neutralize or incorrectly<br>
neutralizes special elements that could modify the intended EL statement before it is executed. In certain<br>
versions of Spring 3.0.5 and earlier, there was a vulnerability (CVE-2011-2730) in which Expression Language<br>
tags would be evaluated twice, which effectively exposed any application to EL injection. However, even<br>
for later versions, this weakness is still possible depending on configuration.<br>
<h2>Source Code Disclosure - File Inclusion</h2>
Uses local file inclusion techniques to scan for files containing source code on the web server<br>
<h2>Source Code Disclosure - SVN</h2>
Uses Subversion source code repository metadata to scan for files containing source code on the web server<br>
<h2>Source Code Disclosure - Git</h2>
Uses Git source code repository metadata to scan for files containing source code on the web server<br>
<h2>Heartbleed OpenSSL Vulnerability</h2>
Detects if the web server is vulnerable to the Heartbleed OpenSSL Vulnerability