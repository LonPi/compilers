package absyn;

public class FunctionDec extends Dec {
  public NameTy result;
  public String func;
  public VarDecList params;
  public CompoundExp body;
  public int frameOffset = 0;

  public FunctionDec( int row, int col, NameTy result, String func, VarDecList params, CompoundExp body) {
    this.row = row;
    this.col = col;
    this.result = result;
    this.func = func;
    this.params = params;
    this.body = body;
  }

  public String toString(){
    return func + ": (" + paramString() + ") -> " + result.toString().toLowerCase();
  }

  private String paramString(){
      String result = "";
      while(this.params.head != null){
          if(this.params.head instanceof SimpleDec){
              SimpleDec toprint = (SimpleDec)this.params.head;
              if(toprint.typ.typ == 0){
                  result += "int";
              }
          }

          if(this.params.head instanceof ArrayDec){
              ArrayDec toprint = (ArrayDec)this.params.head;
              if(toprint.typ.typ == 0){
                  result += "int";
              }
          }
          if(this.params.tail == null){
              break;
          }
          this.params = (VarDecList)this.params.tail;
          result += ", ";

      }
      return result;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
