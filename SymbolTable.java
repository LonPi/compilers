import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import absyn.*;


class SymbolTable implements AbsynVisitor{
    protected	ArrayList<Hashtable> list = new ArrayList<>();
    final static int SPACES = 4;

    protected boolean callInt;
    protected boolean returnInt;

    public SymbolTable(){
        list.add(new Hashtable<String, Dec>());
        //int row, int col, NameTy result, String func, VarDecList params, CompoundExp body
        try{
            FunctionDec funcDec = new FunctionDec(0,0,null,"input", null, null);
            funcDec.offset = 4;
            inputToSymTbl(funcDec, 0);
            funcDec = new FunctionDec(0,0,null, "output", null, null);
            funcDec.offset = 7;
            inputToSymTbl(funcDec, 0);
        }catch(Exception e){
            //pass
        }
    }

    protected void inputToSymTbl(FunctionDec decleration, int level)throws Exception{
        //creates new scope if list does not match scope
        if(level >=  list.size()){
            list.add(new Hashtable<String, Dec>());
        }
        Hashtable curTable = list.get(level);
        //if key is already in raises error
        if(curTable.containsKey(decleration.func)){
            throw new Exception("ERROR: Function "+decleration.func+" already exists");
        }
        curTable.put(decleration.func, decleration);
    }

    protected void inputToSymTbl(VarDec decleration, int level)throws Exception{
        //creates new scope if list does not match scope
        if(level >=  list.size()){
            list.add(new Hashtable<String, Dec>());
        }
        Hashtable curTable = list.get(level);
        //if key is already in raises error
        if(curTable.containsKey(decleration.name)){
            throw new Exception("ERROR: Variable "+decleration.name+" already exists");
        }
        curTable.put(decleration.name, decleration);
    }


    protected void deleteTable(int level){
        if(level < list.size()){
            printTable(level);
            list.remove(level);
        }
    }

    public void printTable(int level){
        Hashtable curTable = list.get(level);
        Set<String> keys = curTable.keySet();
        for(String key: keys){
            indent(level + 1);
            System.out.println(curTable.get(key));
        }
    }

    public void indent( int level ) {
        for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
    }

    /* Type checking methods */
    /* Traversal for Exp class via OpExp */
    protected void typeCheck(Exp exp, int level){
        if (exp instanceof VarExp)
			typeCheck((VarExp) exp, level);
		else if (exp instanceof CallExp)
			typeCheck((CallExp) exp, level);
		else if (exp instanceof OpExp)
			typeCheck((OpExp) exp, level);
		else if (exp instanceof AssignExp)
			typeCheck((AssignExp) exp, level);
		else if (exp instanceof IntExp)
			typeCheck((IntExp) exp, level);
    }

    protected void typeCheck(VarExp exp, int level){
        if(returnInt){
            Hashtable curTable = list.get(level);
            //if key variable is not within same level - Raise error
            if(!curTable.containsKey(exp.variable.name)){
                indent(level);
                System.out.println("ERROR: Variable "+exp.variable.name+" does not exist");
            }else{
                VarDec var = (VarDec)curTable.get(exp.variable.name);
                if(var.typ.typ != 0){
                    indent(level);
                    System.out.println("ERROR: Variable "+exp.variable.name+" does not return int");
                }
            }
        }
    }

    protected void typeCheck(CallExp exp, int level){
        // Find funcation at global scope
        Hashtable curTable = list.get(0);

        //if key function is not within global scope - Raise error
        if(!curTable.containsKey(exp.name)){
            indent(level);
            System.out.println("ERROR: Function "+exp.name+" does not exist");
        }else if(callInt){
            FunctionDec func = (FunctionDec)curTable.get(exp.name);
            if(func.result.typ != 0){
                indent(level);
                System.out.println("ERROR: Function "+exp.name+" does not return int");
            }
        }
    }

    protected void typeCheck(OpExp exp, int level){
        typeCheck(exp.right, level);
        typeCheck(exp.left, level);
    }

    protected void typeCheck(AssignExp exp, int level){
        typeCheck(exp.lhs, level);
        typeCheck(exp.rhs, level);
    }

    protected void typeCheck(Var exp, int level){
		if(exp instanceof SimpleVar)
			typeCheck((SimpleVar)exp, level);
		else if (exp instanceof IndexVar)
			typeCheck((IndexVar) exp, level);
    }

