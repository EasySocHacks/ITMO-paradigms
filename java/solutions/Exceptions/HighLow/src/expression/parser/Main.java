package expression.parser;

import expression.TripleExpression;
import expression.exceptions.IllegalExpressionException;
import expression.exceptions.ParserException;

public class Main {
	public static void main(String[] args) {
		ExpressionParser expp = new ExpressionParser();
		try {
			TripleExpression exp = expp.parse("max1");
			
			System.out.println(exp.evaluate(1, 1, 1));
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		
	}
}
