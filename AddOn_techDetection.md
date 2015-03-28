# Technology Detection (alpha) #

The Technology Detection add-on uses the Wappalyzer (http://wappalyzer.com/) rules to detect the technologies used by applications.<p>

It works in a very similar way to the Wappalyzer browser add-ons with the following exceptions:<br>
<ul><li>It does not use the 'Global Javascript variables' as these are difficult to test without a 'full' browser<br>
</li><li>It does not use the 'meta tags'- this is still todo<br>
</li><li>It does not not show the versions - this is still todo<br>
</li><li>It does not not show the confidence - this is still todo<br>
</li><li>It allows you to see the 'evidence' used to detect the technologies</li></ul>

<h2>The Technology Tab</h2>
This tab shows all of the detected technologies for the site selected.<br>
Right clicking on a technology will display a 'Show evidence' menu underneath which are all of the regexes used to detect it.<br>
Selecting a regex will switch to the 'Search' tab and search through the history for that regex.