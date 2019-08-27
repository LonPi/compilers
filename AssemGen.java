import java.io.*;
import absyn.*;
import java.util.ArrayList;
import java.util.Hashtable;



class AssemGen extends SymbolTable{
	/*
	REG# purpose
	0 = address storage 1
	1 = address storage 2
	2 = operatation register one(opReg1)
	3 = operatation register two(opReg2)
	4 = answer storage (ex. answer = opReg1 + opReg2)
	5 = frame pointer
	6 = global pointer
	7 = program counter
	*/
	public final static int adrStr1 = 0;
	public final static int adrStr2 = 1;
	public final static int opReg1 = 2;
	public final static int opReg2  = 3;
	public final static int ansReg = 4;
	public final static int fp = 5;
	public final static int gp = 6;
	public final static int pc = 7;


	int offF = 0; //offsetFrame
	int offG = 0; //offsetGlobal
	int offI = 0; //offsetInstruction
	PrintWriter  code  = null;
	FileOutputStream file = null;
	PrintStream original = System.out;
	PrintStream destroy = new PrintStream(new OutputStream() {public void write(int b) {/*DO NOTHING*/}});

	public static int emitLoc = 0;
	public static int highEmitLoc = 0;
	private boolean isParam = false;
	public AssemGen(String fileName, boolean printIt){
		super();
		if(!printIt){
			original = destroy;
		}
	    System.setOut(destroy);

		try{
			code = new PrintWriter(fileName,"UTF-8");
		}catch (Exception e) {
	        System.out.println("Invalide File Error: " + fileName);
	    }

	}

	public void startAssembly(Absyn result){
	    int storage = 0;

		emitComment("C-Minus Compilation to TM Code");
		emitComment("File: fac.tm");
		emitComment("Standard prelude:");
		emitRM("LD", gp, 0 , 0, "load gp with maxaddress");
		emitRM("LDA", fp, 0 , gp, "copy gp to fp");
		emitRM("ST", adrStr1, 0, 0, "clear location 0");
		emitComment("Jump around i/o routines here");
		//backtracking code here
		storage = emitLoc;
		emitSkip(1);
		emitComment("code for input routine");

		emitRM("ST", adrStr1, -1, fp, "store return");
		emitRO("IN", ansReg, 0, 0, "input");
		emitRM("LD", pc, -1, fp,"return to caller");
		emitComment("code for output routine");
		emitRM("ST", adrStr1, -1, fp, "store return");
		emitRM("LD", adrStr1, -2, fp,"load output value");
		emitRO("OUT", 0, 0, 0, "output");
		emitRM("LD", pc, -1, fp,"return to caller");
		//backtracking code here
		emitBackup(storage);
		emitRM("LDA", pc, 7 , pc, "jump around i/o code");
		emitRestore();

		result.accept(this, 0);

		emitRM("ST", fp, offG,fp, "frame control");
		emitRM("LDA", fp, offG,fp, "push new frame");
		emitRM("LDA", adrStr1, 1,pc, "save return");
		emitRM("LDA", pc, ((emitLoc - getOffset("main", 0))*-1) - 1 ,pc, "jump to main entry");

		emitRM( "LD", fp, 0, fp, "pop frame" );
		emitRO( "HALT", 0, 0, 0, "" );
	}


	/*
	HALT
	IN reg[r]
	OUT	reg[r]
	r = s operation t
	ADD
	reg[r] = reg[s] + reg[t]
	SUB
	reg[r] = reg[s] -reg[t]
	MUL
	reg[r] = reg[s] * reg[t]
	DIV
	reg[r] = reg[s] / reg[t]       (may generate ZERO_D
	*/
	/*
	LD
	reg[r] = dMem[a]
	LDA
	reg[r] = a
	LDC
	reg[r] = d
	ST
	reg1 = reg2
	dMem[a] = reg[r]
	JLT
	if( reg[r] < 0 ) reg[PC_REG] = a
	JLE
	if( reg[r] <= 0 ) reg[PC_REG] = a
	JGT
	if( reg[r] > 0 ) reg[PC_REG] =
a	JGE
	if( reg[r] >= 0 ) reg[PC_REG] = a
	JEQ
	if( reg[r] == 0 ) reg[PC_REG] = a
	JNE
	if( reg[r] != 0 ) reg[PC_REG] =
	*/
	/*
	REG# purpose
	0 = address storage 1
	1 = address storage 2
	2 = operatation register one(opReg1)
	3 = operatation register two(opReg2)
	4 = answer storage (ex. answer = opReg1 + opReg2)
	5 = frame pointer
	6 = global pointer
	7 = program counter
	*/


