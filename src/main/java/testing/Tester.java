package testing;

import model.Problem;
import model.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solvers.Solver;
import solvers.bestfit.BestFitSolver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pablo on 31/03/16.
 */
public class Tester {

    static final String DELIMITER = ", ";

    static final Logger LOG = LoggerFactory.getLogger("default");

    BufferedWriter writer;

    public Tester() throws IOException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();

        String filename = "Results " + format.format(date) + ".csv";
        writer = new BufferedWriter(new FileWriter(filename));

        WriteInfoHeader();
    }

    public void DoAllTestsDefault() throws IOException {
        DoAllTests(5, 100);
    }

    public void DoAllTests(int from, int to) throws IOException {
        DoPackomania(from, to, 0, "0");
        DoPackomania(from, to, 1.0/2.0, "1/2");
        DoPackomania(from, to, -1.0/2.0, "-1/2");
        DoPackomania(from, to, -2.0/3.0, "-2/3");
        DoPackomania(from, to, -1.0/5.0, "-1/5");
    }

    public void DoPackomania(int from, int to, double power, String id) throws IOException {
        for(int i = from; i <= to; ++i) {
            Problem p = new Problem(i, power);
            DoAndWriteTest(p, "Packomania " + id + " (" + i + ")");
        }
    }

    public void DoAndWriteTest(Problem problem, String id) throws IOException {
        Solver solver = new BestFitSolver(problem);

        long startTime = System.nanoTime();
        solver.solve();
        long endTime = System.nanoTime();

        long ns = (endTime - startTime);
        Solution sol = solver.getSolution();

        int circleCount = problem.getCircles().size();
        double finalSize = sol.calculateBoundingCircle().getCircle().getRadius();
        double overlap = sol.calculateOverlap();
        int nanCount = sol.countNaN();

        WriteInfo(id, circleCount, finalSize, ns, overlap, nanCount);
        LOG.info("Test written: " + id);
    }

    void WriteInfoHeader() throws IOException {
        writer.append("id");
        writer.append(DELIMITER);

        writer.append("circlecount");
        writer.append(DELIMITER);

        writer.append("size (radius)");
        writer.append(DELIMITER);

        writer.append("time (ns)");
        writer.append(DELIMITER);

        writer.append("overlap (square units)");
        writer.append(DELIMITER);

        writer.append("nancount");

        writer.newLine();
        writer.flush();
    }

    void WriteInfo(String id, int circleCount, double finalSize, long nsTime, double overlap, int nanCount) throws IOException {
        writer.append(id);
        writer.append(DELIMITER);

        writer.append(Integer.toString(circleCount));
        writer.append(DELIMITER);

        writer.append(Double.toString(finalSize));
        writer.append(DELIMITER);

        writer.append(Long.toString(nsTime));
        writer.append(DELIMITER);

        writer.append(Double.toString(overlap));
        writer.append(DELIMITER);

        writer.append(Integer.toString(nanCount));

        writer.newLine();
        writer.flush();
    }

}
