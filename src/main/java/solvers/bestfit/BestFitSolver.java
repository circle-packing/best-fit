package solvers.bestfit;

import model.*;
import solvers.Solver;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Pablo on 22/11/15.
 */
public class BestFitSolver extends Solver {

	private List<Circle> circlesToPack; //The remaining circles to pack, for all see the Problem

	private Queue<NHole> holes;

	private List<Location> shell;

	private Location enclosingCircle = null;

	static final Logger LOG = LoggerFactory.getLogger("default");

	public BestFitSolver(Problem problem) {
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
		holes = new LinkedBlockingQueue<>(/*circlesToPack.size()*/);
		shell = new ArrayList<>(circlesToPack.size());
	}

	private void packFirstThree() {
		//Initially place the two biggest circles next to eachother
		Circle first = circlesToPack.get(0);
		Circle second = circlesToPack.get(1);

		Vector2 firstPos = new Vector2(0, 0);
		Vector2 secondPos = new Vector2(first.getRadius() + second.getRadius(), 0);

		Location firstLoc = new Location(firstPos, first);
		Location secondLoc = new Location(secondPos, second);

		getSolution().add(firstLoc);
		getSolution().add(secondLoc);

		// Place the third biggest circle on top of the first two (assuming they are positioned clockwise)
		Circle third = circlesToPack.get(2);
		Vector2 thirdPos = Helpers.getMountPositionFor(third, firstLoc, secondLoc);
		Location thirdLoc = new Location(thirdPos, third);
		getSolution().add(thirdLoc);

		circlesToPack.remove(first);
		circlesToPack.remove(second);
		circlesToPack.remove(third);

		// Create first hole
		holes.add(new NHole(firstLoc, secondLoc, thirdLoc));
		// Create the initial shell
		// IMPORTANT: must be clock-wise
		shell.add(firstLoc);
		shell.add(thirdLoc);
		shell.add(secondLoc);

		enclosingCircle = Location.calculateEnclosingCircle(Arrays.asList(firstLoc, secondLoc, thirdLoc));
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
		if (circlesToPack.isEmpty()) {
			LOG.trace("No more circles to place.");
			return false;
		}

		if(!holes.isEmpty()) {
			NHole toFill = holes.remove(); //removes the hole, so remembers we already handled it
			// IMPORTANT: circlesToPlace must be ordered big to small!
			Location bestFit = findBestFitFor(toFill, circlesToPack);

			if (bestFit == null) { //none fit
				LOG.trace("Tried to fill a hole, but no circle is small enough.");
				return true; //just go on with the algo, this hole can't be filled
			}

			// place the best fit
			getSolution().add(bestFit);

			LOG.trace("Packing in hole: " + bestFit);

			//remember as already been placed
			circlesToPack.remove(bestFit.getCircle());

			//create new holes
			for (int i = 0; i < toFill.getLocations().size(); ++i) {
				int j = (i+1)%toFill.getLocations().size();
				holes.add(new NHole(bestFit, toFill.getLocations().get(i), toFill.getLocations().get(j)));
			}
		}
		else if (!shell.isEmpty()) {
			// Find closest mount on the shell to 0,0 //DONE Closest to round-circle center
			Location first = null;
			Location second = null;
			int firstIndex = 0;
			int secondIndex = 0;
			double minDist = Double.MAX_VALUE;
			for(int i = 0; i < shell.size(); ++i) {
				int j = (i+1) % shell.size();
				Location f = shell.get(i);
				Location s = shell.get(j);

				Vector2 dir = s.getPosition().minus(f.getPosition());
				dir.normalize();
				double distDiff = f.getPosition().distanceTo(s.getPosition()) - f.getCircle().getRadius() - s.getCircle().getRadius();
				// center between the two circles, if they touch it's the touching point, otherwise somewhere halfway to the other circle
				Vector2 pos = f.getPosition().plus(dir.times(f.getCircle().getRadius() + distDiff/2.0));

				//double dist = pos.lengthSquared(); //f.getPosition().plus(s.getPosition()).times(0.5).lengthSquared();
				double dist = enclosingCircle.getPosition().distanceTo(pos);

				if (dist < minDist) {
					minDist = dist;
					first = f;
					second = s;
					firstIndex = i;
					secondIndex = j;
				}
			}

			int prevIndex = (firstIndex+shell.size()-1) % shell.size();
			Location prev = shell.get(prevIndex);

			int nextIndex = (secondIndex+1) % shell.size();
			Location next = shell.get(nextIndex);

			// Choose circle to pack
			BestFitResult res = findBestFitFor(prev, first, second, next, circlesToPack);

			if (res.success) { //loc contains position and circle to pack
				Location loc = res.loc;

				LOG.trace("Packing on shell: " + loc);

				// Everything went well, no overlaps and such
				circlesToPack.remove(loc.getCircle());
				getSolution().add(loc);

				// Add new hole
				holes.add(new NHole(first, second, loc));
				// Extend shell
				shell.add(secondIndex, loc);

				// Check counter-clockwiseness
				Vector2 relFirstPos = first.getPosition().minus(enclosingCircle.getPosition());
				Vector2 relSecondPos = second.getPosition().minus(enclosingCircle.getPosition());
				Vector2 relLocPos = loc.getPosition().minus(enclosingCircle.getPosition());
				double angleFirst = Vector2.singedAngle(relFirstPos, relLocPos);
				double angleSecond = Vector2.singedAngle(relLocPos, relSecondPos);
				if (angleFirst < 0) {
					LOG.warn("Angle First = " + angleFirst + ": " + relFirstPos + "->" + relLocPos);
					shell.remove(first);
				}
				if (angleSecond < 0) {
					LOG.warn("Angle Second = " + angleSecond + ": " + relLocPos + "->" + relSecondPos);
					shell.remove(second);
				}
			}
			else { //no success, now loc contains circle to remove
				//Nothing fits, and never will
				LOG.trace("Tried packing on shell, but nothing small enough. Updating shell...");
				// Shrink the shell, depending on next/prev collision.
				shell.remove(res.loc);
			}

			// recalc enclosing circle if needed
			if (!enclosingCircle.contains(res.loc)) {
				enclosingCircle = Location.calculateEnclosingCircle(shell);
			}
		}
		else {
			LOG.warn("Something went wrong, there are circles, but nowhere to place them.");
			return false;
		}

		return true;
	}

