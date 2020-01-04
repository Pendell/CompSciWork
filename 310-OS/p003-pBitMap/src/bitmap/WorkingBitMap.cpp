/*
 * Alex Pendell
 * CIS 303 - Operating Systems
 * p003 - pBitMap
 * September 24th, 2019
 *
 * This is the working implementation of BitMap.h.
 *
 * The following are copied right from BitMap.cpp, although modified
 * to actually work.
 */

#include "WorkingBitMap.h"
#include <iostream>

using namespace std;

// This is the bitmap
char* bitmap;

// This is the number of entries, just shortened to n for ease of typing and it's how I wrote
// it in my notes.
unsigned int n;
int nbytes;

/**
 * Construct a BitMap for holding the given number of entries.
 * Initially, all entries are to be unset.
 *
 * @param number_of_entries the number of entries that bitmap should track
 */
WorkingBitMap::WorkingBitMap(unsigned int number_of_entries) : BitMap(number_of_entries){
  // Since we're allocating space in increments of 8, we need to find how many 8's we have,
  // and possibly add 1 (for the extra space if we have a remainder)
  n = number_of_entries;
  nbytes = n / 8 + ((n % 8 == 0)?0:1);
  bitmap = new char[nbytes];
  unset();
}

/**
 * Clean up after the BitMap as necessary.
 */
WorkingBitMap::~WorkingBitMap() {
  delete[] bitmap;
}

/**
 * Query the number of entries that this BitMap contains.
 *
 * @return the number of entries the BitMap was initialized with
 */
unsigned int WorkingBitMap::entry_count() {
  return n;
}

/**
 * Set ALL bits in the BitMap.
 */
void WorkingBitMap::set() {
  for (int i = 0; i < nbytes; i++){
    bitmap[i] = 0xFF;
  }
}

/**
 * Unset ALL bits in the BitMap.
 */
void WorkingBitMap::unset() {
  for (int i = 0; i < nbytes; i++){
    bitmap[i] = 0x00;
  }
}

/**
 * Get the lowest index of a bit that is not set.
 *
 * @return the smallest index [0,entry_count) of a bit that is unset if
 *   such a bit exists; return nbit otherwise
 */
unsigned int WorkingBitMap::get_unset_index() {
  for (unsigned int i = 0; i < n; i++){
    if(get(i) == false) {
    	return i;
    }
  }
  return nbit;
}

/**
 * Get the bit at the given index as a Boolean: true if the bit is
 * set to 1; false if the bit is unset to 0.
 *
 * @param index the index [0,entry_count) of the bit to get the value of
 * @return true if index is valid and the given bit is set; false otherwise
 *   (if index is invalid OR the validly indexed bit is unset).
 */
bool WorkingBitMap::get(unsigned int index) {
  return bitmap[index/8] & 1 << (index % 8);
}

/**
 * Set the bit at the given index to 1 if it is a valid index; do
 * nothing if the index is out of range.
 *
 * @param index the index [0,entry_count) of the bit to set
 */
void WorkingBitMap::set(unsigned int index) {
  if(index < n)
     bitmap[index/8] |= (1 << (index % 8));
}

/**
 * Set the bit at the given index to 0 if it is a valid index; do
 * nothing if the index is out of range.
 *
 * @param index the index [0,entry_count) of the bit to get the value of
 */
void WorkingBitMap::unset(unsigned int index) {
  if (index < n)
    bitmap[index/8] &= ~(1 << (index % 8));
}
