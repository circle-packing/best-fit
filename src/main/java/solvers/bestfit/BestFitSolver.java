package solvers.bestfit;

import com.sun.istack.internal.NotNull;
import model.Circle;
import model.Vector2;
import model.Problem;
import model.Solution;
import solvers.Solver;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Pablo on 22/11/15.
 */
public class BestFitSolver extends Solver {

	public BestFitSolver(@NotNull Problem problem) {
		super(problem);
	}

	@Override
	public void solve() {
		Problem problem = getProblem();
		List<Circle> circles = problem.getCircles();
		Solution solution = new Solution(circles.size());

		if (problem.getCircles().size() < 3) {
			throw new IllegalStateException("Too little circles in this problem, need at least 3.");
		}

		Collections.sort(circles, new Comparator<Circle>() {
			@Override
			public int compare(Circle c1, Circle c2) {
				return Double.compare(c2.getRadius(), c1.getRadius());
			}
		});

		//Initially place the two biggest circles next to eachother
		Circle first = circles.get(0);
		Circle second = circles.get(1);

		Vector2 firstPos = new Vector2(0, 0);
		Vector2 secondPos = new Vector2(first.getRadius() + second.getRadius(), 0);

		solution.add(first, firstPos);
		solution.add(second, secondPos);

		// Place the third biggest circle on top of the first two (assuming they are positioned clockwise)
		SideMount mountThird = new SideMount(firstPos, secondPos, first, second);
		Circle third = circles.get(2);
		Vector2 thirdPos = mountThird.getMountPositionFor(third);
		solution.add(third, thirdPos);



		// Set solution
		setSolution(solution);
		// Check if solution is valid
		if (!solution.isValid()) {
			System.out.println("Didn't find a valid solution. Some circles overlap.");
		}
		else if (solution.getLocations().size() != problem.getCircles().size()) {
			System.out.println("The solution is valid, but doesn't include all circles.");
		}
		else {
			System.out.println("Found valid solution.");
		}
	}
}
