/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

import java.awt.Graphics;
import java.lang.*;
import java.util.*;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.display2d.*;
import org.opensourcephysics.controls.*;

/**
 * Freeway uses the Nagel-Schreckenberg model of single lane traffic
 *
 * @author Jan Tobochnik, Wolfgang Christiann, Harvey Gould
 * @version 1.0  revised 06/24/05
 */
public class FreewayF implements Drawable {
  public int[] v, x, xtemp, escolhido;
  public LatticeFrame spaceTime;
  public double[] distribution;
  public int roadLength;
  public int onramp;
  public int offramp;
  public int existeescolhido;
  public int numberOfCars;
  public int maximumVelocity;
  public double p;             // probability of reducing velocity
  public TreeMap<Integer, Integer> distGaps;
  public TreeMap<Integer, Integer> distVel;
  private CellLattice road;
  public double flow;
  public int steps, t;
  public int scrollTime = 100; // number of time steps before scrolling space-time diagram

  /**
   * Initializes arrays and starting configuration of cars.
   */
  public void initialize(LatticeFrame spaceTime) {
    this.spaceTime = spaceTime;
    onramp = (int) roadLength/3;
    offramp = (int) roadLength*2/3;
    System.out.println(onramp + " " + offramp);
    x = new int[numberOfCars];
    escolhido = new int[numberOfCars];
    existeescolhido = 0;
    xtemp = new int[numberOfCars]; // used to allow parallel updating
    v = new int[numberOfCars];
    spaceTime.resizeLattice(roadLength, 100);
    road = new CellLattice(roadLength, 1);
    road.setIndexedColor(0, java.awt.Color.RED);
    road.setIndexedColor(1, java.awt.Color.GREEN);
    spaceTime.setIndexedColor(0, java.awt.Color.RED);
    spaceTime.setIndexedColor(1, java.awt.Color.GREEN);
    int d = roadLength/numberOfCars;
    x[0] = 0;
    v[0] = maximumVelocity;
    for(int i = 1;i<numberOfCars;i++) {
      x[i] = x[i-1]+d;
      if(Math.random()<0.5) {
        v[i] = 0;
      } else {
        v[i] = 1;
      }
    }
    flow = 0;
    steps = 0;
    t = 0;
  }

  /**
   * Does one time step
   */
  public void step() {
    distGaps = new TreeMap<Integer, Integer>();
    distVel = new TreeMap<Integer, Integer>();
    int achou = 0;
    //ordenar vetor! (por algum motivo o vetor não ficava em ordem crescente, mesmo que de forma deslocada, 
    //não consegui descobrir o motivo, por isso ordenei para que pudesse fazer as operações corretamente)
    int auxx, auxy, auxw;
    for(int i = 0; i<numberOfCars; i++){
      for(int j = 0; j<numberOfCars-1; j++){
        if(x[j] > x[j + 1]){
          auxx = x[j];
          auxy = v[j];
          auxw = escolhido[j];
          x[j] = x[j+1];
          v[j] = v[j+1];
          escolhido[j] = escolhido[j+1];
          v[j+1] = auxy;
          x[j+1] = auxx;
          escolhido[j+1] = auxw;
        }
      }
    }
    for(int i = 0;i<numberOfCars;i++) {
      xtemp[i] = x[i];
    }
    for(int i = 0;i<numberOfCars;i++) {
      achou = 0;
      if(v[i]<maximumVelocity) {
        v[i]++;                                   // acceleration
      }
      int d = xtemp[(i+1)%numberOfCars]-xtemp[i]; // distance between cars
      if(d<=0) {                                  // periodic boundary conditions, d = 0 correctly treats one car on road
        d += roadLength;
      }
      if (existeescolhido == 0 && Math.random()<p) {
        escolhido[i] = 1;
        existeescolhido = 1; //só um carro quer entrar na offramp por vez!
      }
      if (xtemp[i]<offramp) {
        //o carro está antes da offramp
        if (d<(offramp-xtemp[i])){
          //o proximo carro esta mais perto que a offramp
          if(v[i]>=d) {
            v[i] = d-1; // slow down due to cars in front
          }
          if((v[i]>0)&&(Math.random()<p)) {
            v[i]--;     // randomization
          }
        } else {
          if (escolhido[i] == 1){ //ele tem que desacelerar para a offramp
            if(v[i]>=(offramp-xtemp[i])) {
              v[i] = (offramp-xtemp[i])-1;
            }
            if(v[i]==0)
              v[i]++;

          }
        }
      } else {
        if (xtemp[i]!=offramp || escolhido[i] == 0){ //tratamento comum
          if(v[i]>=d) {
            v[i] = d-1; // slow down due to cars in front
          }
          if((v[i]>0)&&(Math.random()<p)) {
            v[i]--;     // randomization
          }
        } else { //se chegou aqui com certeza xtemp[i] == offramp e escolhido[i] == 1
          //carro tem que sair da estrada e entrar pela onramp
          //remanejar todo o vetor de carros para incluir um carro na onramp
          //achar menor i
          int imenor = 0;
          escolhido[i] = 0;
          existeescolhido = 0;
          for (int it=0; it<numberOfCars;it++) {
            if (xtemp[it] < xtemp[imenor])
              imenor = it;
          }
          for (int it=0; it<numberOfCars; it++) {
            if (xtemp[(imenor+it+1)%numberOfCars] < onramp) {
              continue;
            }
            achou = 1;
            int aux = imenor+it;
            int xt, xttemp, vt, temppos;
            if (xtemp[(aux+1)%numberOfCars] == onramp) { //remanejar pensando em encaixar o carro na próxima posição dísponível depois de onramp
              aux = aux + 2;
              int pos = onramp+1;
              while (true) {
                if (pos < xtemp[(aux)%numberOfCars]){
                  break;
                }
                pos++;aux++;
              }
              //posição pos é a que vc vai colocar
              xttemp = xt = pos;
            } else {
              //remanejar encaixando o carro em onramp
              xttemp = xt = onramp;
            }
            //fazer o shift à direita do subvetor [aux+1, i]
            vt = v[i];           
            for (int s=i; s!=((aux+1)%numberOfCars); s = Math.floorMod((s-1),numberOfCars)) {
              System.out.println(s + " " + Math.floorMod((s-1),numberOfCars) + " " + -1%10);
              x[s] = x[Math.floorMod((s-1),numberOfCars)];
              xtemp[s] = xtemp[Math.floorMod((s-1),numberOfCars)];
              v[s] = v[Math.floorMod((s-1),numberOfCars)];              
            }
            x[(aux+1)%numberOfCars] = xt;
            xtemp[(aux+1)%numberOfCars] = xttemp;
            v[(aux+1)%numberOfCars] = vt;
            break;
          }
        }
      }
      if (achou == 0){ //não houve operação de shift
        x[i] = (xtemp[i]+v[i])%roadLength;
        flow += v[i];
      }
    }
    steps++;
    computeSpaceTimeDiagram();
  }

