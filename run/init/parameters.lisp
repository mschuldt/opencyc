;;; -*- Mode: LISP; Syntax: ANSI-Common-Lisp; Base: 10; Package: CYC;  -*-

;;; Cyc system parameters
;;;
;;; Generated 12/01/2002 17:58:26 from CycL 1.3039

(in-package :CYC)

;;; Possible values: NIL, T.  If NIL, transcript problems will cause error
;;; breaks that make the system stop.  If T, such problems will not cause
;;; breakage.
(csetq *AUTO-CONTINUE-TRANSCRIPT-PROBLEMS* T)

;;; Possible values: NIL, T.  If NIL, agenda errors will cause the system to
;;; halt.  If T, they will be automatically continued.
(csetq *CONTINUE-AGENDA-ON-ERROR* T)

;;; Possible values: T, NIL.  Type checking occurs in SBHL modules iff this
;;; is nil.
(csetq *SUSPEND-SBHL-TYPE-CHECKING?* T)

;;; Possible values: T, NIL.  If NIL, the System Info page (accessible to
;;; administrators only) will estimate, rather than actually count, the
;;; number of operations in the transcript.  If T, it will actually count
;;; them, which takes longer but is accurate.
(csetq *REALLY-COUNT-TRANSCRIPT-OPS* NIL)

;;; Possible values: NIL, T.  If NIL, a local transcript will always be
;;; written when operations are done, even if those operations are also being
;;; written to the master transcript.  If T, then the image does not write
;;; to a local transcript file, and will write to the master transcript when
;;; it is set to transmit operations.  This allows an image that is standalone,
;;; and always in :TRANSMIT-AND-RECEIVE, to keep only a single copy of its
;;; operations.
(csetq *DONT-RECORD-OPERATIONS-LOCALLY* NIL)

;;; Possible values -- :TRANSMIT-AND-RECEIVE, :RECEIVE-ONLY,
;;; :ISOLATED, :DEAF, :DEAD.  This is the communication mode the cyc image
;;; should get initialized to at startup.
(csetq *STARTUP-COMMUNICATION-MODE* :DEAF)

;;; Possible values: T, NIL.  If NIL, the Cyc agenda is not started at
;;; startup, but can be enabled later by the user.  If T, the agenda is enabled
;;; at startup.
(csetq *START-AGENDA-AT-STARTUP?* T)

;;; The base port offset for all the TCP services for the Cyc image.
(csetq *BASE-TCP-PORT* 3600)

;;; Possible values: A number.  This parameter specifies the offset of the html port
;;; from *base-tcp-port*.
(csetq *HTML-PORT-OFFSET* 0)

;;; Possible values: A number.  This parameter specifies the offset of the Cyc API
;;; (application program interface) service from *base-tcp-port*.
(csetq *FI-PORT-OFFSET* 1)

;;; Possible values: A number.  This parameter specifies the offset of the http port
;;; from *base-tcp-port*.
(csetq *HTTP-PORT-OFFSET* 2)

;;; Possible values: A number.  This parameter specifies the offset of the Cyc
;;; CFASL-server service from *base-tcp-port*.
(csetq *CFASL-PORT-OFFSET* 14)

;;; Possible values: T, NIL.  IF NIL, then remote TCP clients can connect to Cyc, otherwise no remote connections are allowed. The most secure configuration leaves this parameter T, and uses a separate Web server to redirect HTTP requests to Cyc.
(csetq *TCP-LOCALHOST-ONLY?* T)

;;; Possible values: T, NIL.  IF T, then API functions can access host services including the file system and outbound tcp connections.  The most secure configuration leaves this parameter NIL.
(csetq *PERMIT-API-HOST-ACCESS* NIL)

;;; Possible values: T, NIL.  IF T, then writing to the master transcript will be controlled by the Cyc Transcript Server, which will need to be installed at your site.  You only need to set this to T if you are running multiple instances of Cyc.  If NIL, then Cyc will read and write to the master transcript without regard to other processes doing the same.
(csetq *USE-TRANSCRIPT-SERVER* NIL)

;;; Possible values: NIL or a string.  This parameter is only used if *USE-TRANSCRIPT-SERVER* is T.  If so, then this parameter should be set to the name of the host offering the
;;; cyc-serializer service.
(csetq *MASTER-TRANSCRIPT-LOCK-HOST* NIL)

;;; Possible values: A number.  This parameter is only used if
;;; *USE-TRANSCRIPT-SERVER* is T.  If so, then this
;;; parameter should be set to the port number of the cyc-serializer
;;; read service.
(csetq *MASTER-TRANSCRIPT-SERVER-PORT* 3608)

;;; Possible values: T, NIL.  If NIL, tools for modifying the knowledge base are not accessible.
(csetq *CB-EDITING-ENABLED?* T)

;;; Possible values: T, NIL.  If NIL, require authentication before allowing
;;; modifications to the knowledge base.  If T, any user is allowed to
;;; modify the knowledge base.
(csetq *ALLOW-GUEST-TO-EDIT?* NIL)

;;; Possible values: The name of a constant representing a Cyclist.  This is the
;;; default Cyclist initially logged into the system.
(csetq *DEFAULT-CYCLIST-NAME* "Guest")

;;; The directory under which documents served by the HTTP server are stored.
(csetq *HTTP-HTDOCS-DIRECTORY* "httpd/htdocs")

;;; The directory under which Cyc images (.gif or otherwise) are stored
(csetq *HTML-IMAGE-DIRECTORY* "/cycdoc/img/")

;;; Possible values: T, NIL.  If T, the html tools will correctly display UTF-8 text
;;; derived from Cyc strings.
(csetq *PERMIT-UTF-8-CHARACTER-DISPLAY* T)

;;; The URL for the Cyc system documentation directory.
(csetq *CYC-DOCUMENTATION-URL* "/cycdoc/")

;;; Possible values: A string.  The name of the CGI program that acts as the
;;; intermediary between your WWW server and a Cyc processs.  Normally, the
;;; program will be called ``cg''.  However, if your WWW server requires that
;;; CGI program names have a certain form, such as ``cg.exe'', then change
;;; this parameter to conform.
(csetq *CYC-CGI-PROGRAM* "cg")

;;; Possible values: NIL, T.  If T, the HTML browser allows users to  search for
;;; constants via regular expressions.  Use of this facility requires the
;;; constant-name-grep CGI program and the data file constant-shell.text to
;;; be installed on your WWW server.
(csetq *CONSTANT-NAME-GREP-ENABLED* NIL)

;;; Possible values: A string.  The name of the CGI program that is used for
;;; performing regular expression searches over constant names.  Normally, the
;;; program will be called ``constant-name-grep''.  However, if your WWW server
;;; requires that CGI program names have a certain form, such as
;;; ``constant-name-grep.exe'', then change this parameter to conform.
(csetq *CYC-GREP-CGI-PROGRAM* "constant-name-grep")

;;; Possible values: One of the symbols :CYCORP or :UNKNOWN. If the execution context
;;; is set to :CYCORP, then the CYC image can assume that it is running in
;;; Cycorp's development environment and make strong assumptions about setup
;;; and infrastructure.
(csetq *CYC-EXECUTION-CONTEXT* :UNKNOWN)

(check-system-parameters)
