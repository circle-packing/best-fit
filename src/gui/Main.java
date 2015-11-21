package gui;

import model.Circle;
import model.Point;
import model.Problem;
import model.Solution;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

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

		makeRandomDrawer();

		setSize(800,600);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	private void makeRandomDrawer() {
		int count = 10;
		Problem problem = new Problem(count, -0.5);
		Solution solution = problem.getRandomSolution(5);
		drawer = new SolutionDrawer(solution, 25);
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
