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

	public double calculateOverlap() {
		double overlap = 0;
		for (int i = 0; i < locations.size(); ++i) {
			for (int j = i+1; j < locations.size(); ++j) {
				overlap += locations.get(i).areaOfIntersectionWith(locations.get(j));
			}
		}
		return overlap;
	}

	public double calculateOverlapRadius() {
		double overlap = 0;
		for (int i = 0; i < locations.size(); ++i) {
			Location loci = locations.get(i);
			for (int j = i+1; j < locations.size(); ++j) {
				Location locj = locations.get(j);
				double distance = loci.getPosition().distanceTo(locj.getPosition());
				double combinedRadius = loci.getCircle().getRadius() + locj.getCircle().getRadius();

				if (distance < combinedRadius) {
					overlap += combinedRadius - distance;
				}
			}
		}
		return overlap;
	}

	public int countNaN() {
		int count = 0;
		for (Location loc : locations) {
			Vector2 pos = loc.getPosition();
			if (Double.isNaN(pos.getX()) || Double.isNaN(pos.getY())) {
				++count;
			}
		}
		return count;
	}

	public Location calculateBoundingCircle() {
		return Location.calculateEnclosingCircle(locations);
	}
}
