package solvers.bestfit;

import model.Circle;
import model.Location;
import model.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Pablo on 22/11/15.
 */
public abstract class Helpers {

	static boolean doClockwise = true;

	static public Vector2 getMountPositionFor(Circle cir, Location first, Location second) {
		doClockwise = !doClockwise;
		Vector2 result;
		if (doClockwise) {
			result = getMountPositionForClockwise(cir, first, second);
		}
		else {
			result = getMountPositionForCounterClockwise(cir, second, first);
		}

		return result;

		/*Vector2 c = getMountPositionForClockwise(cir, first, second);
		Vector2 cc = getMountPositionForCounterClockwise(cir, second, first);
		return c.plus(cc).times(.5);*/
	}

	static public Vector2 getMountPositionForClockwise(Circle cir, Location first, Location second) {
		// from: http://stackoverflow.com/questions/3349125/circle-circle-intersection-points
		double x0 = first.getPosition().getX();
		double y0 = first.getPosition().getY();
		double r0 = first.getCircle().getRadius() + cir.getRadius();

		double x1 = second.getPosition().getX();
		double y1 = second.getPosition().getY();
		double r1 = second.getCircle().getRadius() + cir.getRadius();

		/* dx and dy are the vertical and horizontal distances between
		 * the circle centers.
		 */
		double dx = x1 - x0;
		double dy = y1 - y0;

		/* Determine the straight-line distance between the centers. */
		//d = sqrt((dy*dy) + (dx*dx));
		double d = Math.hypot(dx, dy);

		/* 'point 2' is the point where the line through the circle
		 * intersection points crosses the line between the circle
		 * centers.
		 */

		/* Determine the distance from point 0 to point 2. */
		double a = ((r0*r0) - (r1*r1) + (d*d)) / (2.0 * d) ;

		/* Determine the coordinates of point 2. */
		double x2 = x0 + (dx * a/d);
		double y2 = y0 + (dy * a/d);

		/* Determine the distance from point 2 to either of the
		 * intersection points.
		 */
		double h = Math.sqrt((r0*r0) - (a*a));

		/* Now determine the offsets of the intersection points from
		 * point 2.
		 */
		double rx = -dy * (h/d);
		double ry = dx * (h/d);

		/* Determine the absolute intersection points. */
		//xi = x2 + rx;
		//yi = y2 + ry;
		//xi_prime = x2 - rx;
		//yi_prime = y2 - ry;

		return new Vector2(x2 - rx, y2 - ry);
	}

	static public Vector2 getMountPositionForCounterClockwise(Circle cir, Location first, Location second) {
		// from: http://stackoverflow.com/questions/3349125/circle-circle-intersection-points
		double x0 = first.getPosition().getX();
		double y0 = first.getPosition().getY();
		double r0 = first.getCircle().getRadius() + cir.getRadius();

		double x1 = second.getPosition().getX();
		double y1 = second.getPosition().getY();
		double r1 = second.getCircle().getRadius() + cir.getRadius();

		/* dx and dy are the vertical and horizontal distances between
		 * the circle centers.
		 */
		double dx = x1 - x0;
		double dy = y1 - y0;

		/* Determine the straight-line distance between the centers. */
		//d = sqrt((dy*dy) + (dx*dx));
		double d = Math.hypot(dx, dy);

		/* 'point 2' is the point where the line through the circle
		 * intersection points crosses the line between the circle
		 * centers.
		 */

		/* Determine the distance from point 0 to point 2. */
		double a = ((r0*r0) - (r1*r1) + (d*d)) / (2.0 * d) ;

		/* Determine the coordinates of point 2. */
		double x2 = x0 + (dx * a/d);
		double y2 = y0 + (dy * a/d);

		/* Determine the distance from point 2 to either of the
		 * intersection points.
		 */
		double h = Math.sqrt((r0*r0) - (a*a));

		/* Now determine the offsets of the intersection points from
		 * point 2.
		 */
		double rx = -dy * (h/d);
		double ry = dx * (h/d);

		/* Determine the absolute intersection points. */
		//xi = x2 + rx;
		//yi = y2 + ry;
		//xi_prime = x2 - rx;
		//yi_prime = y2 - ry;

		return new Vector2(x2 + rx, y2 + ry);
	}

	static public Vector2 tryPlace(Circle cir, Location first, Location second, Location... dontOverlap) {
		Vector2 pos = Helpers.getMountPositionFor(cir, first, second);

		// test with the rest
		Location loc = new Location(pos, cir);
		for (int i = 0; i < dontOverlap.length; ++i) {
			if (dontOverlap[i].overlaps(loc)) {
				return null;
			}
		}
		return pos;
	}
}
