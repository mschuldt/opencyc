;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Copyright (c) 1995 - 2000 Cycorp.  All rights reserved.
;;
;; tests/inference-benchmarks.lisp
;;
;; Keith Goolsbey
;; 2000/02/01
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This file contains inference tests which are to be used as 
;; inference efficiency benchmarks.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(in-package "CYC")

(defvar-private
    *test-speed-efficiency-power*
    10)

(def-inference-test
    :speed-efficiency
    '(
      (herald-start)
      (pif (cnot (member *test-direction* '(:backward)))
       (csetq *test-status* :not-run)
       (clet ((power *test-speed-efficiency-power*)
	      (k (- (expt 2 power) 1))
	      mt-1 mt-2
	      collections top-collection bottom-collection
	      parent ancestor sibling
	      terms
	      )
	 (cunwind-protect
	     (progn

	       ;; create a 2-deep mt hierarchy under BaseKB to MT
	       (progn
		 (csetq MT-1 (print-create "Mt-1" 'MT-1))
		 (csetq MT-2 (print-create "Mt-2" 'MT-2))
		 (print-assert
		  `((#$isa ,MT-1 #$Microtheory)
		    (#$isa ,MT-2 #$Microtheory)
		    (#$genlMt ,MT-2 ,MT-1)
		    )
		  #$BaseKB :monotonic :forward)
		 )

	       ;; Create a 25-deep ontology under Thing to collection COL
	       (progn
		 (progress-cdotimes (i 25 "Creating collection ontology~%")
		   (clet ((collection (print-create (format nil "Col-~A" i))))
		     (cpush collection collections)
		     (print-assert
		      `((#$isa ,collection #$Collection))
		      #$BaseKB :monotonic :forward)))
		 (clet ((previous #$Thing))
		   (cdolist (collection collections)
		     (print-assert
		      `((#$genls ,collection ,previous))
		      #$BaseKB :monotonic :forward)
		     (csetq previous collection)))
		 (csetq top-collection (first collections))
		 (csetq bottom-collection (car (last collections))))

	       ;; Create an asymmetric binary predicate PARENT over COL
	       (progn
		 (csetq parent (print-create "parent" 'PARENT))
		 (print-assert
		  `((#$isa ,parent #$IrreflexiveBinaryPredicate)
		    (#$isa ,parent #$AsymmetricBinaryPredicate)
		    (#$arg1Isa ,parent ,top-collection)
		    (#$arg2Isa ,parent ,top-collection))
		  #$BaseKB :monotonic :forward))

	       ;; Create a transitive binary predicate ANCEST over COL
	       (progn
		 (csetq ancestor (print-create "ancestor" 'ANCESTOR))
		 (print-assert
		  `((#$isa ,ancestor #$ReflexiveBinaryPredicate)
		    (#$isa ,ancestor #$AntiSymmetricBinaryPredicate)
		    (#$isa ,ancestor #$TransitiveBinaryPredicate)
		    (#$arg1Isa ,ancestor ,top-collection)
		    (#$arg2Isa ,ancestor ,top-collection))
		  #$BaseKB :monotonic :forward))

	       ;; Create a symmetric binary predicate SIB over COL
	       (progn
		 (csetq sibling (print-create "sibling" 'SIBLING))
		 (print-assert
		  `((#$isa ,sibling #$IrreflexiveBinaryPredicate)
		    (#$isa ,sibling #$SymmetricBinaryPredicate)
		    (#$arg1Isa ,sibling ,top-collection)
		    (#$arg2Isa ,sibling ,top-collection))
		  #$BaseKB :monotonic :forward))

	       ;; define predicates
	       (progn
		 (print-assert
		  `((#$genlPreds ,parent ,ancestor))
		  #$BaseKB :monotonic :forward)
		 (print-assert
		  `((#$implies
		     (#$and
		      (#$different ?CHILD-1 ?CHILD-2)
		      (,parent ?CHILD-1 ?PARENT)
		      (,parent ?CHILD-2 ?PARENT))
		     (,sibling ?CHILD-1 ?CHILD-2)))
		  #$BaseKB :monotonic :backward))

	       ;; For N = 0 to K
	       ;; Create PERSON(N) 
	       ;; Assert (isa PERSON(N) COL) in BaseKB
	       (progn
		 (progress-cdotimes (i k "Creating terms")
		   (clet ((term (print-create (format nil "Term-~A" i))))
		     (cpush term terms)
		     (print-assert
		      `((#$isa ,term ,bottom-collection))
		      #$BaseKB :monotonic :forward)))
		 (csetq terms (apply #'vector (nreverse terms))))
    

	       ;; For N = 1 to K
	       ;; Assert (PARENT PERSON(N) PERSON((N - 1)/2)) in Mt-1
	       (progress-cdotimes (i k "Asserting parent links")
		 (punless (= i 0)
		   (print-assert
		    `((,parent ,(aref terms i) ,(aref terms (int/ (- i 1) 2))))
		    MT-1 :monotonic :forward)))

	       ;; For N = 1 to K
	       ;; ASK (PARENT PERSON(N) ?PARENT) in Mt-2
	       (progress-cdotimes (i k "Asking parent links")
		 (punless (= i 0)
		   (b-verify
		    `(,parent ,(aref terms i) ?PARENT)
		    MT-2
		    )))
	
	       ;; For N = 1 to K
	       ;; ASK (and (ANCEST PERSON(N) ?ANCEST)
	       ;;          (different PERSON(N) ?ANCEST)) in Mt-1
	       (progress-cdotimes (i k "Asking ancestor links")
		 (punless (= i 0)
		   (b-verify
		    `(#$and
		      (,ancestor ,(aref terms i) ?ANCEST)
		      (#$different ,(aref terms i) ?ANCEST))
		    mt-1
		    )))
	
	       ;; For N = 1 to K
	       ;; ASK (SIBLING PERSON(N) ?SIBLING) in Mt-2
	       (progress-cdotimes (i k "Asking sibling links")
		 (punless (= i 0)
		   (b-verify
		    `(,sibling ,(aref terms i) ?SIBLING)
		    mt-2
		    1)))

	       ;; Reassert sibling rule forward
	       (noting-activity "Repropagating sibling rule"
		 (print-assert
		  `((#$implies
		     (#$and
		      (#$different ?CHILD-1 ?CHILD-2)
		      (,parent ?CHILD-1 ?PARENT)
		      (,parent ?CHILD-2 ?PARENT))
		     (,sibling ?CHILD-1 ?CHILD-2)))
		  #$BaseKB :monotonic :forward))

	       ;; For N = 1 to K
	       ;; ASK (SIBLING PERSON(N) ?SIBLING) in Mt-2
	       (progress-cdotimes (i k "Asking sibling links")
		 (punless (= i 0)
		   (b-verify
		    `(,sibling ,(aref terms i) ?SIBLING)
		    mt-2
		    ))))

	   ;; (break "end of test")
      
	   ;; cleanup
	   (progn
	     ;; For N = 0 to K
	     ;; KILL PERSON(N)
	     (progress-cdotimes (i k "Killing terms")
	       (print-kill `(,(aref terms i))))
	     (print-kill collections)
	     (print-kill
	      `(,SIBLING ,ANCESTOR ,PARENT ,MT-1 ,MT-2))))
	
	 )				; close clet
       )

      (update-test-results *current-test*)
  
      ))


(define-public metrics-speed-efficiency (&optional (power 10))
  (check-type power non-negative-integer-p)
  (clet ((harness-count (- (expt 2 1) 1))
	 (harness-time (metrics-speed-efficiency-internal 1))
	 (full-count (- (expt 2 power) 1))
	 (full-time (metrics-speed-efficiency-internal power))
	 (delta-count (- full-count harness-count))
	 (delta-time  (- full-time harness-time))
	 (efficiency (/ delta-time delta-count))
	 (cyclops (/ efficiency)))
    (format t "~%System ~S.~S KB ~S"
	    (cycl-system-number)
	    (cycl-patch-number)
	    (kb-loaded))
    (format t "~%Scaling factor :~% ~S" delta-count)
    (format t "~%Elapsed time (seconds) :~% ~S" delta-time)
    (format t "~%Efficiency (seconds/op) :~% ~S" efficiency)
    (format t "~%CycLOPs :~% ~S~%" cyclops))
  (ret nil))

;; internals
    
(define-private metrics-speed-efficiency-internal (power)
  (clet (time)
    (clet ((*test-speed-efficiency-power* power))
      (ctime time (run-test :speed-efficiency)))
    (ret time)))
   	   
