# Add-on Debugging #

Roughly, the procedure is:
  * Have both zaproxy and zaproxy-extensions open in Eclipse
  * package and deploy the add-on in the zaproxy/src/plugin using the ant build of zaproxy-extensions
  * Refresh the zaproxy  project
  * Run ZAP in debug mode
  * Put your breakpoint anywhere in the add-on code
  * Execute your scenario
  * When the breakpoint is reached, you may be prompted to define where source are located. Select java project and "zaproxy-extensions"
  * That's all.

You just have to not forget the package/deploy phase.