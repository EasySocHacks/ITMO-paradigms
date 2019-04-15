package expression.parser;

import expression.generic.GenericTabulator;

public class Main {
	public static void main(String[] args) {System.out.println(Integer.MAX_VALUE + " | " + (int)((Math.floor(Math.sqrt(Integer.MAX_VALUE)) + 0)));
		GenericTabulator gt = new GenericTabulator();
		try {
			int x1 = -6;
			int x2 = -6;
			int y1 = -15;
			int y2 = -15;
			int z1 = -19;
			int z2 = -19;
			
			Object[][][] answer = gt.tabulate("i", "square " + Integer.MAX_VALUE + "*2 + 1", x1, x2, y1, y2, z1, z2);
			
			for (int i = 0; i < x2 - x1 + 1; i ++) {
				for (int j = 0; j < y2 - y1 + 1; j++) {
					for (int k = 0; k < z2 - z1 + 1; k++) {
						System.out.println((i + x1) + ":" + (j + y1) + ":" + (k + z1) + " = " + answer[i][j][k]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
