/* Alex Pendell
 * CIS 303 - Operating Systems
 * RAMVSSD.cpp
 * November 12, 2019
 *
 *
 * This is the RAMVSSD portion of the assignment. We were tasked with implementing
 * a storage class that functions as actual RAM does. This is accomplished by
 * creating a vector of strings, then writing to and reading from that vector.
 * 
 * In it's current state, when you write that seems to be getting stored somewhere
 * that's being read no matter what block to you tell it to read from.
 * For example, when you write to block 2 the string "hello", when attempting to
 * read, say, block 3, the test program will output hello, even though we wrote to 2.
 * This leads me to believe I'm not clearing memory properly.
 *
 * A side note, similar to the FileVSSD, there seems to be some characters in the
 * beginning of the read output that don't belong. One of them (I'm guessing) looks
 * like a null character? I'm unsure what the others are.
 */


#include "RAMVSSD.h"
//#include <stdio.h>
#include <string.h>

RAMVSSD::RAMVSSD(unsigned int block_size, unsigned int block_count,
		 const std::string & filename, bool initialize) {


  // Grab the block size and block count
  block_s = block_size;
  block_c = block_count;
  

  // 'zero' out all of the locations in memory. So we don't have any artifacts
  // of previous residents.
  for(unsigned int i = 0; i < block_c; i++){
    memory.push_back("");
  }

  // This line will put into memory location 1 the metadata needed to check
  // initialization. The metadata is a string of the format "block_s/block_c".
  // We can just compare strings when it comes time to compare. This is the
  // best way I could think of doing it right now.
  memory.at(0) = "" + std::to_string(block_s) + "/" + std::to_string(block_c);
  
}

RAMVSSD::~RAMVSSD(){
  // Removes all elements from the vector, leaving it with a size of 0
  memory.clear();
}

// Get and return the block size
std::size_t RAMVSSD::blockSize() const {
  return block_s;
}

// Get and return the block count
std::size_t RAMVSSD::blockCount() const {
  return block_c;
}

// Return the latest status
DiskStatus RAMVSSD::status() const {
  return stat;
}

/* read() takes a buffer and a location in the memory vector,
 * and reads the contents of that location into the buffer.
 * parameters:
 *   blocknumber_t block - the block (location) we are reading from
 *   void* buffer - the location we are reading into.
 * postconditions:
 *   buffer now contains the contents of memory.at(block)
 */
DiskStatus RAMVSSD::read(blocknumber_t block, void* buffer) {
  
  if (block == 0){
    // Are we trying to reach block 0?
    stat = DiskStatus::ERROR;
    std::cout << "Block 0 is privileged." << std::endl;
    
  } else if (block >= block_c){
    // Are we trying to reach a block that's out of bounds?
    std::cout << "Block is out of bounds." << std::endl;
    stat = DiskStatus::BLOCK_OUT_OF_RANGE;
  } else {
    // No to both of those? Okay let's read.
    // Empty the buffer first to give it a clean slate to read into
    free(buffer);
    // For some reason, I can't get it to work by directly coding
    // buffer into strncopy, using a temp was the only way I could
    // get it to make progress.
    char tempbuffer[block_s];
    strncpy(tempbuffer, memory.at(block).c_str(), block_c);
    buffer = tempbuffer;
    stat = DiskStatus::OK;
  }
  return stat;
}

/* write() takes a buffer and a location in the memory vector,
 * and writes the contents of the buffer to that location.
 * parameters:
 *   blocknumber_t block - the block (location) we are writing to
 *   void* buffer - the contents of which we are writing
 * postconditions:
 *   The contents of memory at location 'block' now contains
 *   the contents of 'buffer'. Buffer is also freed at the end.
 */
DiskStatus RAMVSSD::write(blocknumber_t block, void* buffer){
  if (block == 0){
    stat = DiskStatus::ERROR;
    std::cout << "Block 0 is privileged." << std::endl;
  } else if (block >= block_c){
    std::cout << "Block is out of bounds." << std::endl;
    stat = DiskStatus::BLOCK_OUT_OF_RANGE;
  } else {
    memory.at(block) = (char*)buffer;
    free(buffer);
    stat = DiskStatus::OK;
  }
  return stat;
}

/* Currently returns last status of RAM */
DiskStatus RAMVSSD::sync(){
  return stat;
}
