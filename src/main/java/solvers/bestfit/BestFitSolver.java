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

	private List<Circle> circlesToPack; //The remaining circles to pack, for all see the Problem

	static private Comparator<Hole> holeComparer = new Comparator<Hole>() {
		@Override
		public int compare(Hole o1, Hole o2) {
			return Double.compare(o2.getSize(), o1.getSize());
		}
	};
	private PriorityQueue<Hole> holes;

	static private Comparator<SideMount> mountComparer = new Comparator<SideMount>() {
		@Override
		public int compare(SideMount o1, SideMount o2) {
			return Double.compare(o2.getSize(), o1.getSize());
		}
	};
	private PriorityQueue<SideMount> mounts;


	public BestFitSolver(@NotNull Problem problem) {
		super(problem);
	}

	private void init() {
		// Problem
		Problem problem = getProblem();
		circlesToPack = new ArrayList<>(problem.getCircles());

		if (circlesToPack.size() < 3) {
			throw new IllegalStateException("Too little circles in this problem, need at least 3.");
		}

		Collections.sort(circlesToPack, new Comparator<Circle>() {
			@Override
			public int compare(Circle c1, Circle c2) {
				return Double.compare(c2.getRadius(), c1.getRadius());
			}
		});

		// Solution
		setSolution(new Solution(circlesToPack.size()));

		// Collections
		holes = new PriorityQueue<>(circlesToPack.size(), holeComparer);
		mounts = new PriorityQueue<>(circlesToPack.size(), mountComparer);
	}

	private void packFirstThree() {
		//Initially place the two biggest circles next to eachother
		Circle first = circlesToPack.get(0);
		Circle second = circlesToPack.get(1);

		Vector2 firstPos = new Vector2(0, 0);
		Vector2 secondPos = new Vector2(first.getRadius() + second.getRadius(), 0);

		getSolution().add(first, firstPos);
		getSolution().add(second, secondPos);

		// Place the third biggest circle on top of the first two (assuming they are positioned clockwise)
		SideMount mountThird = new SideMount(firstPos, secondPos, first, second);
		Circle third = circlesToPack.get(2);
		Vector2 thirdPos = mountThird.getMountPositionFor(third);
		getSolution().add(third, thirdPos);

		circlesToPack.remove(first);
		circlesToPack.remove(second);
		circlesToPack.remove(third);

		// Create first hole
		holes.add(new Hole(firstPos, secondPos, thirdPos, first, second, third));
		// Create first mount points
		// TODO
	}

	private void doBestFit() {
		// Do best-fit
		while(!circlesToPack.isEmpty()) {
			boolean ok = bestFitStep();
			if (!ok) break;
		}
	}

	/**
	 * Do a single step in the best-fit algorithm.
	 * A step can be several things:
	 * 1) Fitting a circle into a hole, this removes a hole, creates 3 new holes, and removes a circle from circlesToPack.
	 * 2) Trying to fill a hole, but no circle is found. This removes that hole.
	 * 3) Placing a circle in a mount-point. This removes a mount, creates some new ones and removes a circle from circlesToPack.
	 *
	 * If no next step is possible something went wrong (incomplete or buggy implementation).
	 * 
	 * @return Whether a step was possible.
	 */
	private boolean bestFitStep() {
		if(!holes.isEmpty()) {
			Hole toFill = holes.remove(); //removes the hole, so remembers we already handled it
			double size = toFill.getSize();
			// IMPORTANT: circlesToPlace must be ordered big to small!
			Circle bestFit = biggestNotLargerThan(size, circlesToPack);

			if (bestFit == null) { //none fit
				return true; //just go on with the algo, this hole can't be filled
			}

			// place the best fit
			Vector2 center = toFill.getCenter();
			getSolution().add(bestFit, center);
			//remember as already been placed
			circlesToPack.remove(bestFit);

			//create new holes
			holes.addAll(toFill.getHolesWhenFilledWith(bestFit, center, size));
		}
		else if (!mounts.isEmpty()) {

		}
		else {
			System.out.println("Something went wrong, there are circles, but nowhere to place them.");
			return false;
		}

		return true;
	}

	@Override
	public void solve() {
		init();

		packFirstThree();

		doBestFit();

		// Print some information
		System.out.println("Error/Total overlap: " + getSolution().calculateError());
		System.out.println("Packed " + getSolution().getLocations().size() + " of " + getProblem().getCircles().size() + " circles.");
		System.out.println("" + circlesToPack.size() + " still need to be packed.");
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
