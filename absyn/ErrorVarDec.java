package absyn;

public class ErrorVarDec extends VarDec {
  public ErrorVarDec( int pos ) {
    this.pos = pos;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
