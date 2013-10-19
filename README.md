#JBLab Decompiler

Yet another one JVM byte-code decompiler for several programming languages such as Java, Kotlin, JavaScript.

Fast, easy scalable, based on ASM and a control flow graph dominator tree analysis.<br/>
Can be used as an IntelliJ IDEA plugin or a console application.

###Usage practice

To run the decompiler from the console just build it from Maven pom file '...\core\pom.xml'

To test the decompiler as an IDEA plugin follow these instructions:

1. Check out from GitHub
2. Configure an IntelliJ IDEA Plugin SDK
3. Create an IDEA plugin project in '...\plugin' <br/>
   Be sure that you chose an Intellij Platform Plugin SDK for the plugin module SDK.
4. Import a core module from Maven pom file '...\core\pom.xml'
5. Set JDK 1.6+ as the core module SDK
6. Add the core module to the plugin module dependencies
7. Optional: setup the Kotlin runtime, use java 1.6 and language level at least 6
8. Edit 'Run/Debug configurations' to run the plugin
