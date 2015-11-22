package solvers;

import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import com.joptimizer.optimizers.OptimizationRequest;
import model.Circle;
import model.Point;
import model.Problem;
import model.Solution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Pablo on 22/11/15.
 */
public class SimpleJOptimizeSolver extends Solver {

	public SimpleJOptimizeSolver(Problem problem) {
		super(problem);
	}

	@Override
	public void solve() {
		solve(getProblem().getRandomSolution(25));

	}

	public void solve(Solution initial) {
		if (initial.getLocations().size() != getProblem().getCircles().size()) {
			throw new IllegalArgumentException("Initial solution doesn't contain the right amount of circles. " +
					"Given: " + initial.getLocations().size() + " Wanted: " + getProblem().getCircles().size());
		}

		OptimizationRequest or = new OptimizationRequest();

		//func: r
		PDQuadraticMultivariateRealFunction objectiveFunction = new PDQuadraticMultivariateRealFunction(null, null, 1);

		int n = getProblem().getCircles().size();
		double[] uT = new double[2 * n + 1];
		int i = 0;
		for (Map.Entry<Circle, Point> circle : initial.getLocations().entrySet()) {
			uT[i] = circle.getValue().getX();
			uT[n + i] = circle.getValue().getY();
			++i;
		}
		//uT[2*n] = r???;
	}


}
