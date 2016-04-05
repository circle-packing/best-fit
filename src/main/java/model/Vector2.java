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
		//return Math.atan2(to.y, to.x) - Math.atan2(from.y, from.x);
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
