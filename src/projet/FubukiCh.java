package projet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class FubukiCh {

	public static void main(String[] args) {
		Model model = new Model("Fubuki");
		int n = 3;
		int n2 = n * n;
		int[] lineSum = { 23, 15, 7 };
		int[] colSum = { 14, 16, 15 };

		// *** DECLARE THE MAIN MATRIX ***
		// Use IntVar[][] t = model.intVarMatrix(#lines, #columns, lower bound, upper
		// bound);

		IntVar[][] v = model.intVarMatrix("v", n, n, 1, 23);
		IntVar[] all = new IntVar[n2];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				all[i * n + j] = v[i][j];
			}
		}
		model.allDifferent(all).post();

		// *** SET CONSTRAINTS ON LINES ***
		int k = 0;
		for (int i = 0; i < n; i++) {
			IntVar l[] = new IntVar[n];
			for (int j = 0; j < n; j++) {
				l[j] = v[i][j];
			}
			model.sum(l, "=", lineSum[k++]).post();
		}

		// *** SET CONSTRAINTS ON COLUMNS ***
		k = 0;
		for (int j = 0; j < n; j++) {
			IntVar c[] = new IntVar[n];
			for (int i = 0; i < n; i++) {
				c[i] = v[i][j];
			}
			model.sum(c, "=", colSum[k++]).post();
		}

		Solver solver = model.getSolver();
		solver.showShortStatisticsOnShutdown();
		if (!solver.solve()) {
			System.err.println("Pas de solution - grille insolvable");
			return;
		}

		System.out.println(displayFubuki(n, v, lineSum, colSum));

	}

	private static String displayFubuki(int n, IntVar[][] v, int[] lineSum, int[] colSum) {
		String s = "";
		int width = String.valueOf(n).length();
		int indexLineSum = 0;
		int indexColSum = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n - 1; j++) {
				s += " " + String.format("%" + width + "d", v[i][j].getValue()) + " +";
			}

			s += " " + String.format("%" + width + "d", v[i][n - 1].getValue()) + " = " + lineSum[indexLineSum++];
			s += "\n";

			if (i < n - 1) {
				for (int j = 0; j < n; j++) {
					s += " +  ";
				}
			} else {
				for (int j = 0; j < n; j++) {
					s += " =  ";
				}
				s += "\n";
				for (int j = 0; j < n; j++) {
					s += " " + colSum[indexColSum++] + " ";
				}
			}

			s += "\n";
		}

		return s;
	}

}