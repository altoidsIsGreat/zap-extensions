# Passive Scan Rules - Beta #
The following beta quality passive scan rules are included in this add-on:
## Charset Mismatch ##
This check identifies responses where the HTTP Content-Type header declares a charset different from
the charset defined by the body of the HTML or XML. When there's a charset mismatch between the HTTP
header and content body Web browsers can be forced into an undesirable content-sniffing mode to determine
the content's correct character set.
## Cookie - Loosely Scoped ##
Cookies can be scoped by domain or path. This check is only concerned with domain scope.The domain scope
applied to a cookie determines which domains can access it. For example, a cookie can be scoped strictly
to a subdomain e.g. www.nottrusted.com, or loosely scoped to a parent domain e.g. nottrusted.com. In
the latter case, any subdomain of nottrusted.com can access the cookie. Loosely scoped cookies are common
in mega-applications like google.com and live.com.
## CSRF Countermeasures ##
## Information Disclosure: Debug Errors ##
## Information Disclosure: In URL ##
## Information Disclosure: Referrer ##
## Information Disclosure: Suspicious Comments ##
## Insecure Authentication ##
HTTP basic or digest authentication has been used over an unsecured connection. The credentials can be
read and then reused by someone with access to the network.
## Insecure JSF View State ##
The response at the following URL contains a ViewState value that has no cryptographic protections.
## Servlet Parameter Pollution ##
## Viewstate ##