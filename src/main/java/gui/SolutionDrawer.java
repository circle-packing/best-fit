package gui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

/**
 * Created by Pablo on 21/11/15.
 */
public class SolutionDrawer extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

	private Solution solution;

	private Vector2 mousePosDown;
	private Vector2 offset;
	private double scale;

	public SolutionDrawer(Solution solution) {
		super();

		this.solution = solution;

		this.offset = new Vector2(0, 0);
		this.scale = 100;

		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
	}

	public void setOffset(Vector2 offset) {
		this.offset = offset;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0, 100));

		for (Location location : solution.getLocations()) {
			int r = (int)Math.round(scale * location.getCircle().getRadius());
			Vector2 p = location.getPosition();
			int x = (int)Math.round(scale * p.getX() + offset.getX());
			x = x - r;
			int y = (int)Math.round(scale * p.getY() + offset.getY());
			y = y - r;

			g2.fillOval(x, y, r*2, r*2);
		}
	}



	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		mousePosDown = new Vector2(e.getX(), e.getY()).minus(offset);
	}

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) {
		Vector2 pos = new Vector2(e.getX(), e.getY());
		offset = pos.minus(mousePosDown);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) { }

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		scale = Math.max(1, scale - e.getPreciseWheelRotation());
		repaint();
	}
}
