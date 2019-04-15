package expression.generic;

import expression.parser.ExpressionParser;

public class GenericTabulator implements Tabulator{
	
	
	public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {		
		return solve(getInstance(mode), expression, x1, x2, y1, y2, z1, z2);
	}
	
	private <T>Object[][][] solve(Number<T> instance, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
		Object answer[][][] = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
		
		for (int i = 0; i < x2 - x1 + 1; i++) {
			for (int j = 0; j < y2 - y1 + 1; j++) {
				for (int k = 0; k < z2 - z1 + 1; k++) {
					try {
						ExpressionParser<T> parser = new ExpressionParser<>(instance);
						answer[i][j][k] = parser.parse(expression).evaluate(instance.cast(i + x1), instance.cast(j + y1), instance.cast(k + z1)).getValue();
					} catch(Exception e) {
						answer[i][j][k] = null;
					}
				}
			}
		}
		
		return answer;
	}
	
	private Number<?> getInstance(String mode) {
		switch(mode) {
			case "i": return new IntegerNumber();
			case "bi": return new BigIntegerNumber();
			case "d": return new DoubleNumber();
			case "u": return new NoOverflowIntegerNumber();
			case "f": return new NoOverflowFloatNumber();
			case "b": return new NoOverflowByteNumber(); 
		}
		
		return null;
	}
}
