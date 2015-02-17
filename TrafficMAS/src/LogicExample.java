import java.io.FileInputStream;
import java.io.InputStream;

import nl.uu.trafficmas.antlr.LabeledLogicExprLexer;
import nl.uu.trafficmas.antlr.LabeledLogicExprParser;
import nl.uu.trafficmas.antlr.LogicEvalVisitor;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class LogicExample {
	public static void main(String [] args) throws Exception {
		String inputFile = null;
        if ( args.length>0 ) inputFile = args[0];
        InputStream is = System.in;
        if ( inputFile!=null ) is = new FileInputStream(inputFile);
        ANTLRInputStream input = new ANTLRInputStream(is);
        LabeledLogicExprLexer lexer = new LabeledLogicExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LabeledLogicExprParser parser = new LabeledLogicExprParser(tokens);
        ParseTree tree = parser.prog(); // parse

        LogicEvalVisitor eval = new LogicEvalVisitor();
        System.out.println(eval.visit(tree));
	}
}
