import java.util.ArrayList;


public class EP1 {

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		double[] t = new double[3];
		double[] x = {20, 40, 60};
		double[] file = new double[n * 6];
		file = StdIn.readAllDoubles();
		int l = 0;
		int aux = 0;
		//gerar n vetores de travessias
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
				//System.out.println(t[0] + " " + t[1] + " " + t[2] + "\n");
				if (j < 3) {
					System.out.println ("Gerando MRU- " + aux + "\n");
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