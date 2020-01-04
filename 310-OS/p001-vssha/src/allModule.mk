# @file module.mk
#
# The topmost subsystem (module) make include file. Uses the MODULES
# list to determine what files to continue the make with (the
# submodules themselves).
MODULES := executables

include $(patsubst %,$(SOURCE)/%/module.mk,$(MODULES))
