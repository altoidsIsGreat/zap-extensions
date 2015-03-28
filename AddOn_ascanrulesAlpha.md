# Active scanner rules (alpha) #

The following alpha quality active scan rules are included in this add-on:

### Backup File Disclosure ###
Scans for commonly-named backup copies of files on the web server, which may reveal sensitive information

### Example Simple Active Scanner ###
This implements a very simple example active scan rule.<br>

<h3>Expression Language Injection</h3>
The software constructs all or part of an expression language (EL) statement in a Java Server Page (JSP) using externally-influenced input from an upstream component, but it does not neutralize or incorrectly neutralizes special elements that could modify the intended EL statement before it is executed. In certain versions of Spring 3.0.5 and earlier, there was a vulnerability (CVE-2011-2730) in which Expression Language tags would be evaluated twice, which effectively exposed any application to EL injection. However, even for later versions, this weakness is still possible depending on configuration.<br>
<br>
<h3>Source Code Disclosure - File Inclusion</h3>
Uses local file inclusion techniques to scan for files containing source code on the web server<br>
<br>
<h3>Source Code Disclosure - SVN</h3>
Uses Subversion source code repository metadata to scan for files containing source code on the web server<br>
<br>
<h3>Source Code Disclosure - Git</h3>
Uses Git source code repository metadata to scan for files containing source code on the web server<br>
<br>
<h3>Heartbleed OpenSSL Vulnerability</h3>
Detects if the web server is vulnerable to the Heartbleed OpenSSL Vulnerability