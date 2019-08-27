* C-Minus Compilation to TM Code
* File: fac.tm
* Standard prelude:
0:  LD  6,0(0)	load gp with maxaddress
1:  LDA  5,0(6)	copy gp to fp
2:  ST  0,0(0)	clear location 0
* Jump around i/o routines here
* code for input routine
4:  ST  0,-1(5)	store return
5:  IN 4,0,0	input
6:  LD  7,-1(5)	return to caller
* code for output routine
7:  ST  0,-1(5)	store return
8:  LD  0,-2(5)	load output value
9:  OUT 0,0,0	output
10:  LD  7,-1(5)	return to caller
3:  LDA  7,7(7)	jump around i/o code
12:  ST  0,-1(5)	save return
13:  LDC  4,5(0)	int exp
14:  LD  0,-3(5)	array var start x
15:  ADD 4,4,0	calculating index
16:  LD  4,0(4)	retrieving var from array x
17:  LDA  2,0(4)	Exp for left op
18:  LDC  4,9(0)	int exp
19:  LDA  0,-10(6)	array var start z
20:  ADD 4,4,0	calculating index
21:  LD  4,0(4)	retrieving var from array z
22:  LDA  3,0(4)	Exp for right op
23:  ADD 4,2,3	
24:  LDA  1,0(4)	
25:  ST  4,-4(5)	assigning n
26:  LD  4,-4(5)	simple var n
27:  LD  7,-1(5)	return to caller
11:  LDA  7,16(7)	jump around function: testFunc
29:  ST  0,-1(5)	save return
30:  LDC  4,11(0)	int exp
31:  LDA  1,0(4)	
32:  ST  4,-2(5)	assigning x
33:  LDC  4,2(0)	int exp
34:  LDA  2,0(4)	Exp for left op
35:  LDC  4,2(0)	int exp
36:  LDA  3,0(4)	Exp for right op
37:  MUL 4,2,3	
38:  LDA  2,0(4)	Exp for left op
39:  LDC  4,2(0)	int exp
40:  LDA  2,0(4)	Exp for left op
41:  LDC  4,1(0)	int exp
42:  LDA  3,0(4)	Exp for right op
43:  MUL 4,2,3	
44:  LDA  3,0(4)	Exp for right op
45:  ADD 4,2,3	
46:  ST  4,-6(5)	passing param
47:  ST  5,-4(5)	frame control
48:  LDA  5,-4(5)	push new frame
49:  LDA  0,1(7)	save return
50:  LDA  7,-44(7)	jump to function: output
51:  LD  5,0(5)	pop frame
52:  LD  7,-1(5)	return to caller
28:  LDA  7,24(7)	jump around function: main
53:  ST  5,-11(5)	frame control
54:  LDA  5,-11(5)	push new frame
55:  LDA  0,1(7)	save return
56:  LDA  7,-28(7)	jump to main entry
57:  LD  5,0(5)	pop frame
58:  HALT 0,0,0	
