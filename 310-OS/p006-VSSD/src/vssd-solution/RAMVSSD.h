/* Alex Pendell
 * CIS 303 - Operating Systems
 * RAMVSSD.h
 * November 12, 2019
 *
 * See RAMVSSD.cpp for writeup
 */

#ifndef RAMVSSD_H
#define RAMVSSD_H

#include "VVSSD.h"
#include <string>
#include <iostream>
#include <vector>


class RAMVSSD : public VVSSD {
 public:

  unsigned int block_s, block_c;
  std::vector<std::string> memory;
  DiskStatus stat = DiskStatus::OK;
  
  RAMVSSD(unsigned int block_size, unsigned int block_count,
             const std::string & filename, bool initialize);

  ~RAMVSSD();
  
  std::size_t blockSize() const;
  
  std::size_t blockCount() const;
  
  DiskStatus status() const;
  
  DiskStatus read(blocknumber_t block, void * buffer);
  
  DiskStatus write(blocknumber_t block, void * buffer);         

  DiskStatus sync();
};


#endif /* RAMVSSD_H */
