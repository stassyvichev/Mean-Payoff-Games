package rand;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import help.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Stanimir
 *Creates graphs from RandL family
 */
public class RandL {
	private ArrayList<String> vertices;
	private ArrayList<String> edges;
	private HashMap<String, Double> weights;
	private ArrayList<String> varEdges;
	private Helper help;
	
	/**
	 * @param numVertices
	 * @param numEdges
	 * RandL constructor
	 */
	public RandL(int numVertices, int numEdges) {
		super();
		help= new Helper();
		vertices=help.generateVertices(numVertices);
		edges=new ArrayList<String>();
		weights= new HashMap<String, Double>();
		help.generateHamiltonianCycle(numVertices, vertices, edges, weights);
		varEdges=new ArrayList<String>();
		Random randGen=new Random();
		while(edges.size()<numEdges){
			int v1=randGen.nextInt(numVertices);
			int v2=randGen.nextInt(numVertices);
			String vertex1=vertices.get(v1);
			String vertex2=vertices.get(v2);
			if(!vertex1.equals(vertex2)){
				String edge=vertex1+","+vertex2;
				if(!edges.contains(edge)){
					edges.add(edge);
					varEdges.add(edge);
				}
			}
			
		}
	}
	
	/**
	 * @return
	 * generates starting graph
	 */
	public Graph generateInitialGraph(){
		for (String e: edges){
			weights.put(e, 1d);
		}
		Graph graph= new Graph(vertices, edges, weights);
		return graph;
	}
	
	/**
	 * @param minWeight
	 * @param maxWeight
	 * @return
	 * puts weights picked from assigned weight range
	 */
	public Graph generateGraph(double minWeight, double maxWeight){
		for (String e: varEdges){
			double weight=help.generateRandomWeight(minWeight, maxWeight);
			weights.put(e, weight);
		}
		Graph graph= new Graph(vertices, edges, weights);
		return graph;
	}
	
	public static void main(String[] args) {
		System.out.println("Rand L Initial");
		RandL rl=new RandL(5,20);
		Graph g2= rl.generateInitialGraph();
		for(Vertex v: g2.getVertices().values()){
			System.out.println(v.getLabel());
		}
		for(Edge e: g2.getEdges().values()){
			System.out.println(e.getLabel()+":"+e.getWeight());
		}	
		System.out.println("Rand L actual");
		Graph g3= rl.generateGraph(-10,10);
		for(Vertex v: g3.getVertices().values()){
			System.out.println(v.getLabel());
		}
		for(Edge e: g3.getEdges().values()){
			System.out.println(e.getLabel()+":"+e.getWeight());
		}
	}
}
