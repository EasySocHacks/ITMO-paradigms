package expression;

public abstract class NumericValue implements TripleExpression {	
	abstract protected int getValue(int x, int y, int z);
	abstract protected String getValue(); 
	
	public int evaluate(int x, int y, int z) {
		return getValue(x, y, z);
	}
	
	public String toString() {
		return getValue();
	}
}
