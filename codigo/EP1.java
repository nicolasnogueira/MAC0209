/*******************************************************************
 * Nome: 	Artur Alvarez - 9292931
 *			Mateus Anjos - 9298191
 *			Nícolas Nogueira - 9277541
 *			Victor Domiciano - 8641963
 *
 * Compilacao: javac EP1.java
 * Dependencias: StdIn.java
 * Execucao: java EP1 < cronometro_sem_string.csv
 *
 *******************************************************************/

import java.util.ArrayList;

public class EP1 {

	public static void main(String[] args) {
		double[] t = new double[3];
		double[] x = {20, 40, 60};
		double[] file = new double[n * 6];
		file = StdIn.readAllDoubles();
		int l = 0;
		int aux = 0;
		//gerar n = 18 vetores de travessias (6 travessias por pessoa)
		ArrayList<Travessia> travessias = new ArrayList<Travessia>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				t[0] = 0;
				t[1] = 0;
				t[2] = 0;
				for (int k = 0; k < 2; k++) {
					System.out.println(l + "\n");
					t[0] = t[0] + file[l];
					t[1] = t[1] + file[l+1];
					t[2] = t[2] + file[l+2];
					l = l + 3;
				}
				t[0] = t[0]/2;
				t[1] = t[1]/2;
				t[2] = t[2]/2;
				if (j < 3) {
					Travessia trav = new Travessia("MRU-" + aux, x, t, true);
					travessias.add(trav);
				} else {
					Travessia trav = new Travessia("MRUV-" + aux, x, t, false);
					travessias.add(trav);
				}
				aux++;
			}
		}
	}
}