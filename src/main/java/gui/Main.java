package gui;

import model.Problem;
import model.Solution;
import model.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solvers.Solver;
import solvers.bestfit.BestFitSolver;
import testing.Tester;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Pablo on 21/11/15.
 */
public class Main {


	static final Logger LOG = LoggerFactory.getLogger("default");

	public static void main(String args[]) throws IOException {

		//Tester tester = new Tester();
		//tester.DoPackomaniaPowerTests();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Circle Packing");
				frame.setSize(800, 800);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				//Drawer drawer = new SolutionDrawer(getSolution());
				StepSolverDrawer drawer = new StepSolverDrawer(getStepSolver());
				drawer.doSteps(-1); //-1 to completely solve

				drawer.setOffset(new Vector2(frame.getWidth() / 2, frame.getHeight() / 2));

				frame.getContentPane().add(drawer);

				frame.setVisible(true);
			}
		});
	}

	private static Solution getSolution() {
		BestFitSolver solver = new BestFitSolver(getProblem());

		long startTime = System.nanoTime();
		solver.solve();
		long endTime = System.nanoTime();
		long ns = (endTime - startTime);
		long ms = (ns + 500000) / 1000000;
		long s = (ms + 500) / 1000;

		solver.report();
		LOG.info(String.format("Solve time %d(s); %d(ms); %d(ns)", s, ms, ns ));

		return solver.getSolution();
	}

	private static BestFitSolver getStepSolver() {
		return new BestFitSolver(getProblem());
	}

	private static Problem getProblem() {
		int count = 500; // 3 to 5000 works, though higher also works though visualization isn't great
		// Packomania problems:
		//Problem problem = new Problem(count, 0);
		//Problem problem = new Problem(count,  1.0/2.0);
		//Problem problem = new Problem(count, -1.0/2.0);
		Problem problem = new Problem(count, -2.0/3.0);
		//Problem problem = new Problem(count, -1.0/5.0);
		return problem;
	}

}
