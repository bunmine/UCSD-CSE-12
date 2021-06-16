# This is the Makefile to use for Homework Assignment #10
# To use, at the prompt, type:
#
#       make		# This will make everything
# or
#       make clean	# This will safely remove old stuff
# or
#	make new	# This will remove all old stuff and
#			# will compile fresh
# or
#	make backup	# To capture a copy of all your .java files

.PHONY: backup clean install new public
.SUFFIXES: .java .class
.java.class:
	javac -g $<

DIR=$(HOME)/../public/hw10/java

# run: clean all
# 	clear
# 	./Calc 2>&1 | tee ../__outputs__/java.txt

all:	Driver

Driver:	Base.java Driver.java MyLib.java \
	Heap.java Tracker.java Size.java \
	Driver.java
	javac Driver.java
	echo 'java Driver $$*' > Driver
	chmod ug+rx Driver

clean:
	rm -f *.class Driver

new:
	make clean
	make Driver
