package projet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Sudoku6x6Zone3x2Ch {

	public static void main(String[] args) {
		Model model = new Model("Sodoku");
		int height = 3;
		int width = 2;
		int n2 = height * width;

		if (width > height) {
			System.err.println("Attention la hauteur est inférieure à la largeur");
			return;
		}
		// *** DECLARE THE MAIN MATRIX ***
		// Use IntVar[][] t = model.intVarMatrix(#lines, #columns, lower bound, upper
		// bound);
		IntVar[][] v = model.intVarMatrix(n2, n2, 1, n2);

		// *** SET CONSTRAINTS ON LINES ***
		for (int i = 0; i < n2; i++) {
			IntVar[] l = new IntVar[n2];
			for (int j = 0; j < n2; j++) {
				l[j] = v[i][j];
			}
			model.allDifferent(l).post();
		}
		// *** SET CONSTRAINTS ON COLUMNS ***
		for (int j = 0; j < n2; j++) {
			IntVar[] c = new IntVar[n2];
			for (int i = 0; i < n2; i++) {
				c[i] = v[i][j];
			}
			model.allDifferent(c).post();
		}
		// *** SET CONSTRAINTS ON REGIONS ***
		// NB: regions can be numbered (r) from 0 to n2 (excluded)
		// region r: first coord i0 = r / n * n and j0 = r % n * n
		// for a region r you can do 2 nested loops on i and j
		// i from i0 to i0+n (excluded) and j from j0 to j0+n (excluded)
		// *** SOLVE ***
		for (int r = 0; r < n2; r++) {
			IntVar[] rg = new IntVar[n2];
			int i0 = r / height * width;
			int j0 = r % height * width;
			int k = 0;
			for (int i = i0; i < i0 + height; i++) {
				for (int j = j0; j < j0 + width; j++) {
					rg[k++] = v[i][j];
				}
			}
			model.allDifferent(rg).post();
		}
		Solver solver = model.getSolver();
		solver.showShortStatisticsOnShutdown();
		if (!solver.solve()) {
			System.err.println("Pas de solution - grille insolvable");
			return;
		}
		displaySudoku(height, width, v);
	}

	private static void displaySudoku(int height, int width, IntVar[][] v) {
		int n2 = height * width;
		int lengthN2 = String.valueOf(n2).length();
		System.out.println("--- SOL --- " + n2 + "x" + n2 + " ----- Zone : height x width = " + height + "x" + width);
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < height; i++) {
			s.append("+");
			for (int j = 0; j < width * (lengthN2 + 1) + 1; j++) {
				s.append("-");
			}
		}
		s.append("+");
		for (int i = 0; i < n2; i++) {
			if (i % height == 0) {
				System.out.println(" " + s);
			}
			for (int j = 0; j < n2; j++) {
				if (j % width == 0) {
					System.out.print(" |");
				}
				System.out.printf(" %" + lengthN2 + "d", v[i][j].getValue());
			}
			System.out.println(" |");
		}
		System.out.println(" " + s);
	}
}