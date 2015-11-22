package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo on 21/11/15.
 */
public class Solution {

	Map<Circle, Point> locations;

	public Solution(int totalCircles) {
		this.locations = new HashMap<>(totalCircles);
	}

	public void add(Circle circle, Point point) {
		locations.put(circle, point);
	}

	public Map<Circle, Point> getLocations() {
		return locations;
	}

	public boolean isValid() {
		// TODO check if no circles overlap
		return true;
	}
}
