DIR=$(HOME)/../public/hw7/java

.SUFFIXES: .java .class
.java.class:
	javac -g $<

all:	Driver Calc

Calc: Calculator.class

Calculator.class: Base.java Calculator.java MyLib.java Stack.java \
	SymTab.java Tracker.java
	javac -g Main.java
	echo 'java Main $$*' > Calc
	chmod ug+rx Calc

Driver:	Driver.class

Driver.class:	Base.java Driver.java MyLib.java SymTab.java Tree.java Tracker.java
	javac -g Driver.java
	echo 'java Driver $$*' > Driver
	chmod ug+rx Driver

Main.class:	Calculator.java Main.java
	javac -g Main.java

SymTab.class:	Base.java SymTab.java Tree.java
	javac -g SymTab.java

MyLib.class: MyLib.java

clean:
	rm -f *.class Driver Calc core
	# cp class_files/*.class .
	cp $(DIR)/class_files/*.class .

public:
	rm -f *.class Driver Calc core
	

new:
	make clean
	make

