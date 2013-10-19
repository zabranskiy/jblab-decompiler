#JBLab Decompiler

Yet another one JVM byte-code decompiler for several programming languages such as Java, Kotlin, JavaScript.

Fast, easy scalable, based on ASM. Can be used as an IntelliJ IDEA plugin or a console application.

###Usage practice

To run a decompiler from the console just build it by using Maven '...\core\pom.xml'

To test a decompiler as an IDEA plugin follow these instructions:

1. Check out from GitHub
2. Configure an IntelliJ IDEA Plugin SDK
3. Create an IDEA plugin project in '...\plugin' <br/>
   Be sure that you chose an Intellij Platform Plugin SDK for the plugin module SDK.
4. Import a core module from '...\core\pom.xml' by Maven
5. Add the core module to the plugin module dependencies 
6. Set JDK 1.6+ as the core module SDK
7. Setup the Kotlin runtime (choose java or maven smth in the popup notification)
8. Optional: use java 1.6 and language level at least 6
9. Edit 'Run/Debug configurations' to run the plugin
