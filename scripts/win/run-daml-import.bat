@echo off
rem  Run DAML import application
h:
pushd \opencyc

set CLASSPATH=h:\opencyc\lib\junit.jar
set CLASSPATH=h:\opencyc\lib\ViolinStrings.jar;%CLASSPATH%
set CLASSPATH=h:\opencyc\lib\jakarta-oro-2.0.3.jar;%CLASSPATH%
set CLASSPATH=h:\opencyc\lib\commons-collections.jar;%CLASSPATH%
set CLASSPATH=h:\opencyc\lib\FIPA_OSv2_1_0.jar;%CLASSPATH%
set CLASSPATH=h:\opencyc\lib\jena.jar;%CLASSPATH%
set CLASSPATH=h:\opencyc\lib\jug.jar;%CLASSPATH%
set CLASSPATH=h:\opencyc\lib\jdom.jar;%CLASSPATH%
set CLASSPATH=h:\opencyc\lib\xerces.jar;%CLASSPATH%
set CLASSPATH=h:\opencyc\lib\icu4j.jar;%CLASSPATH%
set CLASSPATH=classes;%CLASSPATH%

echo classpath=%CLASSPATH%

java -classpath %CLASSPATH% org.opencyc.xml.ImportSonatDaml

popd