    protected void typeCheck(IfExp exp, int level){
        typeCheck(exp.test, level);
        typeCheck(exp.thenpart, level);
        if(exp.elsepart != null)
            typeCheck(exp.elsepart, level);
    }

    protected void typeCheck(WhileExp exp, int level){
        typeCheck(exp.test, level);
        typeCheck(exp.body, level);
    }

    protected void typeCheck(ReturnExp exp, int level){
        // Check here on return type
        if(exp.exp == null & returnInt){
            indent(level);
            System.out.println("ERROR: Return value expected of type int");
        }
    }

    protected void typeCheck(IntExp exp, int level){
        // Valid
    }

    /* Visitor methods to traverse Abstract Syntax Tree */
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
        callInt = true;
        exp.lhs.accept( this, level );
        exp.rhs.accept( this, level );
    }

    public void visit( IfExp exp, int level ) {
        callInt = false;
        exp.test.accept( this, level );
        exp.thenpart.accept( this, level );
        if (exp.elsepart != null )
        exp.elsepart.accept( this, level );
    }

    public void visit( IntExp exp, int level ) {
    }

    public void visit( OpExp exp, int level ) {
        // callInt = true;
        exp.left.accept( this, level );
        exp.right.accept( this, level );
    }

    public void visit( ReturnExp exp, int level ) {
        callInt = false;
        typeCheck(exp, level);
        returnInt = false;

        if(exp.exp != null){
            exp.exp.accept(this, level);
        }
    }

    public void visit( IDExp exp, int level ) {
    }

    public void visit( SimpleVar exp, int level ) {
    }

    public void visit( IndexVar exp, int level ) {
        exp.index.accept(this, level);
    }

    public void visit( NameTy exp, int level ){

    }

    public void visit( SimpleDec exp, int level ){
        try{
            inputToSymTbl(exp, level);
        }catch (Exception e) {
            indent(level);
            System.out.println(e.toString());
        }
        exp.typ.accept(this, level);
    }

    public void visit( ArrayDec exp, int level ){
        try{
            inputToSymTbl(exp, level);
        }catch (Exception e) {
            indent(level);
            System.out.println(e.toString());

        }
        exp.typ.accept(this, level);

    }

    public void visit( CallExp exp, int level){
        typeCheck(exp, level);
        if(exp.args != null){
            exp.args.accept(this, level);
        }

    }

    public void visit( VarExp exp, int level ){
        //System.out.println("VarExp: ");
        exp.variable.accept(this, level);
    }

    public void visit( FunctionDec exp, int level){
        indent( level );
        System.out.println("Entering the scope for function "+exp.func+":");

        visit( exp.result, level );
        try{
            inputToSymTbl(exp, level);
        }catch (Exception e) {
            indent(level);
            System.out.println("FunctionDec Error: " + exp.func);
        }

        if(exp.result.typ == 0){
            returnInt = true;
        }else{
            returnInt = false;
        }

        level++;
        if(exp.params != null){
            exp.params.accept(this, level);
        }
        level--;

        exp.body.accept(this, level);
        indent(level);
        System.out.println("Leaving the function scope");
    }

    public void visit( CompoundExp exp, int level){
        callInt = false;
        level++;
        indent( level );
        System.out.println("Entering new block:");
        try{
            inputToSymTbl(new SimpleDec(0,new NameTy(0,0), "thisisahack.jpg"), level);
        }catch(Exception e){

        }
        if(exp.decs != null){
            exp.decs.accept(this, level);
        }
        if(exp.exps != null){
            exp.exps.accept(this, level);
        }
        deleteTable(level);
        indent(level);
        System.out.println("Leaving the block");
        level--;

    }

    public void visit( WhileExp exp, int level ) {
        callInt = false;
        exp.test.accept( this, level );
        if (exp.body != null) {
            exp.body.accept( this, level );
        }

    }

    public void visit( ErrorExp exp, int level ) {
        //System.out.println( "ErrorExp: line " + (exp.row+1) + " and column " + (exp.col+1));
    }

    public void visit( ErrorDec exp, int level ) {
        //System.out.println( "ErrorDec: line " + (exp.row+1) + " and column " + (exp.col+1));
    }

    public void visit( ErrorVarDec exp, int level ) {
        //System.out.println( "ErrorVarDec: line " + (exp.row+1) + " and column " + (exp.col+1));
    }

    public void visit( ErrorVar exp, int level ) {
        //System.out.println( "ErrorVar: line " + (exp.row+1) + " and column " + (exp.col+1));
    }

}
