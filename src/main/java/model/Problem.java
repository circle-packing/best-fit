package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pablo on 21/11/15.
 */
public class Problem {

	private final List<Circle> circles;

	public Problem(List<Circle> circles) {
		this.circles = circles;
	}

	/**
	 * Creates a new poblem with count circles.
	 * The i'th circle will have a radius of pow(i, power)
	 * Many used powers are: 0, +1/2, -1/2, -2/3 and -1/5
	 * @param count The number of circles for this circles
	 * @param power The power of i
	 */
	public Problem(int count, double power) {
		circles = new ArrayList<>(count);

		for (int i = 1; i <= count; ++i) {
			circles.add(new Circle(i, Math.pow((double)i, power)));
		}
	}

	public Problem(double... radii) {
		circles = new ArrayList<>(radii.length);
		for (int i = 0; i < radii.length; ++i) {
			circles.add(new Circle(i+1, radii[i]));
		}
	}

	public List<Circle> getCircles() {
		return circles;
	}

	public Solution getRandomSolution(double radius) {
		Solution sol = new Solution(circles.size());
		Random rand = new Random();
		for (Circle circle : circles) {
			sol.add(circle, new Vector2(radius * rand.nextDouble(), radius * rand.nextDouble()));
		}
		return sol;
	}
}
