package absyn;

public class ArrayDec extends VarDec {

  public IntExp size;


  public ArrayDec( int pos, NameTy typ, String name, IntExp size) {
    this.pos = pos;
    this.typ = typ;
    this.name = name;
    this.size = size;
  }

  public int getSizeInt(){
    System.out.println(size.value);
    if(size.value.length() > 0){
      return Integer.parseInt(size.value);
    }
    return 0;
  }

  public String toString(){
    return name+ "["+size.toString()+"]: " + typ.toString().toLowerCase();
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
