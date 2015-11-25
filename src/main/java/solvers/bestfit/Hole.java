package solvers.bestfit;

import model.Circle;
import model.Vector2;

/**
 * Created by Pablo on 22/11/15.
 */
public class Hole {

	Vector2 posFirst;
	Vector2 posSecond;
	Vector2 posThird;

	Circle cirFirst;
	Circle cirSecond;
	Circle cirThird;

	public Hole(Vector2 posFirst, Vector2 posSecond, Vector2 posThird, Circle cirFirst, Circle cirSecond, Circle cirThird) {
		this.posFirst = posFirst;
		this.posSecond = posSecond;
		this.posThird = posThird;
		this.cirFirst = cirFirst;
		this.cirSecond = cirSecond;
		this.cirThird = cirThird;
	}

	public Vector2 getCenter() {
		//from http://www.mathopenref.com/coordincenter.html
		/*Vector2 first = posFirst.plus(posSecond.minus(posFirst).times(cirFirst.getRadius() / (cirFirst.getRadius()+cirSecond.getRadius())));
		Vector2 second = posSecond.plus(posThird.minus(posSecond).times(cirSecond.getRadius() / (cirSecond.getRadius()+cirThird.getRadius())));
		Vector2 third = posThird.plus(posFirst.minus(posThird).times(cirThird.getRadius() / (cirThird.getRadius()+cirFirst.getRadius())));

		double Ax = first.getX();
		double Ay = first.getY();
		double Bx = second.getX();
		double By = second.getY();
		double Cx = third.getX();
		double Cy = third.getY();*/

		/*double a = second.distanceTo(third);
		double b = third.distanceTo(first);
		double c = first.distanceTo(second);

		double p = a + b + c;

		return new Vector2((a*Ax + b*Bx + c*Cx)/p, (a*Ay + b*By + c*Cy)/p);*/
		//return new Vector2((Ax+Bx+Cx)/3.0, (Ay+By+Cy)/3.0);

		double a = cirFirst.getRadius();
		double b = cirSecond.getRadius();
		double c = cirThird.getRadius();
		double p = a + b + c;

		double Ax = posFirst.getX();
		double Ay = posFirst.getY();
		double Bx = posSecond.getX();
		double By = posSecond.getY();
		double Cx = posThird.getX();
		double Cy = posThird.getY();

		return new Vector2((a*Ax + b*Bx + c*Cx)/p, (a*Ay + b*By + c*Cy)/p);

		// solve( (x-a)^2+(y-z)^2 = (r+e)^2, (x-q)^2+(y-s)^2 = (r+d)^2, (x-w)^2+(y-c)^2 = (r+v)^2, [x,y,r])
	}

	public double getSize() {
		return getCenter().distanceTo(posFirst) - cirFirst.getRadius();
	}
}
