public class Pendulo {
    public static void main(String[] args) {
        DampedDrivenPendulum pendulo = new DampedDrivenPendulum();
        String[] file = new String[21];
        double[][] state = new double[20][3];
        double[] rate = new double[3];
        double eps = .001, y;
        int dir = 1, x = 0;
        file = StdIn.readAllStrings();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setXscale(0, 900);
        StdDraw.setYscale(-2.5, 2.5);
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
        pendulo.initializeState(state[3]);
        pendulo.A = 0;
        y = state[0][1];
        int j = 0;
        for (int i = 0; i < 900; i++) {
            if (i % 45 == 0) {
                pendulo.gamma = Math.min(.18, state[j][1] / 10);
                j++;
            }
            pendulo.getRate(pendulo.getState(), rate);
            pendulo.state[1] += dir*rate[1];
            pendulo.state[0] += dir*pendulo.state[1] / 40;
            if (pendulo.state[1] < eps) {
                pendulo.state[1] = 0;
                if (dir == 1)
                    dir = -1;
                else
                    dir = 1;
            }
            StdDraw.line(x, y, x+1, dir*pendulo.state[1]);
            y = dir*pendulo.state[1];
            x++;
        }
    }
}

