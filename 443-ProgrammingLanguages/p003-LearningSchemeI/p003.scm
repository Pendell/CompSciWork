#lang scheme

;; Alex Pendell
;; CIS443 - Programming Languages
;; p003 - Learning Scheme I
;; February 10th, 2019

;; in-range? is a function that is passed 3 variables. The first is 'tst' and is the number we are examining.
;; The next two, 'low' and 'high' are the boundaries that the test number needs to be between, including low, but excluding high.
(define (in-range? tst low high)
  (and (>= tst low) (< tst high)))


;; The follwing is a helper method to the method below. It takes x as a parameter where x is the random number generated in the method below.
;; 'tries' is simply a counter of how many tries you've guessed
;; It first prints "Guess my number: ", prompts for user input, then returns with feedback whether the number guessed is too high, or low, or correct.
;; If the number guessed is correct, the number of tries is also printed.
(define (helper x tries)
  (newline)
  (display "Guess my number: ")
  (let ([guess (read)])
  (cond [(= x guess) (display "You got it in ") (display tries) (display " tries!")]
        [(> guess x) (display "Too high.") (helper x (+ tries 1))]
        [(< guess x) (display "Too low.") (helper x (+ tries 1))])))

;; This is where the 'guess-my-number' game is initiated. It accepts two parameters in which to select an integer from, low, and high. If low=high, then
;; the program will terminate and print "No numbers given in range." Otherwise it will call the helper method above it to continue the game.
(define (guess-my-number low high)
  (cond [(= low high) ((display "No numbers in given range")(exit))])
  (let ([x (+ (random(- high low)) low)])
  (helper x 1)))