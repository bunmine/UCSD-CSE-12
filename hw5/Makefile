DIR=$(HOME)/../public/hw5

.SUFFIXES: .java .class
.java.class:
	javac -g $<

all:	Driver1 Driver2 Calc

calc: Calc.class Main.class

Driver1:	Driver1.class

Driver2:	Driver2.class

clean: 
	rm -f *.o *.class Driver1 Driver2 Calc core Main


BaseStack.class: BaseStack.java List.java Size.java
MyLib.class: MyLib.java
Size.class: Size.java
Tracker.class: Tracker.java 
List.class: List.java

Driver1.class:	Base.java List.java \
	BaseStack.java MyLib.java Size.java Tracker.java
	javac -g Driver1.java
	echo 'java Driver1 $$*' > Driver1
	chmod ug+rx Driver1

Driver2.class:	Base.java List.java \
	BaseStack.java MyLib.java Tracker.java
	javac -g Driver2.java
	echo 'java Driver2 $$*' > Driver2
	chmod ug+rx Driver2

Calc:	MyLib.java Size.java BaseStack.java List.java \
	Tracker.java
	cp $(DIR)/java/calc_classes/*.class .
	#cp calc_classes/*.class .
	echo 'java Calc $$*' > Calc
	chmod ug+rx Calc

new:
	make clean
	make
