package solvers.bestfit;

import model.Circle;
import model.Vector2;
import org.apache.commons.math3.complex.Complex;

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
		// From https://en.wikipedia.org/wiki/Descartes'_theorem
		double k1 = 1.0/cirFirst.getRadius(); //+1/r for externally tangent
		double k2 = 1.0/cirSecond.getRadius();
		double k3 = 1.0/cirThird.getRadius();

		double k4 = k1 + k2 + k3 + 2 * Math.sqrt(k1*k2 + k2*k3 + k3*k1);

		Complex z1 = new Complex(posFirst.getX(), posFirst.getY());
		Complex z2 = new Complex(posSecond.getX(), posSecond.getY());
		Complex z3 = new Complex(posThird.getX(), posThird.getY());

		Complex firstPart =
				z1.multiply(k1).add(
				z2.multiply(k2)).add(
				z3.multiply(k3));
		Complex secondPart =
				z1.multiply(z2).multiply(k1*k2).add(
				z2.multiply(z3).multiply(k2*k3)).add(
				z1.multiply(z3).multiply(k1*k3));
		Complex sqrtSecondPart = secondPart.sqrt().multiply(2);
		Complex z4 = firstPart.add(sqrtSecondPart).divide(k4);

		return new Vector2(z4.getReal(),z4.getImaginary());
	}

	public double getSize() {
		// From https://en.wikipedia.org/wiki/Descartes'_theorem
		double k1 = 1.0/cirFirst.getRadius(); //+1/r for externally tangent
		double k2 = 1.0/cirSecond.getRadius();
		double k3 = 1.0/cirThird.getRadius();

		double k4 = k1 + k2 + k3 + 2 * Math.sqrt(k1*k2 + k2*k3 + k3*k1);

		return 1/k4;
	}
}
