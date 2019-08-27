package absyn;

public class CompoundExp extends Exp {
  public VarDecList decs;
  public ExpList exps;

  public CompoundExp( int row, int col, VarDecList decs, ExpList exps ) {
    this.row = row;
    this.col = col;
    this.exps = exps;
    this.decs = decs;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}

