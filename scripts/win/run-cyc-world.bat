REM run-cyc-world.bat

REM Execute the cyc server with the specified world 
REM written by Stephen Reed  8/27/2002
REM After some time loading the world into memory you will see CYC(1):
REM which is the SubL command prompt.
REM ***************************************************************
REM [optional]
REM You can enter SubL expressions such as (+ 1 2) or (genls #$Person)
REM or (all-genls #$Person) at the command line to verify Cyc's operation.
REM ***************************************************************
REM At this point the cyc http server is running and you can access
REM Cyc directly via the local web browser.
REM http://localhost:3602/cgi-bin/cyccgi/cg?cb-start
REM You can browse cyc via the Guest account or perform updates by
REM logging on as CycAdminstrator.
REM As a security measure, Cyc by default only accepts tcp connections 
REM from the computer that it runs on.  You can forward ports via ssh 
REM to enable remote clients, provide middleware (see run-web-server.bat) 
REM to do the same. 
REM see the file parameters.lisp in the init directory for startup
REM options including choice of tcp ports utilized, and enabling remote
REM connections.

pushd ..\..\run
bin\latest.exe -w %1
popd