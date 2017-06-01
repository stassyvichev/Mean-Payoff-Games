package help;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Stanimir
 *Helper class that has common methods used in generating graphs
 */
public class Helper {

	private Random rand;

	private final int minRandWeight=1;

	private final int maxRandWeight=1000;

	private final int randEdgeMult=5;

	private final int minLayerWeight=1;

	private final int maxLayerWeight=100;

	private final int minIntLayerWeight=1000;

	private final int maxIntLayerWeight=10000;

	private final int minForwardWeight=1;

	private final int maxForwardWeight=10000;

	private final int maxPotential=16384;
	public Helper() {
		super();
		rand=new Random();
	}
	
	/**
	 * @param numVertices
	 * @return
	 * generates a number of random vertices
	 */
	public ArrayList<String> generateVertices(int numVertices){
		VertexNameGenerator vng=new VertexNameGenerator(4);
		ArrayList<String> vertices=new ArrayList<String>();
		while(vertices.size()<numVertices){
			String name=vng.getRandomName();
			if (!vertices.contains(name)){
				vertices.add(name);
			}
		}
		return vertices;
	}
	
	/**
	 * @param numVetrices
	 * @param vertices
	 * @param edges
	 * @param weights
	 * Generates a hamiltonian cycle
	 */
	public void generateHamiltonianCycle(int numVetrices, ArrayList<String> vertices, ArrayList<String> edges, HashMap<String, Double> weights){
		String start=vertices.get(0);
		String next=start;
		for (int i=1; i<numVetrices; i++){
			String vertex=vertices.get(i);
			String edge=next+","+vertex;
			edges.add(edge);
			weights.put(edge, 1d);
			next=vertex;
		}
		String lastEdge=next+","+start;
		edges.add(lastEdge);
		weights.put(lastEdge, 1d);
	}
	
	/**
	 * @param min
	 * @param max
	 * @return
	 * Generates a random weight
	 */
	public double generateRandomWeight(double min, double max){
		if (min<0){
			double weight=rand.nextInt((int)(Math.abs(max)+Math.abs(min)))+min;
			return weight;
		}else{
			double weight=rand.nextInt((int)(Math.abs(max)-Math.abs(min)))+min;
			return weight;
		}
	}
	
	public int generateRandInt(int max){
		return rand.nextInt(max);
	}
	
	/**
	 * @param graph
	 * @param minPotential
	 * @param maxPotential
	 * @return
	 * makes a potential transformation
	 */
	public Graph applyPotentialTransformation(Graph graph, int minPotential, int maxPotential){
		Graph g=new Graph(graph);
		HashMap<String,Integer> potentials=new HashMap<String, Integer>();
		Random randGen= new Random();
		for(String v: g.getVertices().keySet()){
			int potential= randGen.nextInt(Math.abs(maxPotential)+Math.abs(minPotential))+minPotential;
			potentials.put(v, potential);
		}
		for (Edge e: g.getEdges().values()){
			double old=e.getWeight();
			String vStart=e.getVStart().getLabel();
			String vEnd=e.getVEnd().getLabel();
			int potStart=potentials.get(vStart);
			int potEnd=potentials.get(vEnd);
			double newWeight=old+potStart-potEnd;
			g.changeEdgeWeight(e.getLabel(), newWeight);
		}
		return g;
	}
	
	/**
	 * @param g
	 * @return
	 * randomizes ownership of vertices
	 */
	public HashMap<String, ArrayList<String>> getVertexOwnership(Graph g){
		ArrayList<String> vMin= new ArrayList<String>();
		ArrayList<String> vMax= new ArrayList<String>();
		ArrayList<String> ordered= new ArrayList<String>();
		int i=0;
		while(ordered.size()<g.getVertices().size()){
			int coin=generateRandInt(g.getVertices().size());
			String v= (String) g.getVertices().keySet().toArray()[coin];
			if(!ordered.contains(v)){
				ordered.add(v);
				if(i%2==0){
					vMin.add(v);
				}else{
					vMax.add(v);
				}
				i++;
			}
		}
		HashMap<String, ArrayList<String>> owner= new HashMap<String, ArrayList<String>>();
		owner.put("min", vMin);
		owner.put("max", vMax);
		return owner;
	}

	public int getMinRandWeight() {
		return minRandWeight;
	}

	public int getMaxRandWeight() {
		return maxRandWeight;
	}

	public int getRandEdgeMult() {
		return randEdgeMult;
	}

	public int getMinLayerWeight() {
		return minLayerWeight;
	}

	public int getMaxLayerWeight() {
		return maxLayerWeight;
	}

	public int getMinIntLayerWeight() {
		return minIntLayerWeight;
	}

	public int getMaxIntLayerWeight() {
		return maxIntLayerWeight;
	}

	public int getMinForwardWeight() {
		return minForwardWeight;
	}

	public int getMaxForwardWeight() {
		return maxForwardWeight;
	}

	public int getMaxPotential() {
		return maxPotential;
	}
}
