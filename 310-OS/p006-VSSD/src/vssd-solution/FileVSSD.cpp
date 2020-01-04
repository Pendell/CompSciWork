/* Alex Pendell
 * CIS 303 - Operating Systems
 * fileVSSD.cpp
 * November 12, 2019
 *
 * This is the FileVSSD portion of the assignment. This cpp file is meant
 * to mimic the functionality of a spinning hard disk. This is accomplished
 * using the PersistentArray class that was given to us to use how we see fit
 * We read and write to the 'hard disk' by utilizing the PersistentArray's 
 * get() and set() functions. get() is passed a block #, and returns a void*
 * of whatever is in that location which we read into a buffer. set() is given
 * a block number, and a buffer, and will write to that block whatever the 
 * contents of buffer are.
 *
 * Currently, there seems to be seg core dump error when it's ran in a specific
 * sequence of commands. Also, similarly to the RAMVSSD, strange characters
 * are printed when reading, and reading seems to only read the last strings
 * that were written to the disk...
 */

#include "FileVSSD.h"
#include <iostream>
#include <cstring>
#include <fstream>


/* This struct is how we store the metadata to the file system.
 * The metadata we're storing in block 0 is the block count, and the
 * block size. This is necessary because when opening a pre-existing
 * file, we need to make sure that the format (the block count and 
 * block size) are the same, otherwise data might bleed into other 
 * blocks and cause loss of data.
 */
struct Metadata {
  unsigned int size, count;

  Metadata(int block_size, int block_count){
    size = block_size;
    count = block_count;
  }
};
  

FileVSSD::FileVSSD(unsigned int block_size, unsigned int block_count,
	 const std::string & filename, bool initialize) {

  // How big each block is
  block_s = block_size;

  // How many blocks are in the file
  block_c = block_count;

  // Construct metadata that we're going to store in block 0
  Metadata* meta = new Metadata(block_s, block_c);

  // Initialize the array and write to block 0 the metadata
  // pertaining to this file.
  disk = new PersistentArray(filename);
  disk->write_k(0, (char*)meta);
  
  stat = DiskStatus::OK;
}

FileVSSD::~FileVSSD(){
  delete(disk);
}

// return the block size
std::size_t FileVSSD::blockSize() const {
  return block_s;
}

// Return the block count
std::size_t FileVSSD::blockCount() const {
  return block_c;
}

// Return the last status set by the disk
DiskStatus FileVSSD::status() const {
  return stat;
}

/* read() takes a block and buffer and reads the contents of the 
 * block (located in the hard disk) and puts them into the buffer.
 * parameters:
 *   blocknumber_t block - the block we're reading from
 *   void* buffer - the buffer we're reading into
 * postcondition:
 *   buffer now contains the contents of the PersistentArray at
 *   location 'block'.
 */
DiskStatus FileVSSD::read(blocknumber_t block, void * buffer) {
  if(block == 0) {
    // Trying to acces block 0?
    std::cout << "Block 0 is privileged" << std::endl;
    stat = DiskStatus::ERROR;
  } else if (block >= block_c) {
    // Is the block out of range?
    std::cout << "Out of bounds. Enter a valid block" << std::endl;
    stat = DiskStatus::BLOCK_OUT_OF_RANGE;
  } else {
    // We're all good, let's read.
    // This was a potential work-around to the seg core fault
    // but it seems to only work some of the time.
    if(buffer != nullptr){
      free(buffer);
    }
    buffer = nullptr;
    buffer = disk->get(block);
    stat = DiskStatus::OK;
    
  }
  return stat;
}

/* write() takes a block and buffer and reads the contents of the
 * buffer into the PersistentArray at location 'block'.
 * parameters:
 *   blocknumber_t block - the block we're writing into.
 *   void* buffer - the buffer we're writing from.
 * postcondition:
 *   block now contains the contents of buffer. Then buffer is 
 *   free'd
 */
DiskStatus FileVSSD::write(blocknumber_t block, void * buffer) {
   if(block == 0) {
     std::cout << "Block 0 is privileged" << std::endl;
     stat = DiskStatus::ERROR;
   } else if (block >= block_c) {
     std::cout << "Out of bounds. Enter a valid block" << std::endl;
     stat = DiskStatus::BLOCK_OUT_OF_RANGE;
   } else {
     disk->set(block, (char*)buffer);
     if(buffer != nullptr){
       free(buffer);
     }
     buffer = nullptr;
     stat = DiskStatus::OK;
   }

   return stat;
}

DiskStatus FileVSSD::sync(){
  return DiskStatus::OK;
}




