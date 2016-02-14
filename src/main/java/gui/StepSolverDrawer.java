package gui;

import solvers.bestfit.BestFitSolver;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Pablo on 30/11/15.
 */
public class StepSolverDrawer extends Drawer implements KeyListener {

	private BestFitSolver solver;

	public StepSolverDrawer(BestFitSolver solver) {
		super();

		this.solver = solver;
		solver.startStepSolve();

		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		solver.drawState((Graphics2D) g);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			doSteps(1);
		}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			solver.report();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public void doSteps(int count) {
		if (count < 0) {
			count = Integer.MAX_VALUE;
		}

		for (int i = 0; i < count; ++i) {
			if (!solver.doStepSolve()) {
				solver.report();
				break;
			}
		}
		repaint();
	}
}
