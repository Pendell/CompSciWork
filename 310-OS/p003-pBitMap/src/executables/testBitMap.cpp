/**
 * Test code for BitMap and (when uncommented) WorkingBitMap.
 *
 * BitMap is as unimplemented as possible so that when the test code is
 * run with it, the 1 test case fails with about 6 failed assertions; there
 * are loops with assertions and the first failure causes the test code to
 * abort the loop. There is therefore about one failure per SECTION.
 *
 * The assertions that DO pass for BitMap are checks that the code returns
 * a false Boolean value for "unset" bits; the BitMap code returns false no
 * matter what index is read.
 *
 *
 */
#define CATCH_CONFIG_MAIN  // This tells Catch to provide a main()
                           // - only do this in one cpp file
#include "catch.hpp"

#include "BitMap.h"
#include "WorkingBitMap.h"

TEST_CASE("BitMap testing:", "[BitMap]") {
  const unsigned int map_size = 100;
  BitMap * b;
  #ifdef WORKINGBITMAP_H
  INFO("b is a WorkingBitMap");
  b = new WorkingBitMap(map_size);
  #else
  INFO("b is a BitMap");
  b = new BitMap(map_size);
  #endif

  SECTION("entry_count properly set.") {
    REQUIRE(b->entry_count() == map_size);
  }

  SECTION("Starts fully unset.") {
    for (unsigned int i = 0; i < map_size; i++) {
      INFO("i = " << i);
      REQUIRE(!b->get(i));
    }
  }

  SECTION("BitMap::set() works.") {
    b->set();
    for (unsigned int i = 0; i < map_size; i++) {
      INFO("i = " << i);
      REQUIRE(b->get(i));
    }
  }

  SECTION("BitMap::unset() works.") {
    b->unset();
    for (unsigned int i = 0; i < map_size; i++) {
      INFO("i = " << i);
      REQUIRE(!b->get(i));
    }
  }

  SECTION("BitMap::set(i) works.") {
    for (unsigned int i = 0; i < map_size; i += 2)
      b->set(i);
    // evens should be set; odds not.
    for (unsigned int i = 0; i < map_size; i++) {
      INFO("i = " << i);
      REQUIRE(b->get(i) == (i % 2 == 0));
    }
  }

  b->unset();

  SECTION("BitMap::set(i) works.") {
    b->set();
    for (unsigned int i = 0; i < map_size; i += 2)
      b->unset(i);
    // odds should still be set; evens not.
    for (unsigned int i = 0; i < map_size; i++) {
      INFO("i = " << i);
      REQUIRE(b->get(i) == (i % 2 != 0));
    }
  }

  b->unset();

  SECTION("BitMap::get_unset_index() works when space is available.") {
    for (unsigned int i = 0; i < map_size / 2; i++) {
      unsigned int next = b->get_unset_index();
      REQUIRE(next != BitMap::nbit);
      REQUIRE(!b->get(next));
      b->set(next);
    }

    // first half should be set
    for (unsigned int i = 0; i < map_size; i++) {
      INFO("i = " << i);
      REQUIRE(b->get(i) == (i < (map_size / 2)));
    }
  }

  SECTION("BitMap::get_unset_index() works when internal space is available.") {
    b->set(); // no space available
    for (unsigned int i = 0; i < map_size; i += 2) {
      INFO("i = " << i);
      b->unset(i); // one spot available
      unsigned int next = b->get_unset_index();
      REQUIRE(next == i);
      REQUIRE(!b->get(next));
      b->set(next);
    }
  }


  SECTION("BitMap::get_unset_index() works when space is unavailable.") {
    b->set();
    unsigned int next = b->get_unset_index();
    REQUIRE(next == BitMap::nbit);
  }

  delete b;
}
