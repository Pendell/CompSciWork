/* Alex Pendell
 * CIS 303 - Operating Systems
 * FileVSSD.h
 * November 12, 2019
 *
 * See FileVSSD.cpp for writeup and comments
 */


#ifndef FILEVSSD_H
#define FILEVSSD_H

#include "VVSSD.h"
#include "../io/PersistentArray.h"
#include <string>
#include <fstream>

class FileVSSD : public VVSSD {
 public:

  PersistentArray * disk;
  unsigned int block_s, block_c;
  DiskStatus stat = DiskStatus::OK;
  
  FileVSSD(unsigned int block_size, unsigned int block_count,
             const std::string & filename, bool initialize);

  ~FileVSSD();
  
  std::size_t blockSize() const;
  
  std::size_t blockCount() const;
  
  DiskStatus status() const;
  
  DiskStatus read(blocknumber_t block, void * buffer);
  
  DiskStatus write(blocknumber_t block, void * buffer) ;

  DiskStatus sync();
  
};


#endif /* FILEVSSD_H */
