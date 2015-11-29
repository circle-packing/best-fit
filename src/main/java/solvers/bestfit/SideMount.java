package solvers.bestfit;

import model.Circle;
import model.Vector2;

/**
 * Created by Pablo on 22/11/15.
 */
public class SideMount {

	Vector2 posFirst;
	Vector2 posSecond;

	Circle cirFirst;
	Circle cirSecond;

	public SideMount(Vector2 posFirst, Vector2 posSecond, Circle cirFirst, Circle cirSecond) {
		this.posFirst = posFirst;
		this.posSecond = posSecond;
		this.cirFirst = cirFirst;
		this.cirSecond = cirSecond;
	}

	public double getSize() {
		return posFirst.distanceTo(posSecond);
	}

	public Vector2 getMountPositionFor(Circle cir) {
		// from: http://stackoverflow.com/questions/3349125/circle-circle-intersection-points
		double x0 = posFirst.getX();
		double y0 = posFirst.getY();
		double r0 = cirFirst.getRadius() + cir.getRadius();

		double x1 = posSecond.getX();
		double y1 = posSecond.getY();
		double r1 = cirSecond.getRadius() + cir.getRadius();

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

	public Vector2 getPosFirst() {
		return posFirst;
	}

	public Vector2 getPosSecond() {
		return posSecond;
	}

	public Circle getCirFirst() {
		return cirFirst;
	}

	public Circle getCirSecond() {
		return cirSecond;
	}
}
