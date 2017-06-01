package rand;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import help.Helper;

import rand.Rand4;

/**
 * @author Stanimir
 *Generates RandP graphs
 */
public class RandP {
	private Graph initialGraph;
	private Helper help;
	/**
	 * @param numVertices
	 * RandP Constructor
	 */
	public RandP(int numVertices) {
		super();
		help=new Helper();
		Rand4 r4=new Rand4(numVertices, help.getMinRandWeight(), help.getMaxRandWeight());
		initialGraph=r4.generateGraph(help.getRandEdgeMult());
		
	}
	
	/**
	 * @param minPotential
	 * @param maxPotential
	 * @return
	 * generate randP graph
	 */
	public Graph generateGraph(int minPotential, int maxPotential){
		return help.applyPotentialTransformation(initialGraph, minPotential, maxPotential);
	}
	public static void main(String[] args) {
		System.out.println("Rand P");
		RandP rP=new RandP(10);		
		Graph g4= rP.generateGraph(0,10);
		for(Vertex v: g4.getVertices().values()){
			System.out.println(v.getLabel());
		}
		for(Edge e: g4.getEdges().values()){
			System.out.println(e.getLabel()+":"+e.getWeight());
		}
	}
}
