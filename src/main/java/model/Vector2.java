package model;

/**
 * Created by Pablo on 21/11/15.
 */
public class Vector2 {

	private double x;
	private double y;

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Vector2 plus(Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}

	public Vector2 minus(Vector2 other) {
		return new Vector2(x - other.x, y - other.y);
	}

	public Vector2 times(double num) {
		return new Vector2(x * num, y * num);
	}

	public double distanceTo(Vector2 other) {
		return Math.hypot(x - other.x, y - other.y);
	}

	public double lengthSquared() {
		return x*x + y*y;
	}

	static public double singedAngle(Vector2 from, Vector2 to) {
		return Math.atan2(from.x * to.y - from.y * to.x, from.x * to.x + from.y * to.y);
	}

	static public boolean isInsideTriableBy(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p) {
		// http://stackoverflow.com/questions/13300904/determine-whether-point-lies-inside-triangle

		double alpha = ((p2.y - p3.y)*(p.x - p3.x) + (p3.x - p2.x)*(p.y - p3.y)) /
				((p2.y - p3.y)*(p1.x - p3.x) + (p3.x - p2.x)*(p1.y - p3.y));
		double beta = ((p3.y - p1.y)*(p.x - p3.x) + (p1.x - p3.x)*(p.y - p3.y)) /
				((p2.y - p3.y)*(p1.x - p3.x) + (p3.x - p2.x)*(p1.y - p3.y));
		double gamma = 1.0f - alpha - beta;

		return alpha > 0 && beta > 0 && gamma > 0;
	}

	public void normalize() {
		double length = Math.sqrt(lengthSquared());
		x /= length;
		y /= length;
	}

	@Override
	public String toString() {
		return "Vector2{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
