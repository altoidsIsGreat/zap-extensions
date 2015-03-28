# Upgrade to 2.4 #

## Database abstraction layer ##
A database abstraction layer has been introduced at this version to make it easier to support different databases in addition to the default HSQLDB.

Methods that used to throw
```
    java.sql.SQLException
```
now throw
```
    org.parosproxy.paros.db.DatabaseException
```

The db classes are now all interfaces, and implemented by classes in org.parosproxy.paros.db.paros package prefixed by 'Paros'.

Add-ons should never access the db tables directly, instead they should access the data via the classes that 'own' the tables. If they do not provide the data required then they should be enhanced to do so.

New tables should be registered with ZAP as per TBA.

## Add-on dependencies ##

Add-ons can now explicitly depend on other add-ons.

Add-ons that require add-ons should specify the add-ons the depend on - details TBA.

## Setting Fonts ##
ZAP now allows the user to specify the default font size, which doesnt work if fixed fonts are explicitly used.

Add-ons should typically _not_ set the font size.

If there is a need to set the font size or style then the [org.zaproxy.zap.view.FontUtils](https://code.google.com/p/zaproxy/source/browse/trunk/src/org/zaproxy/zap/utils/FontUtils.java) static methods should be used.