import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

  final static int SPACES = 4;

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
  }

  public void visit( ExpList expList, int level ) {
    while( expList != null ) {
      expList.head.accept( this, level );
      expList = expList.tail;
    }
  }

  public void visit( DecList decList, int level ) {
    while( decList != null ) {
      decList.head.accept( this, level );
      decList = decList.tail;
    }
  }


  public void visit( AssignExp exp, int level ) {
    indent( level );
    System.out.println( "AssignExp: " );
    level++;
    exp.lhs.accept( this, level );
    exp.rhs.accept( this, level );
  }

  public void visit( IfExp exp, int level ) {
    indent( level );
    System.out.println( "IfExp: " );
    level++;
    exp.test.accept( this, level );
    exp.thenpart.accept( this, level );
    if (exp.elsepart != null )
       exp.elsepart.accept( this, level );
  }

  public void visit( IntExp exp, int level ) {
    indent( level );
    System.out.println( "IntExp: " + exp.value );
  }

  public void visit( OpExp exp, int level ) {
    indent( level );
    System.out.print( "OpExp: " );
    switch( exp.op ) {
      case OpExp.PLUS:
        System.out.println( " + " );
        break;
      case OpExp.MINUS:
        System.out.println( " - " );
        break;
      case OpExp.TIMES:
        System.out.println( " * " );
        break;
      case OpExp.OVER:
        System.out.println( " / " );
        break;
      case OpExp.EQ:
        System.out.println( " == " );
        break;
      case OpExp.LT:
        System.out.println( " < " );
        break;
      case OpExp.GT:
        System.out.println( " > " );
        break;
      case OpExp.LTEQ:
        System.out.println( " <= " );
        break;
      case OpExp.GTEQ:
        System.out.println( " >= " );
        break;
      case OpExp.NEQ:
        System.out.println( " != " );
        break;
      default:
        System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
    }
    level++;
    exp.left.accept( this, level );
    exp.right.accept( this, level );
  }

  public void visit( ReturnExp exp, int level ) {
      indent( level );
      System.out.println( "ReturnExp: ");
      level++;
      if(exp.exp != null){
        exp.exp.accept(this, level);
      }
  }

  public void visit( IDExp exp, int level ) {
    indent( level );
    System.out.println( "IDExp: " + exp.name );
  }

  public void visit( SimpleVar exp, int level ) {
    indent( level );
    System.out.println( "SimpleVar: " + exp.name );
  }

  public void visit( IndexVar exp, int level ) {
    indent( level );
    System.out.println( "IndexVar: " + exp.name );
    level++;
    exp.index.accept(this, level);
  }

  public void visit( NameTy exp, int level ){
    //indent(level);
    //System.out.print("NameTy: ");
    switch( exp.typ ) {
      case NameTy.VOID:
        System.out.println( " - VOID" );
        break;
      case NameTy.INT:
        System.out.println( " - INT" );
        break;
      default:
        System.out.println("Unrecognized");
    }
  }

  public void visit( SimpleDec exp, int level ){
    indent(level);
    System.out.print("SimpleDec: " + exp.name);
    level++;
    exp.typ.accept(this, level);
  }

  public void visit( ArrayDec exp, int level ){
    indent(level);
    System.out.print("ArrayDec: " + exp.name + "["+exp.size.value + "]");
    level++;
    exp.typ.accept(this, level);
  }

  public void visit( CallExp exp, int level){
   indent(level);
    System.out.println("CallExp: " + exp.name);
    level++;
    if(exp.args != null){
      exp.args.accept(this, level);
    }

  }

  public void visit( VarExp exp, int level ){
    indent(level);
    System.out.println("VarExp: ");
    level++;
    exp.variable.accept(this, level);
  }

  public void visit( FunctionDec exp, int level){
   indent(level);
    System.out.print("FunctionDec: " + exp.func);
    visit( exp.result, level );
    level++;
    if(exp.params != null){
      exp.params.accept(this, level);
    }
    exp.body.accept(this, level);
  }

  public void visit( CompoundExp exp, int level){
    indent(level);
    System.out.println("CompoundExp: ");
    level++;
    if(exp.decs != null){
      exp.decs.accept(this, level);
    }
    if(exp.exps != null){
      exp.exps.accept(this, level);
    }
  }

  public void visit( WhileExp exp, int level ) {
    indent( level );
    System.out.println( "WhileExp: " );
    level++;
    exp.test.accept( this, level );
    if (exp.body != null) {
      exp.body.accept( this, level );
    }
  }

  public void visit( ErrorExp exp, int level ) {
    indent( level );
    System.out.println( "ErrorExp: line " + (exp.row+1) + " and column " + (exp.col+1));
  }

  public void visit( ErrorDec exp, int level ) {
    indent( level );
    System.out.println( "ErrorDec: line " + (exp.row+1) + " and column " + (exp.col+1));
  }

  public void visit( ErrorVarDec exp, int level ) {
    indent( level );
    System.out.println( "ErrorVarDec: line " + (exp.row+1) + " and column " + (exp.col+1));
  }

  public void visit( ErrorVar exp, int level ) {
    indent( level );
    System.out.println( "ErrorVar: line " + (exp.row+1) + " and column " + (exp.col+1));
  }
}
