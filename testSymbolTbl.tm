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
13:  LD  7,-1(5)	return to caller
11:  LDA  7,2(7)	jump around function: s
15:  ST  0,-1(5)	save return
16:  LD  7,-1(5)	return to caller
14:  LDA  7,2(7)	jump around function: input
18:  ST  0,-1(5)	save return
19:  LDC  4,2(0)	int exp
20:  LDA  1,0(4)	
21:  ST  4,-5(5)	assigning fac
22:  ST  5,-8(5)	frame control
23:  LDA  5,-8(5)	push new frame
24:  LDA  0,1(7)	save return
25:  LDA  7,-22(7)	jump to function: input
26:  LD  5,0(5)	pop frame
27:  LDA  1,0(4)	
28:  ST  4,-6(5)	assigning z
29:  ST  5,-8(5)	frame control
30:  LDA  5,-8(5)	push new frame
31:  LDA  0,1(7)	save return
32:  LDA  7,-29(7)	jump to function: input
33:  LD  5,0(5)	pop frame
34:  LDA  1,0(4)	
35:  ST  4,-2(5)	assigning x
36:  LDC  4,1(0)	int exp
37:  LDA  1,0(4)	
38:  ST  4,-5(5)	assigning fac
39:  ST  5,-10(5)	frame control
40:  LDA  5,-10(5)	push new frame
41:  LDA  0,1(7)	save return
42:  LDA  7,-39(7)	jump to function: input
43:  LD  5,0(5)	pop frame
44:  LD  4,-5(5)	simple var fac
45:  LDA  2,0(4)	Exp for left op
46:  LDA  4,-2(5)	return array address x
47:  LDA  3,0(4)	Exp for right op
48:  MUL 4,2,3	
49:  LDA  1,0(4)	
50:  ST  4,-5(5)	assigning fac
51:  LDA  4,-2(5)	return array address x
52:  LDA  2,0(4)	Exp for left op
53:  LDC  4,1(0)	int exp
54:  LDA  3,0(4)	Exp for right op
55:  SUB 4,2,3	
56:  LDA  1,0(4)	
57:  ST  4,-2(5)	assigning x
58:  LD  4,-5(5)	simple var fac
59:  ST  4,-12(5)	passing param
60:  ST  5,-10(5)	frame control
61:  LDA  5,-10(5)	push new frame
62:  LDA  0,1(7)	save return
63:  LDA  7,-57(7)	jump to function: output
64:  LD  5,0(5)	pop frame
65:  LD  4,-5(5)	simple var fac
66:  LDA  2,0(4)	Exp for left op
67:  LDC  4,1(0)	int exp
68:  LDA  3,0(4)	Exp for right op
69:  ADD 4,2,3	
70:  LD  7,-1(5)	return to caller
17:  LDA  7,53(7)	jump around function: main
71:  ST  5,-1(5)	frame control
72:  LDA  5,-1(5)	push new frame
73:  LDA  0,1(7)	save return
74:  LDA  7,-57(7)	jump to main entry
75:  LD  5,0(5)	pop frame
76:  HALT 0,0,0	
