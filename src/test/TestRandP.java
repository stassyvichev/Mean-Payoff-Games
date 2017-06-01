package test;

import java.util.ArrayList;
import java.util.HashMap;

import rand.RandP;
import graph.Graph;
import help.Helper;
import help.NegCycleFilter;
import algorithms.BCDGR;
import algorithms.SIEG;
import algorithms.SIPI;

/**
 * @author Stanimir
 *
 *Class TestRandP is used to test the running time of algorithms on RandP graphs
 *tests on graphs of size 1024, edit the definition of maxP to change the upper bounds of the possible potentials.
 */
public class TestRandP {

	public static void main(String[] args) {
		Helper help= new Helper();
		//Edit maxP to change the upper bound of the potentials for a potential transformation:
		//ex. put 1024 in maxP to test on a graph where the upper bound of the potentials is maxP.
		int[] maxP={512};
		RandP rp=new RandP(1024);
		System.out.println("RANDP test (Results in Milliseconds):");
		for (int i=0;i<maxP.length;i++){
			System.out.println("MaxP="+maxP[i]);
			Graph g=rp.generateGraph(0,maxP[i]);
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
