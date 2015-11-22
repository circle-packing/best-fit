package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo on 21/11/15.
 */
public class Solution {

	Map<Circle, Vector2> locations;

	public Solution(int totalCircles) {
		this.locations = new HashMap<>(totalCircles);
	}

	public void add(Circle circle, Vector2 position) {
		locations.put(circle, position);
	}

	public Map<Circle, Vector2> getLocations() {
		return locations;
	}

	public boolean isValid() {
		// TODO check if no circles overlap
		return true;
	}
}
