import edu.princeton.cs.algs4.*;

public class AnalyticResults {
    public static void main(String[] args) {
        double y0 = 10;
        double v0 = 0;
        double t = 0;
        double dt = 0.01; // time step
        double y = y0;
        double v = v0;
        double g = 9.8;   // gravitational field
        for (int n = 0; n < 100; n++) {
            y = y + v * dt;
            v = v - g * dt; // use Euler algorithm
            t = t + dt;
        }
        System.out.println("Results");
        System.out.println("final time = " + t);
        // display numerical result
        System.out.println("y = " + y + " v = " + v);
        // display analytic result
        double yAnalytic = y0 + v0 * t - 0.5 * g * t * t;
        double vAnalytic = v0 - g * t;
        System.out.println("analytic y = " + yAnalytic + " v = " + vAnalytic);
    }
}

