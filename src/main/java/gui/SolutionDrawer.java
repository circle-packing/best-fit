package gui;

import model.*;

import java.awt.*;
import java.util.Map;

/**
 * Created by Pablo on 21/11/15.
 */
public class SolutionDrawer {

	private Solution solution;
	private double scale;

	public SolutionDrawer(Solution solution, double scale) {
		this.solution = solution;
		this.scale = scale;
	}

	public void draw (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		for (Map.Entry<Circle, Vector2> location : solution.getLocations().entrySet()) {
			int r = (int)Math.round(scale * location.getKey().getRadius());
			Vector2 p = location.getValue();
			int x = (int)Math.round(scale * p.getX());
			x = x - r;
			int y = (int)Math.round(scale * p.getY());
			y = y - r;
			g.fillOval(x, y, r*2, r*2);
		}

	}
}
