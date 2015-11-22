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

	private Vector2 getMiddle() {
		double weight = cirFirst.getRadius() / (cirFirst.getRadius() + cirSecond.getRadius());
		Vector2 dir = posSecond.minus(posFirst);
		return posFirst.plus(dir.times(weight));
	}

	private Vector2 getDir() {
		Vector2 dir = posSecond.minus(posFirst);
		return new Vector2(-dir.getY(), -dir.getX());
	}

	public Vector2 getMountPositionFor(Circle cir) {
		return getMiddle().plus(getDir().times(cir.getRadius())); //Incorrect, TODO
	}
}
