# Add-on Structure #

Add-ons allow you to dynamically add functionality to ZAP.

They can contain:
  * Extensions, ie code that includes a class which extends [Extension.java](http://code.google.com/p/zaproxy/source/browse/trunk/src/org/parosproxy/paros/extension/Extension.java)
  * Active scan rules, which extend [AbstractPlugin.java](http://code.google.com/p/zaproxy/source/browse/trunk/src/org/parosproxy/paros/core/scanner/AbstractPlugin.java)
  * Passive scan rules, which extend [PluginPassiveScanner.java](http://code.google.com/p/zaproxy/source/browse/trunk/src/org/zaproxy/zap/extension/pscan/PluginPassiveScanner.java)
  * 'Raw' files (see below)
  * Help files, which must be under 'resource/help' and follow a standard structure (to be documented)
  * Libraries, which will be automatically loaded and must be under 'lib'

Add-ons have a standard naming convention:

  * _name_-_status_-_version_.zap

Where
  * _name_ is the last element of the add-on package name
  * _status_ is one or "alpha", "beta", "release"
  * _version_ is an integer which increments on each release

Add-ons are jar (zip) files and must contain a file called [ZapAddOn.xml](https://code.google.com/p/zap-extensions/source/browse/trunk/src/org/zaproxy/zap/extension/ZapAddOn.xml) at the top level.

This file defines the contents of the add-on and is used by ZAP to dynamically load and unload it.

### Raw Files ###
To include 'raw files' include them in a directory structure underneath a 'files' directory. Reference the files in the [ZapAddOn.xml](https://code.google.com/p/zap-extensions/source/browse/trunk/src/org/zaproxy/zap/extension/ZapAddOn.xml) file - these files will then be installed in the right places in the same directory structure under the users ZAP home directory.

See the [fuzzdb](https://code.google.com/p/zap-extensions/source/browse/#svn%2Ftrunk%2Fsrc%2Forg%2Fzaproxy%2Fzap%2Fextension%2Ffuzzdb) add-on for a good example.

### Building your add-on ###

You should use the 'build-addon' task in the relevant [build.xml](https://code.google.com/p/zap-extensions/source/browse/branches/alpha/build/build.xml) file to create add-ons.

As long as your add-on is structured as above then you should just need to make the following changes (where _myaddon_ is the package name of your add-on):

Target: build-all add:
```
    <antcall target="build-addon"><param name="addon" value="myaddon"/></antcall>
```
New target:
```
    <target name="deploy-myaddon" depends="build-all" description="deploy the myaddon extension">
        <antcall target="deploy-extension"> <param name="extension" value="myaddon"/> </antcall>
    </target>

```
Target: deploy-all add:
```
    <antcall target="deploy-myaddon"/>
```

You can then just use the 'deploy-myaddon' to build and deploy your add-on to your 'zaproxy' project.

You can also just use the 'build-all' target and then manually import your add-on into ZAP using the "File/Load Add-on file..." menu option, for example if you want to test your add-on with a version of ZAP that you have not built yourself. Your add-on will be under the 'build/zap-exts' directory.