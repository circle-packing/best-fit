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
		//tester.DoAllTestsDefault();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Circle Packing");
				frame.setSize(800, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				//Drawer drawer = new SolutionDrawer(getSolution());
				StepSolverDrawer drawer = new StepSolverDrawer(getStepSolver());
				drawer.doSteps(755); //-1 to completely solve

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
		int count = 2000;
		// Packomania problems:
		//Problem problem = new Problem(count, 0); // 5000 good result (1min)
		//Problem problem = new Problem(count,  1.0/2.0); //Bij 3000: eerste overlap na 1900 -> check shell further
		//Problem problem = new Problem(count, -1.0/2.0); // 2000 bug, small overlap -> check shell further
		Problem problem = new Problem(count, -2.0/3.0); // 2000 overlap problem, just after 755 steps, looks like check-shell-further problem
		//Problem problem = new Problem(count, -1.0/5.0); // 1000 good result. 5000 good too (1min)

		// My problems:
		//Problem problem = new Problem(count,  -1.5); //100 NaN & overlap problem
		//Problem problem = new Problem(count,  -1.1);
		//Problem problem = new Problem(count,  -1.01);
		//Problem problem = new Problem(count,  -1); //300 NaN Problem, Ok at 250. Just after 170 steps overlap
		//Problem problem = new Problem(count,  -0.95);
		//Problem problem = new Problem(count,  -0.9);
		//Problem problem = new Problem(count,  -0.85);
		//Problem problem = new Problem(count,  -0.8);
		//Problem problem = new Problem(count,  -0.75); // 250 fine
		//Problem problem = new Problem(count,  -0.7);
		//Problem problem = new Problem(count,  -0.6);

		return problem;
	}

}
