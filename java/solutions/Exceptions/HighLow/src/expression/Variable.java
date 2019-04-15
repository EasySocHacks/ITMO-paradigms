package expression;

public class Variable extends NumericValue {
	private String name;
	
	public Variable(String name) {
		this.name = name;
	}
	
	protected int getValue(int x, int y, int z) {
		if (name.equals("x")) {
			return x;
		} else if (name.equals("y")) {
			return y;
		} else {
			return z;
		}
	}

	protected String getValue() {
		return name;
	}
}