  //printa distribuicoes de velocidades e distancias entre carros
  public void computeDistributions() {
    distGaps = new TreeMap<Integer, Integer>();
    distVel = new TreeMap<Integer, Integer>();
    int tem;
    for (int ind : v) {
      tem = 0;
      if (distVel.containsKey(ind)) {
        tem = distVel.get(ind);
      }
      distVel.put(ind, tem + 1);
    }
    System.out.println("Tempo " + t + "\nVelocidades: Quantidade");
    for(Map.Entry<Integer,Integer> entry : distVel.entrySet()) {
      Integer key = entry.getKey();
      Integer value = entry.getValue();

      System.out.println(key + ": " + value);
    }    
    tem = 0;
    for (int ind = 1; ind < xtemp.length; ind++) {
      int gap = Math.abs(x[ind-1] - x[ind]);
      tem = 0;
      if (distGaps.containsKey(gap)) {
        tem = distGaps.get(gap);
      }
      distGaps.put(gap, tem + 1);
    }
    System.out.println("Tempo " + t + "\nDistâncias: Quantidade");
    for(Map.Entry<Integer,Integer> entry : distGaps.entrySet()) {
      Integer key = entry.getKey();
      Integer value = entry.getValue();

      System.out.println(key + ": " + value);
    } 
  }

  public void computeSpaceTimeDiagram() {
    t++;
    if(t<scrollTime) {
      for(int i = 0;i<numberOfCars;i++) {
        spaceTime.setValue(x[i], t, 1);
      }
    } else {                                       // scroll diagram
      for(int y = 0;y<scrollTime-1;y++) {
        for(int i = 0;i<roadLength;i++) {
          spaceTime.setValue(i, y, spaceTime.getValue(i, y+1));
        }
      }
      for(int i = 0;i<roadLength;i++) {
        spaceTime.setValue(i, scrollTime-1, 0);    // zero last row
      }
      for(int i = 0;i<numberOfCars;i++) {
        spaceTime.setValue(x[i], scrollTime-1, 1); // add new row
      }
    }
  }

  /**
    * Draws freeway.
    */
  public void draw(DrawingPanel panel, Graphics g) {
    if(x==null) {
      return;
    }
    road.setBlock(0, 0, new byte[roadLength][1]);
    for(int i = 0;i<numberOfCars;i++) {
      road.setValue(x[i], 0, (byte) 1);
    }
    road.draw(panel, g);
    g.drawString("Number of Steps = "+steps, 10, 20);
    g.drawString("Flow = "+ControlUtils.f3(flow/(roadLength*steps)), 10, 40);
    g.drawString("Density = "+ControlUtils.f3(((double) numberOfCars)/(roadLength)), 10, 60);
  }
}

/* 
 * Open Source Physics software is free software; you can redistribute
 * it and/or modify it under the terms of the GNU General Public License (GPL) as
 * published by the Free Software Foundation; either version 2 of the License,
 * or(at your option) any later version.

 * Code that uses any portion of the code in the org.opensourcephysics package
 * or any subpackage (subdirectory) of this package must must also be be released
 * under the GNU GPL license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston MA 02111-1307 USA
 * or view the license online at http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2007  The Open Source Physics project
 *                     http://www.opensourcephysics.org
 */
