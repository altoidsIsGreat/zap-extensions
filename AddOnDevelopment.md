# Add-on Development #

This page gives an overview of the process of developing an add-on and getting it published.

## Implementation ##

Not surprisingly the first thing to do (apart from deciding what to implement) is to create the first version of your add-on.

For help with that see:
  * [Building ZAP](http://code.google.com/p/zaproxy/wiki/Building)
  * [Extending ZAP](http://code.google.com/p/zaproxy/wiki/DevExtending)
  * [ZAP Add-ons](http://code.google.com/p/zaproxy/wiki/ZapAddOns)
  * [ZAP Internal Details](http://code.google.com/p/zaproxy/wiki/InternalDetails)

## [Alpha version](AddOnsAlpha.md) ##
Once you have something you would like to publish you will need commit access to this project. If you dont already have it then ask on the [ZAP developer group](http://groups.google.com/group/zaproxy-develop) giving us a brief overview of your add-on

You should then add your code to the [alpha branch](https://code.google.com/p/zap-extensions/source/browse/#svn%2Fbranches%2Falpha)

Let us know when you are happy with it (on the [dev group](http://groups.google.com/group/zaproxy-develop) again) and we'll give it an initial review.

This review will just be to check basic things like:
  * It builds(!)
  * It loads as an add-on in the latest released version of ZAP
  * It doesnt do anything obviously bad
As long as it passes those criteria then it will be released and available to install from within ZAP.

You may continue to develop your extension while its in the alpha branch. If and when you would like another alpha version published let us know via the [dev group](http://groups.google.com/group/zaproxy-develop) - this will _not_ happen automatically.

## [Beta version](AddOnsBeta.md) ##
There are additional [requirements](AddOnsBeta.md) for add-ons to achieve beta status.

If and when you think your add-on is ready to move to beta status then let us know via the [dev group](http://groups.google.com/group/zaproxy-develop)

It will then be reviewed, and when it has passed the review you will be able to move it to the [beta branch](https://code.google.com/p/zap-extensions/source/browse/#svn%2Fbranches%2Fbeta)

Again, let us know when it is ready to be published (and republished if relevant).

## [Release version](AddOnsRelease.md) ##
Again, ther are additional [requirements](AddOnsRelease.md) for add-ons to achieve release status.

If and when you think your add-on is ready to move to release status then let us know via the [dev group](http://groups.google.com/group/zaproxy-develop)

It will then be reviewed, and when it has passed the review you will be able to move it to the [trunk](https://code.google.com/p/zap-extensions/source/browse/#svn%2Ftrunk)

Again, let us know when it is ready to be published (and republished if relevant).
