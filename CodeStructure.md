The code is structured in svn as follows:

## v1.4 Extensions ##
All extensions for ZAP 1.4 line in the [1.4 branch](https://code.google.com/p/zap-extensions/source/browse/#svn%2Fbranches%2F1.4)

## v2.2 Extensions ##

### 2.2 Alpha Extensions ###
New extensions should typically be added to the [alpha branch](https://code.google.com/p/zap-extensions/source/browse/#svn%2Fbranches%2Falpha)

Code in this branch must compile, but may not be complete or have a variety of issues.

When you are happy that your code is working correctly and follows _most_ of the [development rules and guidelines](http://code.google.com/p/zaproxy/wiki/DevGuidelines) post a request to the ZAP [developer group](http://groups.google.com/group/zaproxy-develop) to ask for a review. If the general consensus is that it is ready then you can move it to the beta branch.

### 2.2 Beta Extensions ###
Code in the [beta branch](https://code.google.com/p/zap-extensions/source/browse/#svn%2Fbranches%2Fbeta) has been reviewed and can be expected to be generally fit for purpose.

When you are happy that your code is ready for release status and follows _all_ of the [development rules and guidelines](http://code.google.com/p/zaproxy/wiki/DevGuidelines) post a request to the ZAP [developer group](http://groups.google.com/group/zaproxy-develop) to ask for another review. If the general consensus is that it is ready then you can move it to the trunk.

### 2.2 Release Extensions ###
Code in the [trunk](https://code.google.com/p/zap-extensions/source/browse/trunk) has been reviewed and can be expected to be of high quality.

## Notes ##
Version numbers should increase sequentially when changes are made or when the extension moves to a new branch or the trunk.

You do not have to perform a release when you change the code - its up to the author to decide when their code is ready.

We would not normally expect an extension to drop, eg from release to beta, but that can happen if there are problems with it.

An extension should have only one release, and in any case the code will only show the most recent release.

In the 'check for updates' code the version number takes precedence - so an alpha-3 release is an update to a beta-2 release.

If you need to use features in your add-on that are only available in the trunk then people get in touch via the ZAP developer group.