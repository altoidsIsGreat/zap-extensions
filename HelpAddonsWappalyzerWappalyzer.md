# Technology Detection using Wappalyzer #

The Technology Detection add-on uses the Wappalyzer (http://wappalyzer.com/) rules to detect the technologies
used by applications.

It works in a very similar way to the Wappalyzer browser add-ons with the following exceptions:
  * It does not use the 'Global JavaScript variables' as these are difficult to test without a 'full' browser
  * It does not use the 'meta tags'- this is still todo
  * It does not not show the versions - this is still todo
  * It does not not show the confidence - this is still todo
  * It allows you to see the 'evidence' used to detect the technologies
## The Technology Tab ##
This tab shows all of the detected technologies for the site selected.<br>Right clicking on a technology<br>
will display a 'Show evidence' menu underneight which are all of the regexes used to detect it.<br>Selecting<br>
a regex will switch to the 'Search' tab and search through the history for that regex.<br>
<h2>External links</h2>
<table>
<tr><td></td><td><a href='http://wappalyzer.com/'>http://wappalyzer.com/</a></td><td>The Wappalyzer homepage</td></tr>
<tr><td></td><td><a href='https://github.com/ElbertF/Wappalyzer/'>https://github.com/ElbertF/Wappalyzer/</a></td><td>The Wappalyzer repository</td></tr>
</table>