	//go through all hash tables
	public int getOffset(String key, int level){
		Dec decleration = null;
		while(level >= 0){

			Hashtable curTable = list.get(level);
			try{
				decleration = (Dec)curTable.get(key);
			}catch (Exception e){
				System.out.println(e);

			}
			if(decleration != null){
				break;
			}
			level = level - 1;

		}

		//System.out.println("this is what we are trying to find: "+key +  decleration.offset);
		return decleration.offset;
	}

	public int getFrameOffest(String key){
		Hashtable curTable = list.get(0);
		FunctionDec decleration = null;
		try{
			decleration = (FunctionDec)curTable.get(key);
		}catch (Exception e){
			System.out.println(e);

		}
		return decleration.frameOffset;
	}

	public boolean isGlobal(String key, int level){
		Dec decleration = null;
		while(level >= 0){

			Hashtable curTable = list.get(level);
			try{
				decleration = (Dec)curTable.get(key);
			}catch (Exception e){
				System.out.println(e);

			}
			if(decleration != null){
				break;
			}
			level = level - 1;

		}

		//System.out.println("this is what we are trying to find: "+key +  decleration.offset);
		return level == 0;
	}

	public boolean isTypeArray(String key, int level){
		ArrayDec decleration = null;
		while(level >= 0){

			Hashtable curTable = list.get(level);
			try{
				decleration = (ArrayDec)curTable.get(key);
				decleration.getSizeInt();
				System.out.println(key + " is an array");
				return true;
			}catch (Exception e){
			}
			if(decleration != null){
				break;
			}
			level = level - 1;

		}

		//System.out.println("this is what we are trying to find: "+key +  decleration.offset);
		return false;
	}

	public boolean isTypeParam(String key, int level){
		Dec decleration = null;
		while(level >= 0){

			Hashtable curTable = list.get(level);
			try{

				decleration = (Dec)curTable.get(key);
			}catch (Exception e){
				System.out.println(e);

			}
			if(decleration != null){
				break;
			}
			level = level - 1;

		}

		//System.out.println("this is what we are trying to find: "+key +  decleration.offset);
		return decleration.isParam;
	}


	public void setDecOffset( Dec exp, int level){
        if(level > 0){
	        exp.offset = offF;
	        offF = offF - 1;
        }else{
        	exp.offset = offG;
        	offG = offG - 1;
        }
	}

	public void setDecOffset( ArrayDec exp, int level){
		if(isParam){
			//pass by reference
			setDecOffset((Dec) exp, level);
		}else{
	        if(level > 0){
		        offF = offF - exp.getSizeInt() + 1;
		        exp.offset = offF;
		        offF = offF - 1;
	        }else{
	        	offG = offG - exp.getSizeInt() + 1;
	        	exp.offset = offG;
	        	offG = offG - 1;
	        }
		}
	}

	public void setDecOffset( FunctionDec exp, int level){
        exp.offset = emitLoc;
	}



	/*
	<code to compute first arg>
	ST ac, frameoffset+initFO (fp)
	<code to compute second arg>
	ST ac, frameoffset+initFO-1(fp)
	ST fp, frameoffset+ofpFO (fp)	* store current fp
	LDA fp, frameoffset (fp)	* push new frame
	LDA ac, 1 (pc)	* save return in ac
	LDA pc, ... (pc)	* relative jump to function entry
	LD fp, ofpFO (fp
	*/
    public void visit( CallExp exp, int level){
        if(exp.args != null){
            ExpList expList = exp.args;
        	int paramCount = 0;
        	isParam = true;
            while( expList != null ) {
                expList.head.accept( this, level );
                emitRM("ST", ansReg, -2 + offF + paramCount,fp, "passing param");
                expList = expList.tail;
                paramCount = paramCount - 1;

            }
            isParam = false;
        }
        emitRM("ST", fp, offF,fp, "frame control");
		emitRM("LDA", fp, offF,fp, "push new frame");
		emitRM("LDA", adrStr1, 1,pc, "save return");

		emitRM("LDA", pc, ((emitLoc - getOffset(exp.name, 0))*-1) - 1,pc, "jump to function: " + exp.name);
	    emitRM("LD", fp, 0, fp, "pop frame" );
    }

