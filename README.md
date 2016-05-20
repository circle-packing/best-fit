# Best-fit

A constructive heuristic for the circle packing problem.

by Pablo Bollansée.

## Requirements

The project was made using IntelliJ IDEA.
A free community edition is available [here](https://www.jetbrains.com/idea/#chooseYourEdition).

It's a Maven project, however I wasn't able to run it without using IntelliJ IDEA.
I'm not sure why yet, if I find the problem I will update this readme, but any help is appreciated.
Sorry for the inconvenience.

## Building & Running

1. Install IntelliJ IDEA
* Open the project in IntelliJ
* Navigate to the main GUI file: `src/main/java/gui/Main.java`
* Run the project by right-clicking in Main.java, and selecting "Run Main.main()" or clicking the little green arrow in the top right
	* If that option isn't available wait until IntalliJ IDEA has parsed everything
* Now the project should start building, and will soon show the packing.
	* In the console some information about the packing should be shown
* You can scroll to zoom, and drag to move. Packings can differ greatly in size, so some zooming will probably be required.
	* Especially the problem with power `1.0/2.0` has some huge circles, so don't panic if the entire screen is blue, you just have to zoom out a lot.

## Trying different packings

1. Open `Main.java` in `src/main/java/gui/`
* Scroll down until you see the method `getProblem()`
* This method returns the problem that will be solved and visualized
* Change the variable `count` to change the number of circles in the problem
* Choose one of the predefined problems (by uncommenting one of them) or create one yourself
	* `new Problem(count, power)` gives a problem with radii `r_i = i^power`, similar to the Packomania Power problems
	* `new Problem(r1, r2, r3, r4, ...)` gives a problem with the specified radii
	* Note that only the problems specified in my thesis were tested

## Visualizing steps

By default the program will do the entire packing, and then visualize it.
It is however possible to visualize each step separately.
You can do this as followed:

1. Open `Main.java` in `src/main/java/gui/`
* Find the line that says `drawer.doSteps(-1); //-1 to completely solve` (should be line 40)
* This line specifies how many steps the solver should do before showing the initial packing
	* Change this to `0` if you want to see only the initialization step
	* Change it to any number to let the solver do that many steps
* Press space to go to the next step
* Hold space to repeatedly do steps

## Timing a solution

By default the required packing time isn't shown.
This is because the visualizer is used that looks better and allows you to visualize each step separately.
To sole a problem and show the required time look for these lines:

	//Drawer drawer = new SolutionDrawer(getSolution());
	StepSolverDrawer drawer = new StepSolverDrawer(getStepSolver());
	drawer.doSteps(-1); //-1 to completely solve

Reverse the commenting to:

	Drawer drawer = new SolutionDrawer(getSolution());
	//StepSolverDrawer drawer = new StepSolverDrawer(getStepSolver());
	//drawer.doSteps(-1); //-1 to completely solve

This will show some info, including the required time, in the console, and show a gray packing.
Note that you can only do full packing, not parts of it as is possible by default.
Also note that the required time is the time for the packing only, there are some extra checks done to calculate the shown info, which isn't included in the time.
