# Bean Shell Console #

The BeanShell is an interactive Java shell that can be used to execute BeanShell scripts. These scripts
are a simplified form of Java that use many elements from Java syntax, but in a simpler scripting format.
All Java code is also valid BeanShell code.<br>BeanShell integration in OWASP ZAP enables you to write scripts<br>
using the ZAP functions and data set. This can be a very powerful feature for analyzing web applications.<br>
<br>
<h2>BeanShell Console</h2>

The console is started from the Tools menu, and contains a split screen where the top half is the interactive<br>
BeanShell console and the bottom half is a simple text editor. For complex scripts, you're encouraged<br>
to use a Java editor. Scripts can be loaded, saved and evaluated from the editor. When the BeanShell<br>
starts a number of objects from ZAP are available for use, namely:<br>
<ul><li>the <i>Model</i> singleton, through the object named <i>model</i>
</li><li>the <i>SiteTree</i> tree of current sites through the <i>sites</i> object<br>
</li><li>an instance of <pre>HttpSender</pre>through the <i>sender</i> object<br>
Notice that the BeanShell is loosely typed. Therefore, it is not necessary to declare variables before<br>
using them – this makes scripts a bit more concise than regular Java. But of course, if you did want<br>
to define the type you can.<br>
<h2>Using the Site Map</h2></li></ul>

Let's start with something useful and typical: Iterate through all the site nodes and check for the existence<br>
of a file. A script has already been created that accomplishes this, choose Load and select the example.tree.bsh<br>
file. Before clicking Evaluate, first browse to a site through ZAP to populate the tree: <br><br>Now click on<br>
evaluate to execute the script that's in the editor. If there are no errors, then you can now start using<br>
the object defined in the script by issuing these commands:<br>
<pre>
t = Tree();<br>
</pre>
Which constructs a new Tree object and assigns a reference to t.<br>
<pre>
t.find(sites.getRoot(), "index.html");<br>
</pre>
Call the find method on t, which takes a SiteNode as the first argument and a resource to find as the<br>
second. In this case, the method will iterate through all the nodes in the tree, because we started at<br>
the root, and will find index.html files. <br><br>Instead of iterating through all the nodes, we could choose<br>
to start a specific node by using the findChild method e.g.: <br><br>This should give you some idea of the power<br>
of the BeanShell in ZAP. But to fully exploit it, you'll need to familiarize yourself with the internal<br>
API and the BeanShell's features. The BeanShell has been setup so as to allow full access to all internal<br>
objects and methods – even private ones.<br>
<h2>Simple HTTP Request</h2>

In the next example, we create and send an HTTP request directly in the interactive console: To fully<br>
utilize the power of the BeanShell, you should familiarize yourself with ZAP's internals. The sender<br>
object is the same instance as is used by the Manual Request Editor and will therefore automatically<br>
use proxy settings set in the ZAP configuration. <br><br>TODO: POST example<br>
<h2>Tips</h2>

Use the unset(String) command to unset any declared variables, methods or objects. This is useful if<br>
you want to replace a method declaration in the current namespace. Note that the command takes a String<br>
argument, not an Object, so to unset the t object we used above, it should be: unset(“t”); and not unset(t);<br>
<br>
<br>
Original document by: Stephen de Vries