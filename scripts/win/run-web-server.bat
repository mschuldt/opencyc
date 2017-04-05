REM WebServer.sh
REM Execute the webserver that overcomes
REM OpenCyc's default localhost-only http client connections.
REM written by Stephen Reed  4/4/2002
REM Assumes java 2 is in your path

REM At this point the cyc http server is running and you can access
REM Cyc directly via the local web browser, or from a remote internet
REM web browser by substituting your host name or IP address for localhost.
REM http://localhost:3063/cgi-bin/cyccgi/cg?cb-start
java -cp ..\..\lib\OpenCyc.jar -Dorg.opencyc.webserver.port=3603 -Dorg.opencyc.webserver.dirs=..\..\run\httpd\htdocs org.opencyc.webserver.WebServer