    public void visit( ReturnExp exp, int level ) {
        if(exp.exp != null){
          exp.exp.accept(this, level);
        }
    }

	public void visit( ExpList expList, int level ) {
	  while( expList != null ) {
	    expList.head.accept( this, level );
	    expList = expList.tail;
	  }
	}

	public void createFrame(FunctionDec exp, int level){
		emitRM("ST", adrStr1, -1,fp, "save return");
	}


    /*
    ST ac, frameoffset+initFO (fp)
    <code to compute second arg
    */

    public void visit( OpExp exp, int level ) {
        exp.left.accept( this, level );
        emitRM("LDA", opReg1,0,ansReg, "Exp for left op");
        exp.right.accept( this, level );
        emitRM("LDA", opReg2,0,ansReg, "Exp for right op");

       	switch( exp.op ) {
       	  case OpExp.PLUS:
       	    emitRO("ADD",ansReg, opReg1, opReg2, "" );
       	    break;
       	  case OpExp.MINUS:
       	    emitRO("SUB",ansReg, opReg1, opReg2, "" );
       	    break;
       	  case OpExp.TIMES:
       	    emitRO("MUL",ansReg, opReg1, opReg2, "" );
       	    break;
       	  case OpExp.OVER:
       	    emitRO("DIV",ansReg, opReg1, opReg2, "" );
       	    break;
       	  case OpExp.EQ:
       	  case OpExp.LT:
       	  case OpExp.GT:
       	  case OpExp.LTEQ:
       	  case OpExp.GTEQ:
       	  case OpExp.NEQ:
            emitRO("SUB",ansReg, opReg1, opReg2, "" );
          default:
       	    System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
       	}
    }

    public void visit( IndexVar exp, int level ) {
		exp.index.accept(this, level);
    	if(isTypeParam(exp.name, level)){
    		emitRM("LD", adrStr1, getOffset(exp.name, level),fp, "array var start " + exp.name);
    	}else if(isGlobal(exp.name, level)){
    		emitRM("LDA", adrStr1, getOffset(exp.name, level),gp, "array var start " + exp.name);
    	}else{
			emitRM("LDA", adrStr1, getOffset(exp.name, level),fp, "array var start " + exp.name);
    	}
		emitRO("ADD", ansReg, ansReg, adrStr1, "calculating index");
		emitRM("LD", ansReg, 0,ansReg, "retrieving var from array " + exp.name);


    }

    public void storeInVar(SimpleVar exp, int level){
    	//hack because for some reason an array without a index value is a simple var
    	if(isGlobal(exp.name, level)){
	    	emitRM("ST", ansReg, getOffset(exp.name, level), gp, "assigning "+ exp.name);
    	}else{
    		emitRM("ST", ansReg, getOffset(exp.name, level), fp, "assigning "+ exp.name);
    	}
    }

    public void storeInVar(IndexVar exp, int level){
    	exp.index.accept(this, level);
    	if(isGlobal(exp.name, level)){
	    	emitRM("LDA", adrStr1, getOffset(exp.name, level),gp, "array var start " + exp.name);
    	}else{
    		emitRM("LDA", adrStr1, getOffset(exp.name, level),fp, "array var start " + exp.name);
    	}
    	emitRO("ADD", ansReg, ansReg, adrStr1, "calculating index");
    	emitRM("ST", adrStr2, 0,ansReg, "storing to array " + exp.name);
    }

