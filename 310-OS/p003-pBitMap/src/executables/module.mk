# @file module.mk
# ------------------------------------------------------
# The main subsystem (module) make include file.
# ------------------------------------------------------
# Each .cpp file is assumed to be named for an executable to be
# built. Each, individually, will build an object file and be
# linked into an executable.

LOCALSOURCE := $(dir $(lastword $(MAKEFILE_LIST)))

# single-level directory name
TOOLS_DIR := $(LOCALSOURCE:$(SOURCE)/%/=%)

# echo the name of the folder being processed
q := $(shell echo "$(LOCALSOURCE)" 1>&2)

# append the submodule directory to the list of include directories
# for C compiler
INCLUDES += -I $(LOCALSOURCE)

# append BUILD modified version of directory name to list of build
# directories (so the directories are made if necessary)
MYBUILD := $(patsubst $(SOURCE)/%,$(BUILD)/%,$(LOCALSOURCE))
BUILDDIRS += $(MYBUILD)

# Make a list of the names of executable tools to build
TOOLS := $(patsubst $(LOCALSOURCE)%.cpp,%,$(wildcard $(LOCALSOURCE)*.cpp))
