package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pablo on 21/11/15.
 */
public class Solution {

	List<Location> locations;

	public Solution(int totalCircles) {
		this.locations = new ArrayList<>(totalCircles);
	}

	public void add(Circle circle, Vector2 position) {
		locations.add(new Location(position, circle));
	}

	public void add(Location loc) { locations.add(loc); }

	public List<Location> getLocations() {
		return locations;
	}

	public double calculateError() {
		double error = 0;
		for (Location first : locations) {
			Vector2 firstPos = first.getPosition();
			double firstRad = first.getCircle().getRadius();
			for (Location second : locations) {
				Vector2 secondPos = second.getPosition();
				double secondRad = second.getCircle().getRadius();

				if (first != second) {
					double diff = firstPos.distanceTo(secondPos) - (firstRad + secondRad);
					if (diff < 0) {
						error += diff;
					}
				}
			}
		}
		return error;
	}
}
