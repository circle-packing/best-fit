package solvers.bestfit;

import com.sun.istack.internal.NotNull;
import model.Circle;
import model.Vector2;
import model.Problem;
import model.Solution;
import solvers.Solver;

import java.util.*;

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

		// TODO
		// Create initial holes, fill biggest holes first
		Comparator<Hole> holeComparer = new Comparator<Hole>() {
			@Override
			public int compare(Hole o1, Hole o2) {
				return Double.compare(o2.getSize(), o1.getSize());
			}
		};
		PriorityQueue<Hole> holes = new PriorityQueue<>(circles.size(), holeComparer);
		holes.add(new Hole(firstPos, secondPos, thirdPos, first, second, third));
		// Create initial mountpoints, place in biggest mountpoints first
		Comparator<SideMount> mountComparer = new Comparator<SideMount>() {
			@Override
			public int compare(SideMount o1, SideMount o2) {
				return Double.compare(o2.getSize(), o1.getSize());
			}
		};
		PriorityQueue<SideMount> mounts = new PriorityQueue<>(circles.size(), mountComparer);
		// Do best-fit
		List<Circle> circlesToPlace = new ArrayList<>(circles);
		while(!circlesToPlace.isEmpty()) {
			if(!holes.isEmpty()) {
				Hole toFill = holes.remove(); //removes the hole, so remembers we already handled it
				double size = toFill.getSize();
				// IMPORTANT: circlesToPlace must be ordered big to small!
				Circle bestFit = biggestNotLargerThan(size, circlesToPlace);

				if (bestFit == null) { //none fit
					continue; //just go on with the algo, this hole can't be filled
				}

				// place the best fit
				Vector2 center = toFill.getCenter();
				solution.add(bestFit, center);
				//remember as already been placed
				circlesToPlace.remove(bestFit);

				//create new holes
				holes.addAll(toFill.getHolesWhenFilledWith(bestFit, center, size));
			}
			else if (!mounts.isEmpty()) {

			}
			else {
				System.out.println("Something went wrong, there are circles, but nowhere to place them.");
				break;
			}
		}


		// Set solution
		setSolution(solution);
		// Check if solution is valid
		System.out.println("Error/Total overlap: " + solution.calculateError());
		System.out.println("Packed " + solution.getLocations().size() + " of " + problem.getCircles().size() + " circles.");
	}

	private Circle biggestNotLargerThan(double size, List<Circle> sortedBigToSmall) {
		for (Circle cir : sortedBigToSmall) {
			if (cir.getRadius() <= size) {
				return cir;
			}
		}
		return null;
	}
}
