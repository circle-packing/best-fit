package gui;

import model.Problem;
import model.Solution;
import solvers.Solver;
import solvers.bestfit.BestFitSolver;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Pablo on 21/11/15.
 */
public class Main extends Frame {

	private SolutionDrawer drawer;

	public static void main(String args[]) {
		new Main();
	}

	public Main() {
		super("Circle Packing");

		init();

		setSize(800,600);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	public void init() {
		int count = 10;
		Problem problem = new Problem(count, -0.5);

		Solver solver = new BestFitSolver(problem);
		solver.solve();

		Solution solution = solver.getSolution();

		drawer = new SolutionDrawer(solution, 50);
	}

	public void paint(Graphics g) {
		g.translate(getSize().width / 2, getSize().height / 2);
		if (drawer != null) {
			drawer.draw(g);
		} else {
			System.out.println("No drawer :(");
		}
	}
}
