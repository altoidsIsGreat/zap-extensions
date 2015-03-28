# Add-ons: Beta #

Add-ons marked as 'beta' status can be expected to be of a reasonable quality and mostly fit for purpose.

However they may be incomplete or need further testing.

They will typically:
  * Have been developed or code reviewed by one or more members of the ZAP core team
  * Have no known significant issues
  * Be fully internationalised
  * Support dynamic loading and unloading
  * Mostly conform the to [ZAP development rules and guidelines](http://code.google.com/p/zaproxy/wiki/DevGuidelines)
  * Ideally have a zap-extensions wiki page (eg [Script Console](AddOn_scripts.md))
  * Active scan rules will correctly check the isStop() method so that they dont hang