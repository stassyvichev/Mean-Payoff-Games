package test;

import java.util.ArrayList;
import java.util.HashMap;
import rand.RandL;
import graph.Graph;
import help.Helper;
import help.NegCycleFilter;
import algorithms.BCDGR;
import algorithms.SIEG;
import algorithms.SIPI;

/**
 * @author Stanimir
 *
 *Class TestRandL is used to test the running time of algorithms on RandL graphs
 *tests on graphs of size 512, edit the definition of U and L to change the upper and lower bounds of the weights.
 */
public class TestRandL {

	public static void main(String[] args) {
		Helper help= new Helper();
		//Edit the upper and lower bounds for which you want to test by editing U and L respectively:
		//ex. put 1024 in U to test on a graph with 1024 as weight upper bound.
		int[] U={512};
		int[] L={0};
		RandL rl=new RandL(512, 5*512);
		System.out.println("RANDL test (Results in Milliseconds):");
		for (int i=0;i<U.length;i++){
			System.out.println("U="+U[i]+", L="+L[i]);
			Graph g=rl.generateGraph(L[i], U[i]);
			for (int j=1; j<5; j++){
				System.out.println("NCF Type "+j);
				NegCycleFilter ncf= new NegCycleFilter(g);
				switch(j){
					case 1:ncf.add1();
						break;
					case 2:ncf.add2();
						break;
					case 3:ncf.add3();
						break;
					case 4:ncf.add4();
				}
				Graph g1=help.applyPotentialTransformation(ncf.getG(), 1, help.getMaxPotential());
				HashMap<String, ArrayList<String>> owner= help.getVertexOwnership(g1);
				
//				//Run BCDGR
				System.out.println("Run BCDGR for EG:");
				long durationBC=0;
				for (int k=0; k<10; k++){
					BCDGR bc= new BCDGR(g1, owner.get("min"), owner.get("max"));
					long startBC = System.currentTimeMillis();
					bc.getBatteryLevel();
					long endBC = System.currentTimeMillis();
					durationBC=durationBC+(endBC-startBC);
				}
				durationBC=durationBC/10;
				System.out.println("BCDGR Duration: "+durationBC);
				
//				//Run SIEG
				System.out.println("Run SIEG for EG:");
				long durationSA=0;
				for (int k=0;k<10;k++){
					SIEG sa= new SIEG(g1, owner.get("min"), owner.get("max"));
					long startSA = System.currentTimeMillis();
					sa.findStrategies();
					long endSA = System.currentTimeMillis();
					durationSA=durationSA+(endSA-startSA);
				}
				durationSA=durationSA/10;
				System.out.println("SIEG Duration: "+durationSA);
			
				//Run SI/PI
				System.out.println("Run SI/PI for MPG:");
				long durationSI=0;
				for(int k=0;k<10;k++){
					SIPI si= new SIPI(g1, owner.get("min"), owner.get("max"));
					long startSI = System.currentTimeMillis();
					si.findStrategies();
					long endSI = System.currentTimeMillis();
					durationSI=durationSI+(endSI-startSI);
				}
				durationSI=durationSI/10;
				System.out.println("SI/PI Duration: "+durationSI);
				System.out.println();
			}	
		}
	}
}
