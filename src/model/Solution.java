package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo on 21/11/15.
 */
public class Solution {

	Map<Circle, Point> solution;

	public Solution(int totalCircles) {
		this.solution = new HashMap<>(totalCircles);
	}

	public void add(Circle circle, Point point) {
		solution.put(circle, point);
	}

	public boolean isValid() {
		// TODO check if no circles overlap
		return true;
	}
}
