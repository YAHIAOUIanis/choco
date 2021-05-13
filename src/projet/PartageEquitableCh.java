package projet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class PartageEquitableCh {

	public static void main(String[] args) {

		int n = 16;
		int nDiv2 = n / 2;
		int sumIntegers = n * (n + 1) / 4;
		int sumSquares = n * (n + 1) * (2 * n + 1) / 12;

		if (n > 8 && n % 4 == 0) { // Ce problème admet des solutions si N > 8 et si N est multiple de 4.
			Model model = new Model("Partage équitable");

			IntVar[] group1 = model.intVarArray("group1", nDiv2, 1, n);
			IntVar[] group2 = model.intVarArray("group2", nDiv2, 1, n);
			model.allDifferent(group1).post();
			model.allDifferent(group2).post();

			model.element(model.intVar(1), group1, model.intVar(1), 1).post(); // pour ne pas afficher les solutions
																				// symétriques
			for (int i = 0; i < nDiv2 - 1; i++) {
				model.arithm(group1[i], "<=", group1[i + 1]).post();
				model.arithm(group2[i], "<=", group2[i + 1]).post();
			}

			for (int i = 0; i < nDiv2; i++) {
				for (int j = 0; j < nDiv2; j++) {
					model.arithm(group1[i], "!=", group2[j]).post();
				}
			}

			model.sum(group1, "=", sumIntegers).post();
			model.sum(group2, "=", sumIntegers).post();

			IntVar[] pow2Group1 = buildArrayPow(group1, 2);
			IntVar[] pow2group2 = buildArrayPow(group2, 2);

			model.sum(pow2Group1, "=", sumSquares).post();
			model.sum(pow2group2, "=", sumSquares).post();

			Solver solver = model.getSolver();
			solver.showShortStatisticsOnShutdown();
			int nbSol = 0;
			while (solver.solve()) {
				System.out.println("Solution #" + (++nbSol));
				displayArray(group1, "G1");
				displayArray(group2, "G2");
				if (n > 30)
					break;
			}
			System.out.println("sum of integers = " + sumIntegers + "\nsum of squares = " + sumSquares);
		} else {
			System.out.println("N doit être > à 8 et multiple de 4");
		}
	}

	public static void displayArray(IntVar[] array, String nameArray) {
		System.out.print(nameArray + " = [");
		for (int i = 0; i < array.length - 1; i++) {
			System.out.print(array[i].getValue() + ", ");
		}
		System.out.println(array[array.length - 1].getValue() + "]");
	}

	public static IntVar[] buildArrayPow(IntVar[] array, int power) {
		IntVar[] powers = new IntVar[array.length];
		for (int i = 0; i < powers.length; i++) {
			powers[i] = array[i].pow(power).intVar();
		}
		return powers;
	}

}
