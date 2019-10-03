@echo off
echo Setting JAVA_HOME
set CATALINA_HOME=C:\Program Files (x86)\apache-tomcat-7.0.55
set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_45
echo setting PATH
set PATH=C:\Program Files\Java\jdk1.6.0_45\bin;C:\Program Files (x86)\apache-tomcat-7.0.55\bin;%PATH%
echo Display java version
java -version