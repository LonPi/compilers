JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-classpath /usr/share/java/cup.jar:.
#CUP=$(JAVA) $(CLASSPATH) java_cup.CM <
CUP=cup

#
# TM Simulator
#

CC = gcc

CFLAGS = 



all: CM.class

run:
	java -classpath /usr/share/java/cup.jar:. CM testSymbolTbl.cm

runAST:
	java -classpath /usr/share/java/cup.jar:. CM -a testAssembly.cm

runSYM:
	java -classpath /usr/share/java/cup.jar:. CM -s testSymbolTbl.cm

runASSEM:
	java -classpath /usr/share/java/cup.jar:. CM -c testAssembly.cm

runScan:
	java -classpath /usr/share/java/cup.jar:. Scanner < 3.cm

CM.class: absyn/*.java parser.java sym.java Lexer.java ShowTreeVisitor.java Scanner.java SymbolTable.java CM.java AssemGen.java

%.class: %.java
	$(JAVAC) $(CLASSPATH)  $^

Lexer.java: tiny.flex
	$(JFLEX) tiny.flex

parser.java: tiny.cup
	#$(CUP) -dump -expect 3 tiny.cup
	$(CUP) -expect 3 tiny.cup

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
	rm -f tm

tm: tm.c
	$(CC) $(CFLAGS) tm.c -o tm

	