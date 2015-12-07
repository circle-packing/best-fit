package solvers.bestfit;

import model.Circle;
import model.Location;
import model.Vector2;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Pablo on 07/12/15.
 */
public class NHole {

	private List<Location> locations;

	/**
	 *
	 * @param locs The first two at least must be counter-clockwise
	 */
	public NHole(Location... locs) {
		this.locations = Arrays.asList(locs);
	}

	public List<Location> getLocations() {
		return locations;
	}

	public Vector2 tryFit(Circle cir) {
		Vector2 pos = Helpers.getMountPositionFor(cir, locations.get(0), locations.get(1));

		// test with the rest
		Location loc = new Location(pos, cir);
		for (int i = 2; i < locations.size(); ++i) {
			if (locations.get(i).overlaps(loc)) {
				return null;
			}
		}
		return pos;
	}
}
