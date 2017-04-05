Installation instructions for OpenCyc release 0.7.0 on Win32

OpenCyc for Win32 requires Microsoft NT, Win2K or XP, about 250MB of 
disk space and performs best with over 256 MB RAM.  It does not work 
with Windows 98, ME or earlier versions of Windows. The java api 
requires Java 2 and the download of freely available third-party jar 
files.

For our extensive documentation: http://www.opencyc.org

1. Unzip the compressed archive opencyc-0.7.0.zip.  The top directory has 
the same name as the OpenCyc release.

2. cd to opencyc-0.7.0\scripts\win and launch the Cyc server.

run-cyc.bat

After some time loading the world into memory you will see 

CYC(1):

which is the SubL command prompt.

3. [optional] 
You can enter SubL expressions such as (+ 1 2) or (genls #$Person)
or (all-genls #$Person) at the command line to verify Cyc's operation.
 
4. At this point the cyc http server is running and you can access
Cyc directly via the local web browser.

http://localhost:3602/cgi-bin/cyccgi/cg?cb-start

You can browse cyc via the Guest account or perform updates by
logging on as CycAdministrator.

See the file parameters.lisp in the init directory for startup 
options including choice of tcp ports utilized, and whether remote tcp 
connections to your cyc server are permitted (default is not permitted).

5. In a separate browser window, access the following URL:

http://www.opencyc.org/cb/welcome

This is the welcome screen. It will eventually be integrated into the 
KB Browser. There is information on getting started with the Cyc Server,
and there is a walkthrough of entering some knowledge into Cyc.

6. You can cleanly shut down the OpenCyc server by entering (exit) on
command line, or you can click on the halt link on the Cyc Browser's
System page.

7.  You can save the state of the OpenCyc world by entering the form
(write-image "world/<filename>") before shutting down using
(exit).  To restart OpenCyc:

run-cyc-world world/<filename> 




