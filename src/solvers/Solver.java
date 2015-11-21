package solvers;

import model.Problem;
import model.Solution;

/**
 * Created by Pablo on 21/11/15.
 */
public class Solver {

	protected final Problem problem;
	protected Solution solution;

	public Solver(Problem problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return problem;
	}

	public Solution getSolution() {
		return solution;
	}
}
