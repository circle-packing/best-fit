package solvers.bestfit;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.localization.Localizable;
import model.*;
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

	private List<Location> shell;


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
		//mounts = new PriorityQueue<>(circlesToPack.size(), mountComparer);
		shell = new ArrayList<>(circlesToPack.size());
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
		// Create the initial shell
		// IMPORTANT: must be clock-wise
		shell.add(new Location(firstPos, first));
		shell.add(new Location(thirdPos, third));
		shell.add(new Location(secondPos, second));
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
		else if (!shell.isEmpty()) {
			// Find biggest mount on the shell
			Location first = null;
			Location second = null;
			int firstIndex = 0;
			int secondIndex = 0;
			double biggest = 0;
			for(int i = 0; i < shell.size(); ++i) {
				int j = (i+1) % shell.size();
				Location f = shell.get(i);
				Location s = shell.get(j);
				double size = f.getPosition().distanceTo(s.getPosition());
				if (size > biggest) {
					biggest = size;
					first = f;
					second = s;
					firstIndex = i;
					secondIndex = j;
				}
			}
			// Choose circle to pack
			Circle cir = circlesToPack.get(0); //The biggest one, since circlesToPack is ordered

			// Calculate position for the circle
			Vector2 pos = SideMount.getMountPositionFor(cir, first, second);
			Location loc = new Location(pos, cir);

			// Check for overlap
			int prevIndex = Math.abs((firstIndex-1) % shell.size());
			Location prev = shell.get(prevIndex);
			if (loc.overlaps(prev)) {
				shell.remove(first);
				return true;
			}

			int nextIndex = (secondIndex+1) % shell.size();
			Location next = shell.get(nextIndex);
			if (loc.overlaps(next)) {
				shell.remove(second);
				return true;
			}

			// Everything went well, no overlaps and such
			circlesToPack.remove(cir);
			getSolution().add(cir, pos);

			// TODO add new hole
			// Extend shell
			shell.add(secondIndex, loc);
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
