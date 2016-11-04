/*******************************************************************
 * Compilacao: javac-algs4 PlotFunction.java
 * Execucao: java-algs4 PlotFunction n < cronometro_sem_string.csv
 * 0 <= n < 18
 * 
 * OBS:
 * 0  <= n < 6: Mateus
 * 6  <= n < 11: Victor
 * 11 <= n < 18: Artur
 *
 * Os 3 primeiros ns de cada intervalo correspondem ao MU; os 3
 * ultimos, correspondem ao MUV.
 *
 *******************************************************************/

import edu.princeton.cs.algs4.*;

public class PlotFunction {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        double[] file = new double[108];
        double[] x1 = new double[4];
        double[] x2 = new double[4];
        double[] y = new double[4];
        file = StdIn.readAllDoubles();
        StdDraw.setXscale(0, 50);
        StdDraw.setYscale(0, 65);
        x1[0] = x2[0] = 0;
        for (int i = 1; i < 4; i++) {
            y[i] = 20 * i;
            x1[i] = file[n * 6 + i - 1];
        }
        for (int i = 1; i < 4; i++)
            x2[i] = file[n * 6 + i + 2];
        StdDraw.setPenRadius(.002);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(0, 0, .5);
        for (int i = 0; i < 3; i++) {
            StdDraw.line(x1[i], y[i], x1[i+1], y[i+1]);
            StdDraw.filledCircle(x1[i+1], y[i+1], .5);
        }
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(0, 0, .5);
        for (int i = 0; i < 3; i++) {
            StdDraw.line(x2[i], y[i], x2[i+1], y[i+1]);
            StdDraw.filledCircle(x2[i+1], y[i+1], .5);
        }
    }
}

