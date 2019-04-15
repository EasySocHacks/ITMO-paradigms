package expression;

public class Const extends NumericValue {
	int value;
	
	public Const(int value) {
		this.value = value;
	}

	protected int getValue(int x, int y, int z) {
		return value;
	}
	
	public int getConstValue() {
		return value;
	}

	protected String getValue() {
		return String.valueOf(value);
	}
}
