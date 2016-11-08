/*******************************************************************
 * Nome:    Artur Alvarez - 9292931
 *          Mateus Anjos - 9298191
 *          Nícolas Nogueira - 9277541
 *          Victor Domiciano - 8641963
 *
 ******************************************************************/

public class Pendulo {

    double g = 9.8;
    double length = 0.55; //em metros
    double q = 0.8; //amortecimento
    double dt = 0.04;
    int npoints = 900;

    public void printRuler () {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.line(0,0,40,0);
        StdDraw.textRight(36, 0.1, "time (s)");
        for (int i = 10; i<50; i = i+10) {
            StdDraw.line(i, 0.01, i, -0.01);
            StdDraw.textLeft(i, -0.1, i + "");
        }
        StdDraw.line(0,-3,0,3);
        StdDraw.textLeft(0, 0.5, "theta (rad)");
        for (double i = -0.4; i<=0.4; i = i+0.2) {
            StdDraw.line(0, i, 0.01, i);
            StdDraw.textLeft(0.1, i, i + "");
        }
    }

    public void euler (double q, int npoints, double dt, double length) {
        double[] omega = new double[npoints];
        double[] theta = new double[npoints];
        double[] time = new double[npoints];

        theta[0] = 0.1745; //10 graus em radianos

        for (int i = 0; i < npoints-1; i++) {
            theta[i+1] = theta[i] + omega[i]*dt;
            omega[i+1] = omega[i] - (g/length)*theta[i]*dt - q*omega[i]*dt;
            time[i+1] = time[i] + dt;
        }

        for (int i = 0; i < npoints-1; i++)
            StdDraw.line(time[i], theta[i], time[i+1], theta[i+1]);
        StdDraw.textRight(34, -0.5,"EULER");

    }

    public void euler_cromer (double q, int npoints, double dt, double length) {
        double[] omega = new double[npoints];
        double[] theta = new double[npoints];
        double[] time = new double[npoints];

        theta[0] = 0.1745; //10 graus em radianos

        for (int i = 0; i < npoints-1; i++) {
            omega[i+1] = omega[i] - (g/length)*theta[i]*dt - q*omega[i]*dt;
            theta[i+1] = theta[i] + omega[i+1]*dt;
            time[i+1] = time[i] + dt;
        }

        for (int i = 0; i < npoints-1; i++)
            StdDraw.line(time[i], theta[i], time[i+1], theta[i+1]);
        StdDraw.textRight(34, -0.7,"EULER-CROMER");

    }

    public static void main(String[] args) {
        DampedDrivenPendulum pendulo = new DampedDrivenPendulum();
        String[] file = new String[21];
        double[][] state = new double[20][3];
        double[] rate = new double[3];
        double eps = .001, y;
        int dir = 1, x = 0;
        file = StdIn.readAllStrings();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setXscale(0, 38);
        StdDraw.setYscale(-1, 1);
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
        int troca = -1;

        //como não conseguimos resultados significativos de posição do acelerômetro
        //estimamos os pontos do pêndulo com theta = 0 nos pontos em que o giroscópio aponta maior velocidade
        //para deixar claro as oscilações envolvendo esses pontos, as linhas possuem como máximo e mínimo os
        //extremos do pêndulo

        for (int i = 0; i < 21 - 2; i++) {

            StdDraw.line(state[i][2] - state[0][2], 0,(state[i][2] + state[i+1][2])/2 - state[0][2], 0.1745*troca);
            StdDraw.line((state[i][2] + state[i+1][2])/2 - state[0][2], 0.1745*troca,state[i+1][2] - state[0][2], 0);
            troca = -troca;
        }

        StdDraw.textRight(34, -0.9,"APROXIMAÇÃO BRUTA DA POSIÇÃO");

        StdDraw.setPenColor(StdDraw.BLUE);

        Pendulo pend = new Pendulo();

        pend.euler(pend.q, pend.npoints, pend.dt, pend.length);

        StdDraw.setPenColor(StdDraw.GREEN);

        pend.euler_cromer(pend.q, pend.npoints, pend.dt, pend.length);

        pend.printRuler();

    }
}

