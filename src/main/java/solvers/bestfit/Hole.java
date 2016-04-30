package solvers.bestfit;

import model.Circle;
import model.Location;
import model.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Pablo on 07/12/15.
 */
public class Hole {

	static final Logger LOG = LoggerFactory.getLogger("default");

	private Location first;
	private Location second;
	private Location third;

	public Hole(Location f, Location s, Location t) {
		this.first = f;
		this.second = s;
		this.third = t;
	}

	public Location getFirst() { return first; }
	public Location getSecond() { return second; }
	public Location getThird() { return third; }

	public Vector2 tryFit(Circle cir) {
		// Test to see if circle is smaller than all circles of this hole,
		// otherwise it might try to place the circle over one of the hole-circles
		if (cir.getRadius() > first.getCircle().getRadius()) return null;
		if (cir.getRadius() > second.getCircle().getRadius()) return null;
		if (cir.getRadius() > third.getCircle().getRadius()) return null;

		// Try to place circle
		Vector2 pos = Helpers.getMountPositionFor(cir, first, second);
		Location loc = new Location(pos, cir);

		// Test for overlap
		if (third.overlaps(loc)) {
			return null;
		}

		// Check that is inside the hole
		boolean inside = Vector2.isInsideTriangleBy(
				first.getPosition(),
				second.getPosition(),
				third.getPosition(),
				pos);
		if (!inside) {
			return null;
		}
		return pos;
	}
}
