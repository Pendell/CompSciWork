#lang scheme
; Alex Pendell
; p003 - Learning Scheme II
; CIS 443 - Programming Languages
; February 19, 2019


; lhs is a function that returns the left side of a symbolic expression
(define (lhs expr)
  (cadr expr))

; rhs is a function that returns the right side of a symbolic expression
(define (rhs expr)
  (car (cddr expr)))

; op is a function that returns the operator in a symbolic alebraic expression
(define (op expr)
  (car expr))

; This is the recursive helper function to the caesar function below.
; It takes a number generated from the parent function which is the offset to move all the following characters as well as a list representation of the string
; It returns a new string that is essentially the old string shifted via the 'shift' parameter.
; THIS STILL PRINTS UNICODE FOR SOME REASON...
(define (caesar-helper shift char-list)
  (append
   (if (and (char? (car char-list))(char-lower-case? (car char-list)))
       (list (integer->char (+ (- shift (char->integer #\a)) (char->integer (car char-list)))))
       (car char-list))
   (if (null? (cdr char-list))
      '()
      (caesar-helper shift (cdr char-list)))))
  
; a function that takes a letter to be the new 'a' and a word to shift. Passes these parameters into the helper function
(define (caesar letter word)
  (list->string (caesar-helper (- (char->integer letter) (char->integer #\a)) (string->list word))))
   

; a helper function to the mono-encipher
(define (encipher-helper alpha word-list)
  (append
  (list(string-ref alpha (- (char->integer (car word-list))(char->integer #\a))))
   (if (null? (cdr word-list))
       '()
       (encipher-helper alpha (cdr word-list)))))

; monoalphabetic-encipher takes a string of length 26 and another string. Return the second string with all lower-case letters replaced with corresponding characters in the first string.
(define (monoalphabetic-encipher alpha word)
  (list->string (encipher-helper alpha (string->list word))))


; The following three functions are for the decipher.
; The approach is that we first find what index the character in the word is with respect to the alphabet given.
; we then take that number and add it to the normal english alphabet, which will decipher the word for us.
(define (decipher-find alpha find-letter count)
  (if (char=? (car alpha) find-letter)
      count
      (decipher-find (cdr alpha) find-letter (+ count 1))))
  

(define (decipher-helper alpha word-list)
  (let ([findnumber (decipher-find alpha (car word-list) 0)])
  (append
   (list(integer->char (+ (char->integer #\a) findnumber )))
   (if (null? (cdr word-list))
       '()
       (decipher-helper alpha (cdr word-list))))))


(define (monoalphabetic-decipher alpha word)
  (list->string (decipher-helper (string->list alpha) (string->list word))))