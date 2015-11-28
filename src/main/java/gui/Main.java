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
		int count = 100;
		// Packomania problems
		// These don't give very good results for now
		//Problem problem = new Problem(count, 0);
		//Problem problem = new Problem(count,  1.0/2.0);
		//Problem problem = new Problem(count, -1.0/2.0);
		//Problem problem = new Problem(count, -2.0/3.0);
		//Problem problem = new Problem(count, -1.0/5.0);

		// Problems more suited for this:
		//Problem problem = new Problem(count,  -3.0/2.0);
		//Problem problem = new Problem(count,  -1.1);
		//Problem problem = new Problem(count,  -1.01);
		Problem problem = new Problem(count,  -0.8);

		Solver solver = new BestFitSolver(problem);

		long startTime = System.nanoTime();
		solver.solve();
		long endTime = System.nanoTime();
		long ns = (endTime - startTime);
		long ms = (ns + 500000) / 1000000;
		long s = (ms + 500) / 1000;

		System.out.println(String.format("Solve time %d(s); %d(ms); %d(ns)", s, ms, ns ));

		Solution solution = solver.getSolution();

		drawer = new SolutionDrawer(solution, 200);
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
