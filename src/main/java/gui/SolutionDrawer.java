package gui;

import model.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by Pablo on 21/11/15.
 */
public class SolutionDrawer extends Drawer {

	private Solution solution;

	public SolutionDrawer(Solution solution) {
		super();

		this.solution = solution;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(0,0,0, 100));

		for (Location location : solution.getLocations()) {
			double r = location.getCircle().getRadius();
			Vector2 p = location.getPosition();
			double x = p.getX();
			x = x - r;
			double y = p.getY();
			y = y - r;

			Ellipse2D.Double circle = new Ellipse2D.Double(x, y, r*2.0, r*2.0);
			g2.fill(circle);
		}
	}
}
