**JBLab JVM-bytecode Decompiler**

Yet another JVM-bytecode decompiler in some programming languages such as Java, Kotlin, JavaScript.
Fast, easy scalable. Can be used as IntelliJ IDEA plugin or console application.

**Running process**
To run decompiler from console just build it using maven from root pom.xml

To test decompiler as IDEA plugin follow these instructions:

1. Create IDEA plugin project in /plugin
2. Import core module from pom.xml
3. Add to plugin project module dependecy to core
4. Make sure that IDEA Platform SDK is on the bottom of dependecy lists of all projects
5. Setup Kotlin runtime (choose java or maven smth in popup notification)
6. Optional: use java 1.6 and language level at least 6
7. Edit 'Run/Debug configurations' for plugin run
8. Be happy
