package nl.uu.trafficmas.antlr;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogicEvalVisitor extends LabeledLogicExprBaseVisitor<Boolean> {

	Set<String> valuation = new HashSet<String>();

	@Override
	public Boolean visitSingleProp(LabeledLogicExprParser.SinglePropContext ctx) {
		// TODO Auto-generated method stub
		return super.visitSingleProp(ctx);
	}

	@Override
	public Boolean visitProg(LabeledLogicExprParser.ProgContext ctx) {
		visit(ctx.valuation());
		return visit(ctx.formula());
	}

	@Override
	public Boolean visitValuation(LabeledLogicExprParser.ValuationContext ctx) {
		List<LabeledLogicExprParser.PropContext> props = ctx.prop();
		for(LabeledLogicExprParser.PropContext prop : props) {
			valuation.add(prop.getText());
		}
		return false;
	}

	@Override
	public Boolean visitNeg(LabeledLogicExprParser.NegContext ctx) {
		return !(visit(ctx.formula()));
	}

	@Override
	public Boolean visitProp(LabeledLogicExprParser.PropContext ctx) {
		return valuation.contains(ctx.getText());
	}

	@Override
	public Boolean visitParens(LabeledLogicExprParser.ParensContext ctx) {
		return visit(ctx.formula());
	}

	@Override
	public Boolean visitAndOrImpl(LabeledLogicExprParser.AndOrImplContext ctx) {
		boolean left = visit(ctx.formula().get(0));
		boolean right = visit(ctx.formula().get(1));
		boolean value = false;
		switch(ctx.op.getType()) {
		case LabeledLogicExprParser.AND:
			value = left && right;
			break;
		case LabeledLogicExprParser.OR:
			value = left || right;
			break;
		case LabeledLogicExprParser.IMPL:
			value = (!left) || right;
			break;
		}
		
		return value;
	}
	
}
