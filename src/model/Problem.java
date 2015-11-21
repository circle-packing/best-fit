package model;

import com.sun.javafx.beans.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 21/11/15.
 */
public class Problem {

	private List<Circle> problem;

	public Problem(@NonNull List<Circle> problem) {
		this.problem = problem;
	}

	/**
	 * Creates a new poblem with count circles.
	 * The i'th circle will have a radius of pow(i, power)
	 * Many used powers are: 0, +1/2, -1/2, -2/3 and -1/5
	 * @param count The number of circles for this problem
	 * @param power The power of i
	 */
	public Problem(int count, double power) {
		List<Circle> circles = new ArrayList<>(count);

		for (int i = 0; i < count; ++i) {
			circles.add(new Circle(i, Math.pow((double)i, power)));
		}
	}
}
