package solvers;

import model.Problem;
import model.Solution;

/**
 * Created by Pablo on 21/11/15.
 */
public abstract class Solver {

	private final Problem problem;
	private Solution solution;

	public Solver(Problem problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return problem;
	}

	abstract public void solve();

	protected void setSolution(Solution solution) {
		this.solution = solution;
	}

	public Solution getSolution() {
		return solution;
	}
}
