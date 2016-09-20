public class Travessia {
	String nome;
	double[] posicao;
	double[] tempos;
	double vmedia;
	double amedia;
	boolean tipo; // true para MRU e false para MRUV
	int N = 10; // pontos a serem plotados

	public Travessia (String nom, double[] pos, double[] tem, boolean tip) {
		nome = nom;
		posicao = pos;
		tempos = tem;
		System.out.println("Nome: " + nome + " ");
		for (int i = 0; i < pos.length; i++) {
			System.out.println(i + " " + tempos[i] + " " + posicao[i]);
		//System.out.println("\n");
		}
		tipo = tip;
		vmedia = getVelMedia (pos, tem);
		if (!tip)
			amedia = getAceMedia (pos, tem);
		this.gerarImg();
	}
	public double getVelMedia (double[] pos, double[] tem) {
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
	public double getAceMedia (double[] pos, double[] tem) {
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

	public void gerarImg () {
		//dimensionar
		//StdDraw.setXscale(0, 150);
       	//StdDraw.setYscale(0, 150);
       	StdDraw.setScale(-.05, 200);
       	StdDraw.setPenRadius(.002);
		//calcular Euler
		double dt = tempos[tempos.length - 1]/N;
       	double s0 = 0;
       	double s1 = 0;
       	double t0 = 0;
       	double t1 = 0;
       	double v = 0;

		StdDraw.clear();


       	// Euler
       	StdDraw.setPenColor(StdDraw.RED);

       	if (tipo) {
			for (int i = 1; i <= N; i++) {
				//MRU
				s1 = s0 + vmedia * dt;
				t1 = t0 + dt;
				StdDraw.line(t0, s0, t1, s1);
				StdDraw.filledCircle(t1, s1, .5);
				s0 = s1;
				t0 = t1;
			}
       	} else {
			for (int i = 1; i <= N; i++) {
				//MRUV
				v = v + amedia * dt;
				s1 = s0 + v * dt;
				t1 = t0 + dt;
				StdDraw.line(t0, s0, t1, s1);
				StdDraw.filledCircle(t1, s1, .5);
				s0 = s1;
				t0 = t1;
			}
       	}

		//calcular analiticamente 
		StdDraw.setPenColor(StdDraw.BLUE);

		if (tipo) {
			//MRU
			System.out.println("Vel esperada cte");
			t0 = 0;
			s0 = 0;
			for (int i = 1; i <= N; i++) {
				s1 = vmedia * (dt * i);
				StdDraw.line(t0, s0, dt * i, s1);
				StdDraw.filledCircle(dt*i, s1, .5);
				s0 = s1;
				t0 = dt * i;
			}
		} else {
			//MRUV
			t0 = 0;
			s0 = 0;
			for (int i = 1; i <= N; i++) {
				s1 = 0.5 * amedia * (dt * i) * (dt * i);
				StdDraw.line(t0, s0, dt*i, s1);
				StdDraw.filledCircle(dt*i, s1, .5);
				s0 = s1;
				t0 = dt * i;
			}
		}

		//plotar pontos dos dados
       	s0 = 0;
       	t0 = 0;
       	for (int i = 0; i < posicao.length; i++) {
       		StdDraw.setPenColor(StdDraw.GREEN);
       		StdDraw.line(t0, s0, tempos[i], posicao[i]);
       		t0 = tempos[i];
       		s0 = posicao[i];
       		StdDraw.filledCircle(tempos[i], posicao[i], .5);
 			StdDraw.setPenColor(StdDraw.BLACK);
       		StdDraw.textLeft(tempos[i], posicao[i],"(" + tempos[i] + ", " + posicao[i] + ")");
       	}
       	/*StdDraw.setPenColor(StdDraw.BLACK);
       	for (int i = 1; i <= 10; i++)
       		StdDraw.line(dt*i, 0, dt*i, 1000);*/
		StdDraw.save(nome + ".png");
	}
}