	@Override
	public void solve() {
		init();

		packFirstThree();

		doBestFit();
	}

	public void report() {
		LOG.info(
			  "\t\nOverlap: " + getSolution().calculateOverlap()
			+ "\t\nNaN: " + getSolution().countNaN()
			+ "\t\nPacked " + getSolution().getLocations().size() + " of " + getProblem().getCircles().size() + " circles."
			+ "\t\n" + circlesToPack.size() + " still need to be packed."
			+ "\t\nEnclosing circle size is " + enclosingCircle.getCircle().getRadius() + ".");
	}

	public void startStepSolve() {
		init();

		packFirstThree();

		//bestFitStep();
	}

	public boolean doStepSolve() {
		return bestFitStep();
	}

	private Location findBestFitFor(NHole hole, List<Circle> sortedBigToSmall) {
		for (Circle cir : sortedBigToSmall) {
			Vector2 pos = hole.tryFit(cir);
			if (pos != null) {
				return new Location(pos, cir);
			}
		}
		return null;
	}

	/**
	 * Returnes a result.
	 * If success == true then loc is the bestfitting circle and it's pos.
	 * If success == false then loc contains the circle (first/second) to be removed.
	 */
	private BestFitResult findBestFitFor(Location prev, Location first, Location second, Location next, List<Circle> sortedBigToSmall) {
		Location toRemove = null;

		for (Circle cir : sortedBigToSmall) {
			Vector2 pos = Helpers.getMountPositionFor(cir, first, second);

			Location loc = new Location(pos, cir);
			if (loc.overlaps(prev)) {
				toRemove = first;
			}
			else if (loc.overlaps(next)) {
				toRemove = second;
			}
			else {
				return new BestFitResult(true, loc);
			}
		}
		return new BestFitResult(false, toRemove);
	}

	private BestFitResult findBestFitFor(Circle cir, Location prev, Location first, Location second, Location next) {
		Vector2 pos = Helpers.getMountPositionFor(cir, first, second);
		Location toRemove = null;

		Location loc = new Location(pos, cir);
		if (loc.overlaps(prev)) {
			toRemove = first;
		}
		else if (loc.overlaps(next)) {
			toRemove = second;
		}
		else {
			return new BestFitResult(true, loc);
		}
		return new BestFitResult(false, toRemove);
	}

