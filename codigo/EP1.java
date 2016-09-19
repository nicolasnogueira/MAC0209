public class EP1 {

	public class Travessia {
		double[] posicao;
		double[] tempos;
		double vmedia;
		double amedia;
		boolean tipo; // true para MRU e false para MRUV

		public Travessia (double[] pos, double[] tem, boolean tip) {
			posicao = pos;
			tempos = tem;
			tipo = tip;
			vmedia = getVelMedia (pos, tem);
			if (!tip)
				amedia = getAceMedia (pos, tem);
		}

		public static double getVelMedia (double[] pos, double[] tem) {
			double media = 0;
			double s = 0;
			double t = 0;
			for (int i = 0; i < pos.length; i++) {
				media += (pos[i] - s)/(tem[i] - t);
				s = pos[i];
				t = tem[i];
			}
			media = media/(pos.length);
			return media;
		}

		public static double getAceMedia (double[] pos, double[] tem) {
			double media = 0;
			double s = 0;
			double v0 = 0;
			double v1;
			double t = 0;
			for (int i = 0; i < pos.length; i++) {
				v1 = ((pos[i] - s) * 2)/(tem[i] - t) - v0;
				media += (v1 - v0)/(tem[i] - t);
				t = tem[i];
				s = pos[i];
				v0 = v1;
			}
			media = media/(pos.length);
			return media;
		}
	}


	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		double t[] = new double[3];
		double x[] = {20, 40, 60};
		//gerar n vetores de travessias
		Travessia travessias[] = new Travessia[n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 6; j++) {
				t[0] = 0;
				t[1] = 0;
				t[2] = 0;
				for (int k = 0; k < 2; k++) {
					t[0] += StdIn.readDouble();
					t[1] += StdIn.readDouble();
					t[2] += StdIn.readDouble();
				}
				t[0] = t[0]/2;
				t[1] = t[1]/2;
				t[2] = t[2]/2;
				if (j < 3)
					travessias[i] = new Travessia(x, t, true);
				else
					travessias[i] = new Travessia(x, t, false);
			}
		}
	}
}