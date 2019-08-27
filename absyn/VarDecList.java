package absyn;

public class VarDecList extends DecList {


  public VarDecList( Dec head, DecList tail ) {
  	super(head, tail);
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
