package projet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class PartageEquitableChExtSumCubes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int n = 16;
		int nDiv2 = n / 2;
		int sumIntegers = n * (n + 1) / 4;
		int sumSquares = n * (n + 1) * (2 * n + 1) / 12;
		int sumCubes = sumIntegers * sumIntegers * 2;

		if (n >= 16 && n % 8 == 0) {
			Model model = new Model("Partage Equitable");

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

			IntVar[] pow3Group1 = buildArrayPow(group1, 3);
			IntVar[] pow3group2 = buildArrayPow(group2, 3);

			model.sum(pow3Group1, "=", sumCubes).post();
			model.sum(pow3group2, "=", sumCubes).post();

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

			System.out.println("sum of integers = " + sumIntegers + "\nsum of squares = " + sumSquares
					+ "\nsum of cuves = " + sumCubes);
		} else
			System.out.println("N doit être supérieur à 16 et un multiple de 8.");
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
