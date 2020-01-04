function: function.c
	gcc -o function -fno-stack-protector -z execstack -no-pie function.c

shenanigans: shenanigans.c
	gcc -o shenanigans -fno-stack-protector -z execstack -no-pie shenanigans.c

zip: clean
	zip -9 exam1 *.org *.c Makefile

clean:
	-rm function shenanigans exam1.zip 2> /dev/null || true
