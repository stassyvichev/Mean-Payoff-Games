package test;

import java.util.ArrayList;
import java.util.HashMap;

import rand.Rand4;
import rand.RandL;
import rand.RandP;
import graph.Graph;
import grid.GridLayers;
import grid.GridSquare;
import help.Helper;
import help.NegCycleFilter;
import algorithms.BCDGR;
import algorithms.SIEG;
import algorithms.SIPI;
import circuit.CircuitReader;


/**
 * @author Stanimir
 *
 *Generic class used for running tests.
 */
public class TestRunner {

	public static void main(String[] args) {
		Helper help= new Helper();
		int[] numVertices={512, 1024};
//		int[] maxL={0, 0, 0, 0, 0, 0, 512, 1024, 2048, 4096, 8192, 16384};
//		int[] minL={-512, -1024, -2048, -4096, -8192, -16384, 0, 0, 0, 0, 0, 0,};
//		int[] maxP={1024, 2048, 4096, 8192, 16384};
		System.out.println("RAND5 test:");
		
//		RandP randG=new RandP(512);
//		RandL randGL=new RandL(512,5*512);
//		for (int i=0;i<maxL.length;i++){
//			System.out.println("Max="+maxL[i]+", Min="+minL[i]);
//			RandL rl=new RandL(512, 5*512);
//			Graph g=rl.generateGraph(minL[i], maxL[i]);
		for(int numV:numVertices){
			System.out.println("Num Vertices: "+numV);
//			GridSquare gs=new GridSquare(numV, help.getMinLayerWeight(), help.getMaxLayerWeight(), help.getMinIntLayerWeight(), help.getMaxIntLayerWeight());
//			Graph g= gs.generateGraph(true);
			GridLayers gl=new GridLayers(numV, help.getMinLayerWeight(), help.getMaxLayerWeight(), help.getMinIntLayerWeight(), help.getMaxIntLayerWeight());
			Rand4 randGraphs=new Rand4(numV,help.getMinRandWeight(), help.getMaxRandWeight());
//			Graph g=randGraphs.generateGraph(help.getRandEdgeMult());
//			Graph g=randGL.generateGraph(minL[i], maxL[i]);
			Graph g= gl.generateGraph(true, 32);
//			Graph g=randG.generateGraph(0, max);
//			CircuitReader cr= new CircuitReader();
//			String path="C:\\Users\\Stanimir\\Dropbox\\Year 3\\Third Year Project\\Testing\\ibm01.netD";
//			Graph g=cr.generateGraph(path);
//			System.out.println("GRIDS Graphs");
			for (int j=3; j<4; j++){
				System.out.println("Neg Cycle Type "+j);
				NegCycleFilter ncf= new NegCycleFilter(g);
				switch(j){
					case 1:ncf.add1();
						break;
					case 2:ncf.add2();
						break;
					case 3:ncf.add3();
//					case 3:;
						break;
					case 4:ncf.add4();
						break;
					case 5:ncf.add5();
				}
				Graph g1=help.applyPotentialTransformation(ncf.getG(), 1, help.getMaxPotential());
				HashMap<String, ArrayList<String>> owner= help.getVertexOwnership(g1);
				
//				//Run Brim Chaloupka
				System.out.println("Run Brim Chaloupka:");
				long durationBC=0;
				for (int k=0; k<10; k++){
					BCDGR bc= new BCDGR(g1, owner.get("min"), owner.get("max"));
					long startBC = System.currentTimeMillis();
					bc.getBatteryLevel();
					long endBC = System.currentTimeMillis();
					durationBC=durationBC+(endBC-startBC);
					System.out.println("Instance duration: "+(endBC-startBC));
				}
				durationBC=durationBC/10;
				System.out.println("BC Duration: "+durationBC);
				
//				//Run Staying Alive
//				System.out.println("Run Staying Alive:");
//				long durationSA=0;
//				for (int k=0;k<10;k++){
//					StayingAlive sa= new StayingAlive(g1, owner.get("min"), owner.get("max"));
//					long startSA = System.currentTimeMillis();
//					sa.findStrategies();
//					long endSA = System.currentTimeMillis();
//					durationSA=durationSA+(endSA-startSA);
////					System.out.println("Instance duration: "+(endSA-startSA));
//				}
//				durationSA=durationSA/10;
//				System.out.println("SA Duration: "+durationSA);
//				
				//Run Strategy Improvement MPG
//				System.out.println("Run Strategy Improvement MPG:");
//				long durationSI=0;
//				for(int k=0;k<10;k++){
//					StrategyImprovementMPG si= new StrategyImprovementMPG(g1, owner.get("min"), owner.get("max"));
//					long startSI = System.currentTimeMillis();
//					si.findStrategies();
//					long endSI = System.currentTimeMillis();
//					durationSI=durationSI+(endSI-startSI);
//					System.out.println("Instance duration: "+(endSI-startSI));
//				}
//				durationSI=durationSI/10;
//				System.out.println("SI Duration: "+durationSI);
				System.out.println();
			}	
		}
	}
}
