package absyn;

public class SimpleDec extends VarDec {
  public SimpleDec( int pos, NameTy typ, String name) {
    this.pos = pos;
    this.typ = typ;
    this.name = name;
  }

  public String toString(){
  	return name+": " + typ.toString().toLowerCase();
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
