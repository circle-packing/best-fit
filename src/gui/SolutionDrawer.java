package gui;

import model.*;

import java.awt.*;
import java.awt.Point;
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

		for (Map.Entry<Circle, model.Point> location : solution.getLocations().entrySet()) {
			int r = (int)(scale * location.getKey().getRadius());
			model.Point p = location.getValue();
			int x = (int)(scale * p.getX());
			x = x - r;
			int y = (int)(scale * p.getY());
			y = y - r;
			g.fillOval(x, y, r*2, r*2);
		}

	}
}
