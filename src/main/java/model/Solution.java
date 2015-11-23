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
		for (Map.Entry<Circle, Vector2> first : locations.entrySet()) {
			Vector2 firstPos = first.getValue();
			double firstRad = first.getKey().getRadius();
			for (Map.Entry<Circle, Vector2> second : locations.entrySet()) {
				Vector2 secondPos = second.getValue();
				double secondRad = second.getKey().getRadius();

				if (first != second) {
					if (firstPos.distanceTo(secondPos) < firstRad + secondRad) {
						return false;
					}
				}
			}
		}
		return true;
	}
}