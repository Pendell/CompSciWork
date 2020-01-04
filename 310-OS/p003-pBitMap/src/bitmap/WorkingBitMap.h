/*
 * Alex Pendell
 * CIS 303 - Operating Systems
 * p003 - pBitMap
 * September 24th, 2019 
 *
 * This is the working implementation of the BitMap.cpp file.
 * 
 */

#ifndef WORKINGBITMAP_H
#define WORKINGBITMAP_H
#include "BitMap.h"

class WorkingBitMap : public BitMap {
  // The following are copied right from the BitMap.h file.
  public:

  // the value returned by get_unset_entry to indicate no entry available
  constexpr static unsigned int nbit = 0xFFFFFFFF;

  WorkingBitMap(unsigned int number_of_entries);
  virtual ~WorkingBitMap();

  // return the number of entries in the bit map
  virtual unsigned int entry_count();

  // unset all entries in the bitmap
  virtual void unset();

  // set all entries in the bitmap
  virtual void set();

  // return the lowest unset index
  virtual unsigned int get_unset_index();

  // return whether entry at the given index is set; out of range always unset
  virtual bool get(unsigned int index);

  // set entry at given index; ignore out-of-range indices
  virtual void set(unsigned int index);

  // set entry at given index; ignore out-of-range indices
  virtual void unset(unsigned int index);

};

#endif