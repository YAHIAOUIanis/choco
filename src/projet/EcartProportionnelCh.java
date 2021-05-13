package projet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class EcartProportionnelCh {

	public static void main(String[] args) {

		int n = 4;
		int n2 = n * 2;

		Model model = new Model("Ecart Proportionnel");

		IntVar[] v = model.intVarArray("v", n2, 1, n);
		IntVar[] card = model.intVarArray("Occur", n, 2, 2);
		int[] values = new int[n];

		for (int i = 0; i < values.length; i++) {
			values[i] = i + 1;
		}

		model.globalCardinality(v, values, card, false).post();

		/*
		for (int i = 0; i < n2 - 1; i++) {
			for (int j = 1; j < n2; j++) {
				int k = j - i;
				model.distance(v[i], v[j], "=", k).post();
			}
		}
		*/
		for (int i = 0; i < n2 - 1; i++) {
			model.arithm(v[i], "!=", v[i + 1]).post();
		}

		Solver solver = model.getSolver();

		int nbSol = 0;
		// Utils.displayDomains(v);
		while (solver.solve()) {
			System.out.println("Solution #" + (++nbSol));
			displayArray(v, "v");

		}
		System.out.println("#Solution : " + nbSol);
	}

	public static void displayArray(IntVar[] array, String nameArray) {
		System.out.print(nameArray + " = [");
		for (int i = 0; i < array.length - 1; i++) {
			System.out.print(array[i].getValue() + ", ");
		}
		System.out.println(array[array.length - 1].getValue() + "]");
	}

}
