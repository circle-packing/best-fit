package gui;

import model.Problem;
import model.Solution;
import model.Vector2;
import solvers.Solver;
import solvers.bestfit.BestFitSolver;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Pablo on 21/11/15.
 */
public class Main {

	public static void main(String args[]) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Circle Packing");
				frame.setSize(800, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				SolutionDrawer drawer = new SolutionDrawer(solve());
				drawer.setOffset(new Vector2(frame.getWidth() / 2, frame.getHeight() / 2));
				frame.getContentPane().add(drawer);

				frame.setVisible(true);
			}
		});
	}

	private static Solution solve() {
		int count = 100;
		// Packomania problems
		// These don't give very good results for now
		//Problem problem = new Problem(count, 0);
		//Problem problem = new Problem(count,  1.0/2.0);
		//Problem problem = new Problem(count, -1.0/2.0);
		//Problem problem = new Problem(count, -2.0/3.0);
		//Problem problem = new Problem(count, -1.0/5.0);

		// Problems more suited for this:
		//Problem problem = new Problem(count,  -1.5);
		//Problem problem = new Problem(count,  -1.1);
		//Problem problem = new Problem(count,  -1.01);
		//Problem problem = new Problem(count,  -1);
		//Problem problem = new Problem(count,  -0.8);
		Problem problem = new Problem(count,  -0.7);
		//Problem problem = new Problem(count,  -0.6);

		Solver solver = new BestFitSolver(problem);

		long startTime = System.nanoTime();
		solver.solve();
		long endTime = System.nanoTime();
		long ns = (endTime - startTime);
		long ms = (ns + 500000) / 1000000;
		long s = (ms + 500) / 1000;

		System.out.println(String.format("Solve time %d(s); %d(ms); %d(ns)", s, ms, ns ));

		return solver.getSolution();
	}

}
