package projet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Sudoku25x25Ch {

	public static void main(String[] args) {
		Model model = new Model("Sodoku");
		int n = 5;
		int n2 = n * n;
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
			int i0 = r / n * n;
			int j0 = r % n * n;
			int k = 0;
			for (int i = i0; i < i0 + n; i++) {
				for (int j = j0; j < j0 + n; j++) {
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
		displaySudoku(n, v);
	}

	private static void displaySudoku(int n, IntVar[][] v) {
		int n2 = n * n;
		int width = String.valueOf(n2).length();
		System.out.println("--- SOL --- " + n2 + "x" + n2);
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < n; i++) {
			s.append("+");
			for (int j = 0; j < n * (width + 1) + 1; j++) {
				s.append("-");
			}
		}
		s.append("+");
		for (int i = 0; i < n2; i++) {
			if (i % n == 0) {
				System.out.println(" " + s);
			}
			for (int j = 0; j < n2; j++) {
				if (j % n == 0) {
					System.out.print(" |");
				}
				System.out.printf(" %" + width + "d", v[i][j].getValue());
			}
			System.out.println(" |");
		}
		System.out.println(" " + s);
	}
}