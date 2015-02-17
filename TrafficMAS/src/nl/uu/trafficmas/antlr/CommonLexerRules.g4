lexer grammar CommonLexerRules;

ID	:	[a-zA-Z0-9]+;
INT	:	[0-9]+;
REAL:	INT ('.' INT);
NEWLINE:'\r'?'\n';
WS	:	[ \t]+ -> skip;