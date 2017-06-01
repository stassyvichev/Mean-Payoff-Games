package rand;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import help.Helper;
import help.NegCycleFilter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Stanimir
 *Class for generating Rand5 graphs
 */
public class Rand4 {

	private int numVertices;

	private int numEdges;

	private double maxWeight;

	private double minWeight;

	private Helper help;
	/**
	 * @param numVertices
	 * @param maxWeight
	 * @param minWeight
	 * Generator
	 */
	public Rand4(int numVertices, double minWeight,
			double maxWeight) {
		super();
		help= new Helper();
		this.numVertices = numVertices;
		this.maxWeight = maxWeight;
		this.minWeight = minWeight;
	}
	
	/**
	 * @param numEdgeCompound
	 * @return
	 * generates Rand5 graph
	 */
	public Graph generateGraph(double numEdgeCompound){
		numEdges=(int) (numVertices*numEdgeCompound);
		ArrayList<String> vertices=help.generateVertices(numVertices);
		ArrayList<String> edges=new ArrayList<String>();
		HashMap<String, Double> weights= new HashMap<String, Double>();
		help.generateHamiltonianCycle(numVertices, vertices, edges, weights);
		while(edges.size()<numEdges){
			int v1=help.generateRandInt(numVertices);
			int v2=help.generateRandInt(numVertices);
			String vertex1=vertices.get(v1);
			String vertex2=vertices.get(v2);
			if(!vertex1.equals(vertex2)){
				String edge=vertex1+","+vertex2;
				if(!edges.contains(edge)){
					edges.add(edge);
					double weight= help.generateRandomWeight(minWeight, maxWeight);
					weights.put(edge,weight);
				}
			}
		}
		Graph graph= new Graph(vertices, edges, weights);
		return graph;
	}
	
	/**
	 * @return
	 * generates more dense graph
	 */
	public Graph generateDenseGraph(){
		int numEdgeCompound=numVertices/4;
		return generateGraph(numEdgeCompound);
	}
	
	public static void main(String[] args) {
		System.out.println("Rand5 Graphs");
		Rand4 r4= new Rand4(20, 1d, 1000d);
		Graph g= r4.generateGraph(5);
		System.out.println("Rand 5");
		for(Vertex v: g.getVertices().values()){
			System.out.println(v.getLabel());
		}
		for(Edge e: g.getEdges().values()){
			System.out.println(e.getLabel()+":"+e.getWeight());
		}
		System.out.println(g.getEdges().size());
		Helper help=new Helper();
		HashMap<String, ArrayList<String>> owner= help.getVertexOwnership(g);
		System.out.println(owner);
	}
}