    public void visit( AssignExp exp, int level ) {
        callInt = true;
        exp.rhs.accept( this, level );
        //because we could do a bunch of calculations we store the answer temporarily in an unused register
        //this is why we tried to use as little registers as possible
        emitRM("LDA",adrStr2 , 0 ,ansReg, "");
        try{
	        storeInVar((IndexVar)exp.lhs,level);
        }catch(Exception e){
        	storeInVar((SimpleVar)exp.lhs,level);
        }

    }

    public void visit( SimpleVar exp, int level ) {
    	if(isTypeArray(exp.name, level)){
    		if(isGlobal(exp.name, level)){
	    		emitRM("LDA", ansReg, getOffset(exp.name, level),gp, "return array address " + exp.name);
    		}else{
    			emitRM("LDA", ansReg, getOffset(exp.name, level),fp, "return array address " + exp.name);
    		}
    	}
    	else if(isGlobal(exp.name,level)){
	    	emitRM("LD", ansReg, getOffset(exp.name, level),gp, "simple var " + exp.name);
    	}else{
    		emitRM("LD", ansReg, getOffset(exp.name, level),fp, "simple var " + exp.name);
    	}

    }

    public void visit( IntExp exp, int level ) {
    	emitRM("LDC", ansReg, Integer.parseInt(exp.value),0,"int exp");
    }



	public void visit( FunctionDec exp, int level){

		//backtracking code here
		int storage = emitLoc;
		int jumpLocation = storage;
		emitSkip(1);
		//resetFrameOffset
		offF = -2;
		setDecOffset(exp, level);
	    createFrame(exp, level);

	    visit( exp.result, level );
	    try{
	        inputToSymTbl(exp, level);
	    }catch (Exception e) {
	        indent(level);
	        //System.out.println("FunctionDec Error: " + exp.func);
	    }

	    if(exp.result.typ == 0){
	        returnInt = true;
	    }else{
	        returnInt = false;
	    }

	    level++;
	    if(exp.params != null){
	    	isParam = true;
	        exp.params.accept(this, level);
	        isParam = false;
	    }
	    level--;

	    exp.body.accept(this, level);
	    exp.frameOffset = offF;
	    //backtracking code here
	    emitRM("LD", pc, -1, fp,"return to caller");
	    jumpLocation = emitLoc - storage - 1;
	    emitBackup(storage);
	    emitRM("LDA", pc, jumpLocation , pc, "jump around function: " + exp.func);
	    emitRestore();


	}

	public void setDecParam(Dec exp){
		exp.isParam = isParam;
	}



	public void visit( SimpleDec exp, int level ){

        setDecOffset(exp, level);

        setDecParam(exp);

        try{
            inputToSymTbl(exp, level);
        }catch (Exception e) {
            indent(level);
            System.out.println(e.toString());
        }
        exp.typ.accept(this, level);

    }

    public void visit( ArrayDec exp, int level ){
        setDecOffset(exp, level);
        setDecParam(exp);
        try{
            inputToSymTbl(exp, level);
        }catch (Exception e) {
            indent(level);
            System.out.println(e.toString());
        }

        exp.typ.accept(this, level);
    }

	public void visit( IfExp exp, int level ){
        emitComment("-> if");
        //type cast object
        OpExp test = (OpExp)exp.test;

        test.accept( this, level );

        //save place where we will put jump at
        int storage = emitSkip(1);
        int jumpLocation = 0;

        exp.thenpart.accept( this, level );

        //calculate jump back omit negative one if else exist because we want to include one extra instruction for the jump
        jumpLocation = emitLoc - storage;

        if(exp.elsepart == null){
        	//include negative one if else does not exist
        	jumpLocation = jumpLocation - 1;
        }
        //jump back and print instruction
        emitBackup(storage);
        switch(test.op){
			case OpExp.EQ:
            	emitRM("JNE", ansReg, jumpLocation, pc, "" );
			    break;
        	case OpExp.LT:
            	emitRM("JGE", ansReg, jumpLocation, pc, "" );
	        	break;
        	case OpExp.GT:
            	emitRM("JLE", ansReg, jumpLocation, pc, "" );
	        	break;
        	case OpExp.LTEQ:
            	emitRM("JGT", ansReg, jumpLocation, pc, "" );
			    break;
    	    case OpExp.GTEQ:
            	emitRM("JLE", ansReg, jumpLocation, pc, "" );
	    	    break;
        	case OpExp.NEQ:
	            emitRM("JEQ", ansReg, jumpLocation, pc, "" );
        	    break;
        }
        emitRestore();

        if (exp.elsepart != null ){
	        storage = emitSkip(1);
            exp.elsepart.accept( this, level );
            jumpLocation = emitLoc - storage -1;
            emitBackup(storage);
            //includes unconditional jump to move around else part
		    emitRM("LDA", pc, jumpLocation, pc, "unconditional jmp" );
		    emitRestore();

        }


        emitComment("<- if");
    }

