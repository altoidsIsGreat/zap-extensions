# Passive Scan Rules - Alpha #
The following alpha quality passive scan rules are included in this add-on:
## ASP.NET ViewState Disclosure ##
An ASP.NET ViewState was disclosed by the application/web server
## ASP.NET ViewState Integrity ##
The application does not use a Message Authentication Code (MAC) to protect the integrity of the ASP.NET
ViewState, which can be tampered with by a malicious client
## Base64 Disclosure ##
Base64 encoded data was disclosed by the application/web server
## Missing Content Security Policy Header ##
This checks response headers for the presence of a Content Security Policy header.
## Cookie poisoning ##
This check looks at user-supplied input in query string parameters and POST data to identify where cookie
parameters might be controlled. This is called a cookie poisoning attack, and becomes exploitable when
an attacker can manipulate the cookie in various ways. In some cases this will not be exploitable, however,
allowing URL parameters to set cookie values is generally considered a bug.
## Example File Passive Scanner ##
This implements an example passive scan rule that loads strings from a file that the user can edit.<br>For<br>
more details see: <a href='http://zaproxy.blogspot.co.uk/2014/04/hacking-zap-3-passive-scan-rules.html'>http://zaproxy.blogspot.co.uk/2014/04/hacking-zap-3-passive-scan-rules.html</a>
<h2>Example Simple Passive Scanner</h2>
This implements a very simple example passive scan rule.<br>For more details see: <a href='http://zaproxy.blogspot.co.uk/2014/04/hacking-zap-3-passive-scan-rules.html'>http://zaproxy.blogspot.co.uk/2014/04/hacking-zap-3-passive-scan-rules.html</a>
<h2>Hash Disclosure</h2>
A hash was disclosed by the web server<br>
<h2>HTTP to HTTPS insecure transition in form post</h2>
This check looks for insecure HTTP pages that host HTTPS forms. The issue is that an insecure HTTP page<br>
can easily be hijacked through MITM and the secure HTTPS form can be replaced or spoofed.<br>
<h2>HTTPS to HTTP insecure transition in form post</h2>
This check identifies secure HTTPS pages that host insecure HTTP forms. The issue is that a secure page<br>
is transitioning to an insecure page when data is uploaded through a form. The user may think they're<br>
submitting data to a secure page when in fact they are not.<br>
<h2>Open redirect</h2>
Open redirects are one of the OWASP 2010 Top Ten vulnerabilities. This check looks at user-supplied input<br>
in query string parameters and POST data to identify where open redirects might be possible. Open redirects<br>
occur when an application allows user-supplied input (e.g. <a href='http://nottrusted.com'>http://nottrusted.com</a>) to control an offsite<br>
redirect. This is generally a pretty accurate way to find where 301 or 302 redirects could be exploited<br>
by spammers or phishing attacks<br>
<h2>Server Header Version Information Leak</h2>
This checks response headers for the presence of a server header that contains version details.<br>
<h2>Source Code Disclosure</h2>
Application Source Code was disclosed by the web server<br>
<h2>Missing Strict Transport Security Header</h2>
This checks HTTPS response headers for the presence of a HTTP Strict Transport Security header.<br>
<h2>Timestamp Disclosure</h2>
A timestamp was disclosed by the application/web server<br>
<h2>User controllable HTML element attribute (potential XSS)</h2>
This check looks at user-supplied input in query string parameters and POST data to identify where certain<br>
HTML attribute values might be controlled. This provides hot-spot detection for XSS (cross-site scripting)<br>
that will require further review by a security analyst to determine exploitability.<br>
<h2>User controllable charset</h2>
This check looks at user-supplied input in query string parameters and POST data to identify where Content-Type<br>
or meta tag charset declarations might be user-controlled. Such charset declarations should always be<br>
declared by the application. If an attacker can control the response charset, they could manipulate the<br>
HTML to perform XSS or other attacks.<br>
<h2>User controllable javascript event (XSS)</h2>
This check looks at user-supplied input in query string parameters and POST data to identify where certain<br>
HTML attribute values might be controlled. This provides hot-spot detection for XSS (cross-site scripting)<br>
that will require further review by a security analyst to determine exploitability.<br>
<h2>User controllable javascript property (XSS)</h2>
This check looks at user-supplied input in query string parameters and POST data to identify where URL's<br>
in certain javascript properties (e.g. createElement src) might becontrolled. This provides hot-spot<br>
detection for XSS (cross-site scripting) that will require further review by a security analyst to determine<br>
exploitability.<br>
<h2>X-Powered-By Header Information Leak</h2>
This checks response headers for the presence of X-Powered-By details.<br>
<h2>X-Backend-Server Header Information Leak</h2>
This checks response headers for the presence of X-Backend-Server details.