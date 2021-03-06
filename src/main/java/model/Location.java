package model;

import java.util.List;

/**
 * Created by Pablo on 30/11/15.
 */
public class Location {

	private Vector2 position;
	private Circle circle;

	public Location(Vector2 position, Circle circle) {
		this.position = position;
		this.circle = circle;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Circle getCircle() {
		return circle;
	}

	public boolean overlaps(Location other) {
		return (circle.getRadius() + other.circle.getRadius()) > position.distanceTo(other.position) + 1e-10;
	}

	public boolean contains(Location other) {
		double xc0 = position.getX() - other.position.getX();
		double yc0 = position.getY() - other.position.getY();
		return Math.sqrt(xc0 * xc0 + yc0 * yc0) < circle.getRadius() - other.circle.getRadius() + 1e-10;
	}

	public static Location calculateEnclosingCircle(Location first, Location second) {
		double x1 = first.getPosition().getX(), y1 = first.getPosition().getY(), r1 = first.getCircle().getRadius();
		double x2 = second.getPosition().getX(), y2 = second.getPosition().getY(), r2 = second.getCircle().getRadius();

		double x12 = x2 - x1;
		double y12 = y2 - y1;
		double r12 = r2 - r1;
		double l = Math.sqrt(x12 * x12 + y12 * y12);

		double x = (x1 + x2 + x12 / l * r12) / 2.0;
		double y = (y1 + y2 + y12 / l * r12) / 2.0;
		double r = (l + r1 + r2) / 2.0;

		return new Location(new Vector2(x,y), new Circle(-1, r));
	}

	public static Location calculateEnclosingCircle(Location first, Location second, Location third) {
		double x1 = first.getPosition().getX(), y1 = first.getPosition().getY(), r1 = first.getCircle().getRadius();
		double x2 = second.getPosition().getX(), y2 = second.getPosition().getY(), r2 = second.getCircle().getRadius();
		double x3 = third.getPosition().getX(), y3 = third.getPosition().getY(), r3 = third.getCircle().getRadius();

		double a2 = 2.0 * (x1 - x2);
		double b2 = 2.0 * (y1 - y2);
		double c2 = 2.0 * (r2 - r1);
		double d2 = x1 * x1 + y1 * y1 - r1 * r1 - x2 * x2 - y2 * y2 + r2 * r2;
		double a3 = 2.0 * (x1 - x3);
		double b3 = 2.0 * (y1 - y3);
		double c3 = 2.0 * (r3 - r1);
		double d3 = x1 * x1 + y1 * y1 - r1 * r1 - x3 * x3 - y3 * y3 + r3 * r3;
		double ab = a3 * b2 - a2 * b3;
		double xa = (b2 * d3 - b3 * d2) / ab - x1;
		double xb = (b3 * c2 - b2 * c3) / ab;
		double ya = (a3 * d2 - a2 * d3) / ab - y1;
		double yb = (a2 * c3 - a3 * c2) / ab;
		double A = xb * xb + yb * yb - 1.0;
		double B = 2.0 * (xa * xb + ya * yb + r1);
		double C = xa * xa + ya * ya - r1 * r1;
		double r = (-B - Math.sqrt(B * B - 4.0 * A * C)) / (2.0 * A);

		double x = xa + xb * r + x1;
		double y = ya + yb * r + y1;

		return new Location(new Vector2(x,y), new Circle(-1, r));
	}

	public static Location calculateEnclosingCircle(List<Location> circles) {
		return recursiveWelzl(
				circles.toArray(new Location[circles.size()]), circles.size(),
				new Location[3], 0);
	}

	private static Location recursiveWelzl(Location[] points, int n, Location[] boundary, int b)
	{
		//based on http://www.sunshine2k.de/coding/java/Welzl/Welzl.html

		Location localCircle = null;

		// terminal cases
		if (b == 3)
			localCircle = calculateEnclosingCircle(boundary[0], boundary[1], boundary[2]);
		else if (n == 1 && b == 0)
			localCircle = points[0];
		else if (n == 0 && b == 2)
			localCircle = calculateEnclosingCircle(boundary[0], boundary[1]);
		else if (n == 1 && b == 1)
			localCircle = calculateEnclosingCircle(boundary[0], points[0]);
		else
		{
			localCircle = recursiveWelzl(points, n-1, boundary, b);
			if (!localCircle.contains(points[n-1]))
			{
				boundary[b++] = points[n-1];
				localCircle = recursiveWelzl(points, n-1, boundary, b);
			}
		}
		return localCircle;
	}

	public static Location dumb_calculateEnclosingCircle(List<Location> circles) {
		for(Location first : circles) {
			for(Location second : circles) {
				if (first == second) continue;

				Location enclosingFS = calculateEnclosingCircle(first, second);
				for(Location third : circles) {
					if (third == first || third == second) continue;

					Location enclosingST = calculateEnclosingCircle(second, third);
					Location enclosingFT = calculateEnclosingCircle(first, third);

					Location enclosingTotal = null;
					if (enclosingFS.contains(third)) {
						enclosingTotal = enclosingFS;
					}
					else if (enclosingST.contains(first)) {
						enclosingTotal = enclosingST;
					}
					else if (enclosingFT.contains(second)) {
						enclosingTotal = enclosingFT;
					}
					else {
						enclosingTotal = calculateEnclosingCircle(first, second, third);
					}

					boolean enclosesAll = true;
					for(Location other : circles) {
						if (!enclosingTotal.contains(other)) {
							enclosesAll = false;
							break;
						}
					}

					if (enclosesAll) {
						return enclosingTotal;
					}
					//else continue searching;
				}
			}
		}

		// Should never happen
		throw new RuntimeException("Couldn't find enclosing circle, something went wrong :s");
	}

	public double areaOfIntersectionWith(Location other)
	{
		double x0 = position.getX(), y0 = position.getY(), r0 = circle.getRadius();
		double x1 = other.position.getX(), y1 = other.position.getY(), r1 = other.circle.getRadius();
		double rr0 = r0 * r0;
		double rr1 = r1 * r1;
		double d = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));

		if (d <= Math.abs(r0 - r1)) // full overlap
		{
			// Return area of smallest circle1
			return Math.PI * Math.min(rr0, rr1);
		}
		else // Circles partially overlap
		{
			// from http://mathforum.org/library/drmath/view/54785.html
			double a = (Math.acos((rr0 + (d * d) - rr1) / (2 * r0 * d))) * 2;
			double b = (Math.acos((rr1 + (d * d) - rr0) / (2 * r1 * d))) * 2;
			double area1 = Math.abs(0.5 * b * rr1 - 0.5 * rr1 * Math.sin(b));
			double area2 = Math.abs(0.5 * a * rr0 - 0.5 * rr0 * Math.sin(a));
			double overlap = area1 + area2;

			if (Double.isNaN(overlap)) { // NaN if no overlap
				return 0;
			}
			return overlap;
		}
	}


	@Override
	public String toString() {
		return "Location{" +
				"position=" + position +
				", circle=" + circle +
				'}';
	}
}
