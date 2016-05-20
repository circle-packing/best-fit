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

## Trying different packings

1. Open `Main.java` in `src/main/java/gui/`
* Scroll down until you see the method `getProblem()`
* This method returns the problem that will be solved and visualized
* Change the variable `count` to change the number of circles in the problem
* Choose one of the predefined problems (by uncommenting one of them) or create one yourself
	* `new Problem(count, power)` gives a problem with radii `r_i = i^power`, similar to the Packomania Power problems
	* `new Problem(r1, r2, r3, r4, ...)` gives a problem with the specified radii
	* Note that only the problems specified in my thesis were tested
