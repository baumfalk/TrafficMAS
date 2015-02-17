grammar LabeledLogicExpr;
import CommonLexerRules;

prog: valuation NEWLINE formula NEWLINE;
valuation:  prop (NEWLINE prop)*;
formula	:	prop								#singleProp
		|	op='-' formula						#neg
		|	formula op=('&'|'|'|'->') formula	#AndOrImpl
		| '(' formula ')'						#parens
		;	 
prop	: 	ID
		| 	ID '(' (ID|INT|REAL) (',' (ID|INT|REAL))* ')'		
		;

AND	: '&';
OR	: '|';
IMPL: '->';
NEG	: '-';