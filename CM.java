/*
  Created by: Fei Song
  File Name: Main.java
  To Build:
  After the scanner, tiny.flex, and the parser, tiny.cup, have been created.
    javac Main.java

  To Run:
    java -classpath /usr/share/java/cup.jar:. Main gcd.tiny

  where gcd.tiny is an test input file for the tiny language.
*/

import java.io.*;
import absyn.*;

class CM {
  public static boolean SHOW_TREE = false;
  public static boolean SHOW_SYMBOL = false;
  public static boolean SHOW_ASSEM = false;
  static public void main(String argv[]) {
    /* Start the parser */
    if(argv[0].equals("-a")){
      SHOW_TREE = true;
      argv[0] = argv[1];
  }else if(argv[0].equals("-s")){
    SHOW_SYMBOL = true;
    argv[0] = argv[1];
  }else if(argv[0].equals("-c")){
    SHOW_ASSEM = true;
    argv[0] = argv[1];
  }
    try {
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Absyn result = (Absyn)(p.parse().value);
      if (SHOW_TREE) {
         System.out.println("The abstract syntax tree is:");
         ShowTreeVisitor visitor = new ShowTreeVisitor();
         result.accept(visitor, 0);
     }
     if (SHOW_SYMBOL) {
         SymbolTable table = new SymbolTable();
         result.accept(table, 0);
         table.printTable(0);
     }
       AssemGen assembly = new AssemGen(argv[0].substring(0, argv[0].length() - 2)+"tm", SHOW_ASSEM);
       assembly.startAssembly(result);
       

    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
