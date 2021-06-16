# This is the Makefile to use for Homework Assignment #6
# To use, at the prompt, type:
#
#       make Calc               # This will make executable Calc
# or
#       make Driver             # This will make executable Driver
# or
#       make clean              # This will safely remove old stuff

DIR=$(HOME)/../public/hw6/java

.SUFFIXES: .java .class
.java.class:
	javac -g $<

all:	Driver Calc 

Calc:	Tracker.java Size.java
	echo 'java Calculator $$*' > Calc
	chmod ug+rx Calc

Driver:	Driver.class
	echo 'java Driver $$*' > Driver
	chmod ug+rx Driver

Driver.class:	Base.java Driver.java HashTable.java MyLib.java SymTab.java \
				Tracker.java Size.java
	javac -g Driver.java

clean:
	rm -f *.class Driver Calc core
	# cp class_files/*.class .
	cp $(DIR)/class_files/*.class .

public:
	rm -f *.class Driver Calc core
	

new:
	make clean
	make

backup:
	cp *.java Makefile backup 
