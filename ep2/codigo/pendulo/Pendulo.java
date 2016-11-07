public class Pendulo {
    public static void main(String[] args) {
        DampedDrivenPendulum pendulo = new DampedDrivenPendulum();
        String[] file = new String[21];
        double[][] state = new double[20][3];
        double[] rate = new double[3];
        double eps = .001;
        int dir = 1;
        int osc = 0, x = 0;
        double y;
        file = StdIn.readAllStrings();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setXscale(0, 900);
        StdDraw.setYscale(-3, 3);
        for (int i = 1; i < 21; i++) {
            state[i-1][0] = 0;
            if (file[i].length() == 13)
                if (i < 11) {
                    state[i-1][2] = Double.parseDouble(file[i].substring(2, 8));
                    state[i-1][1] = Double.parseDouble(file[i].substring(9));
                }
                else {
                    state[i-1][2] = Double.parseDouble(file[i].substring(3, 8));
                    state[i-1][1] = Double.parseDouble(file[i].substring(10));
                }
            else
                if (i < 11) {
                    state[i-1][2] = Double.parseDouble(file[i].substring(2, 8));
                    state[i-1][1] = Double.parseDouble(file[i].substring(10));
                }
                else {
                    state[i-1][2] = Double.parseDouble(file[i].substring(3, 9));
                    state[i-1][1] = Double.parseDouble(file[i].substring(11));
                }
        }
        pendulo.initializeState(state[0]);
        pendulo.A = 0;
        pendulo.gamma = 0.1;
        y = state[0][1];
        int j = 0;
        for (int i = 0; i < 900; i++) {
            /* if (i % 45 == 0) {
                pendulo.gamma = state[j][1] / 5;
                j++;
                // StdOut.println(pendulo.gamma);
            } */
            pendulo.getRate(pendulo.getState(), rate);
            if (dir == 1) {
                pendulo.state[1] += rate[1];
                pendulo.state[0] += pendulo.state[1] / 40;
            }
            else {
                pendulo.state[1] -= rate[1];
                pendulo.state[0] -= pendulo.state[1] / 40;
            }
            if (pendulo.state[1] < eps) {
                pendulo.state[1] = 0;
                if (dir == 1)
                    dir = -1;
                else {
                    osc++;
                    dir = 1;
                }
            }
            StdDraw.line(x, y, x+1, dir*pendulo.state[1]);
            // StdOut.println(dir*pendulo.state[1]);
            y = dir*pendulo.state[1];
            x++;
        }
        // StdOut.println(osc);
    }
}

