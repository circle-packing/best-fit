package gui;

import model.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Pablo on 30/11/15.
 */
public class Drawer extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

	private Vector2 mousePosDown;
	private Vector2 offset;
	private double scale;

	public Drawer() {
		super();

		this.offset = new Vector2(0, 0);
		this.scale = 50;

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

		Graphics2D g2 = (Graphics2D)g;
		g2.translate(offset.getX(), offset.getY());
		g2.scale(scale, scale);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
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
