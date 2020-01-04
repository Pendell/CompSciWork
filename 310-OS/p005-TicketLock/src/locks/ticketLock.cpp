/* Alex Pendell
 * CIS 310 - Operating Systems
 * p005 - pTicketLock
 * October 22, 2019
 *
 * The TicketLock is a sort of extension to the SpinLock, however, it is fair (unlike Spinlock).
 * The primary attribute that makes TicketLock more fair is that it functions similar to a deli
 * counter. That is, a process approaches the ticket dispenser, and pulls a ticket (which increments
 * the next ticket to be pulled). The deli also has a 'Now Serving' display, which determines
 * which thread is making progress.
 */

#include "ticketLock.h"
#include <thread>
#include <chrono>
#include "SpinLock.h"

TicketLock::TicketLock(unsigned int turnCount) {
  turn = 0;
  nowServing = 1;
  totalTurns = turnCount;
  spinlock = new SpinLock();
}

/* From the textbook:
 * "When a thread wishes to acquire a lock, it first does an
 * atomic fetch-and-add on the ticket value; that value is now
 * considered this thread's "turn" (my turn). A globally shared
 * lock->turn is used to determine which thread's turn it is.
 * When (myturn == turn) for a given thread, it is that thread's
 * turn to enter the critical section."
 */
TicketLock::Ticket TicketLock::lock() volatile {

  // Since evil increment is evil, we need to lock around it to make sure that it gets done
  // otherwise, the process that's running evil_increment() might pump the brakes, and turn
  // won't get incremented correctly.
  spinlock->lock();
  // Create the ticket to return
  TicketLock::Ticket *ticket = new TicketLock::Ticket(TicketLock::evil_increment(turn) % totalTurns, nowServing % totalTurns);
  spinlock->unlock();
  
  while(ticket->ticket != nowServing % totalTurns) {
    // Wait your turn.
  }
  return *ticket;
}

/* This unlocks the lock by incrementing the now serving. Since everyone is still spinning until
 * nowServing gets incremented, we don't need to encapsulate this in a SpinLock.
 */
void TicketLock::unlock() volatile {
  TicketLock::evil_increment(nowServing);
}

// slowly increment the given variable; increment and delay are both defaulted
unsigned int TicketLock::evil_increment(volatile unsigned int &toBeIncremented, int incrementToAdd, unsigned int millisecondsToHold)  volatile {
  unsigned int initialValue = toBeIncremented;
  std::this_thread::sleep_for(std::chrono::milliseconds(millisecondsToHold));
  toBeIncremented = initialValue + incrementToAdd;
  return toBeIncremented;
}
