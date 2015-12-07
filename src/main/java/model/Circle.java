package model;

/**
 * Created by Pablo on 21/11/15.
 */
public class Circle {

	private int id;
	private double radius;

	public Circle(int id, double radius) {
		this.id = id;
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public String toString() {
		return "Circle{" +
				"id=" + id +
				", radius=" + radius +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Circle circle = (Circle) o;

		return id == circle.id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