    public void visit( WhileExp exp, int level ){
        emitComment("-> while");
        //type cast object
        OpExp test = (OpExp)exp.test;
        int storage3 = emitLoc;
        test.accept( this, level );

        //save place where we will put jump at
        int storage = emitSkip(1);
        int storage2 = emitLoc;
        int jumpLocation = 0;

        //process the body
        exp.body.accept( this, level );

        //retrieve the offset
        jumpLocation = emitLoc - storage;

        emitRM("LDA", pc, -(emitLoc - storage3), pc, "unconditional jmp" );
        //jump back and print instruction
        emitBackup(storage);
        switch(test.op){
            case OpExp.EQ:
                emitRM("JNE", ansReg, jumpLocation, pc, "" );
                break;
            case OpExp.LT:
                emitRM("JGE", ansReg, jumpLocation, pc, "" );
                break;
            case OpExp.GT:
                emitRM("JLE", ansReg, jumpLocation, pc, "" );
                break;
            case OpExp.LTEQ:
                emitRM("JGT", ansReg, jumpLocation, pc, "" );
                break;
            case OpExp.GTEQ:
                emitRM("JLE", ansReg, jumpLocation, pc, "" );
                break;
            case OpExp.NEQ:
                emitRM("JEQ", ansReg, jumpLocation, pc, "" );
                break;
        }
        emitRestore();

        emitComment("<- while");
    }

      /* Code Emiting Routines */
      public int emitSkip(int distance){
        int i = emitLoc;
        emitLoc += distance;
        if(highEmitLoc < emitLoc){
          highEmitLoc = emitLoc;
        }
        return i;
      }

      public void emitBackup(int loc){
        if(loc > highEmitLoc){
          emitComment("Bug in emmitBackup");
        }
        emitLoc = loc;
      }

      public void emitRestore(){
        emitLoc = highEmitLoc;
      }

      public void emitRM_Abs(String op, int r, int a, String comment){
        String code = emitLoc + ":  " + op + "  " + r + "," + (a-(emitLoc+1)) + "(" + pc + ")";
        emitOutput(code);
        ++emitLoc;
        emitOutputln("\t" + comment);
        if(highEmitLoc < emitLoc){
          highEmitLoc = emitLoc;
        }
      }

      public void emitRM(String op, int r, int offset, int r1, String comment){
        String code = emitLoc + ":  " + op + "  " + r + "," + offset + "(" + r1 + ")";
        emitOutput(code);
        ++emitLoc;
        emitOutputln("\t" + comment);
        if(highEmitLoc < emitLoc){
          highEmitLoc = emitLoc;
        }
      }

      public void emitRO(String op, int destination, int r, int r1, String comment){
        String code = emitLoc + ":  " + op + " " + destination + "," + r + "," + r1;
        emitOutput(code);
        ++emitLoc;
        emitOutputln("\t" + comment);
      }

      public void emitComment(String comment){
        comment = "* " + comment;
        emitOutputln(comment);
      }

      public void emitOutput(String outputLine){
	    System.setOut(original);
          try{
              System.out.print(outputLine);
              code.print(outputLine);
              code.flush();
          }catch(Exception e) {
              System.out.println(e.toString());
          }
      	System.setOut(destroy);
      }

      public void emitOutputln(String outputLine){
	    System.setOut(original);
          try{
              System.out.println(outputLine);
              code.println(outputLine);
              code.flush();
          }catch(Exception e) {
              System.out.println(e.toString());
          }
      	System.setOut(destroy);
      }
}
