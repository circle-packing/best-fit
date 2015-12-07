package model;

import model.Circle;
import model.Vector2;

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
		return (circle.getRadius() + other.circle.getRadius()) > position.distanceTo(other.position);
	}

	@Override
	public String toString() {
		return "Location{" +
				"position=" + position +
				", circle=" + circle +
				'}';
	}
}