	class BestFitResult {
		public boolean success;
		public Location loc;

		public BestFitResult(boolean success, Location loc) {
			this.success = success;
			this.loc = loc;
		}
	}

	public void drawState(Graphics2D g2) {

		// Draw circles
		g2.setColor(new Color(0,0, 255, 100));
		for (Location location : getSolution().getLocations()) {
			double r = location.getCircle().getRadius();
			Vector2 p = location.getPosition();
			double x = p.getX();
			x = x - r;
			double y = p.getY();
			y = y - r;

			Ellipse2D.Double circle = new Ellipse2D.Double(x, y, r*2.0, r*2.0);
			g2.fill(circle);
		}

		// Draw shell
		g2.setColor(new Color(255, 206, 0, 255));
		g2.setStroke(new BasicStroke((float)(1.5 / g2.getTransform().getScaleX())));
		{
			GeneralPath shellLine = new GeneralPath(GeneralPath.WIND_EVEN_ODD, shell.size());
			Location last = shell.get(shell.size() - 1);
			shellLine.moveTo(last.getPosition().getX(), last.getPosition().getY());
			for (Location loc : shell) {
				double x = loc.getPosition().getX();
				double y = loc.getPosition().getY();
				if (Double.isNaN(x)) x = 0;
				if (Double.isNaN(y)) y = 0;
				shellLine.lineTo(x, y);
			}
			g2.draw(shellLine);
		}

		// Draw shell place points
		g2.setColor(new Color(0, 255, 51, 150));
		for(int i = 0; i < shell.size(); ++i) {
			int j = (i+1) % shell.size();
			Location f = shell.get(i);
			Location s = shell.get(j);

			Vector2 dir = s.getPosition().minus(f.getPosition());
			dir.normalize();
			double distDiff = f.getPosition().distanceTo(s.getPosition()) - f.getCircle().getRadius() - s.getCircle().getRadius();
			// center between the two circles, if they touch it's the touching point, otherwise somewhere halfway to the other circle
			Vector2 p = f.getPosition().plus(dir.times(f.getCircle().getRadius() + distDiff/2.0));

			double r = (float)(2.0 / g2.getTransform().getScaleX());
			double x = p.getX();
			x = x - r;
			double y = p.getY();
			y = y - r;

			Ellipse2D.Double circle = new Ellipse2D.Double(x, y, r*2.0, r*2.0);
			g2.fill(circle);
		}

		// Draw holes
		g2.setColor(new Color(255,0,0, 80));
		g2.setStroke(new BasicStroke((float)(1.5 / g2.getTransform().getScaleX())));
		for (NHole hole : holes) {
			List<Location> locs = hole.getLocations();

			GeneralPath holeLine = new GeneralPath(GeneralPath.WIND_EVEN_ODD, locs.size());
			Location last = locs.get(locs.size()-1);
			Vector2 lastPos = last.getPosition();
			holeLine.moveTo(lastPos.getX(), lastPos.getY());

			for (Location loc : locs) {
				Vector2 pos = loc.getPosition();
				double x = pos.getX();
				double y = pos.getY();
				if (Double.isNaN(x)) x = 0;
				if (Double.isNaN(y)) y = 0;
				holeLine.lineTo(x, y);
			}



			Vector2 center = new Vector2(0,0);
			for (Location loc : locs) {
				center = center.plus(loc.getPosition());
			}
			center = center.times(1.0/locs.size());

			AffineTransform tr = new AffineTransform();
			double scale = 0.8;
			tr.translate(-center.getX() * scale, -center.getY() * scale);
			tr.scale(scale, scale);
			tr.translate(center.getX() / scale, center.getY() / scale);
			holeLine.transform(tr);

			g2.draw(holeLine);
		}

		//Draw enclosing circle
		g2.setColor(new Color(0, 208, 9, 255));
		g2.setStroke(new BasicStroke((float)(2.0 / g2.getTransform().getScaleX())));
		{
			double r = enclosingCircle.getCircle().getRadius();
			Vector2 p = enclosingCircle.getPosition();
			double x = p.getX();
			x = x - r;
			double y = p.getY();
			y = y - r;

			Ellipse2D.Double circle = new Ellipse2D.Double(x, y, r*2.0, r*2.0);
			g2.draw(circle);
		}
	}
}
