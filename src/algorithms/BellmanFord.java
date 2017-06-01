package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

/**
 * @author Stanimir
 *
 *Implementation of Bellman Ford algorithm.
 */
public class BellmanFord {
	
	private Graph graph;
	
	private double[][] D;
	
	private ArrayList<String> vertexOrdering;
	
	private double infinity;
	
	private double compareInfinity;
	
	private String target;
	
	private String[] successor;
	
	private double[] distance;
	
	/**
	 * @param graph
	 * @param source
	 * 
	 * constructor that sets up the algorithm
	 */
	public BellmanFord(Graph graph, String target) {
		super();
		this.target = target;
		this.graph = graph;
		double MAX=this.graph.getLargesAbsoluteWeight()*this.graph.getVertices().size();
		this.infinity=Double.MAX_VALUE-MAX;
		this.compareInfinity=this.infinity/2;
		setVertexOrdering(new ArrayList<String>());
		getVertexOrdering().addAll(this.graph.getVertices().keySet());
		successor=new String[this.graph.getVertices().size()];
		this.D=new double[this.graph.getVertices().size()][this.graph.getVertices().size()];
		for (String v: getVertexOrdering()){
			int index= getVertexOrdering().indexOf(v);
			if (v.equalsIgnoreCase(this.target)){
				this.D[index][0]=0;
				successor[index]=target;
			}else{
				successor[index]=null;
				this.D[index][0]=infinity;
			}
		}
	}
	
	
	public String getTarget() {
		return target;
	}

	
	public void setTarget(String target) {
		this.target = target;
	}

	
	public double[][] getD() {
		return D;
	}

	
	public String[] getSuccessor() {
		return successor;
	}

	/**
	 * finds all shortest paths by running Bellman Ford and relaxing vertices incrementally
	 */
	public void findSP(){
		for (int i=1; i<graph.getVertices().size(); i++){
			for (String v: vertexOrdering){
				Vertex vertex=graph.getVertices().get(v);
				int vIndex=getIndexOf(v);
				ArrayList<String> outwardEdges=vertex.getCollection().getOutwards();
				relax(outwardEdges, vIndex, i);
			}
		}
	}
	
	/**
	 * @param outwardEdges
	 * @param vIndex
	 * @param i
	 * 
	 * relaxes a single vertex by calculating what its new shortest path should be 
	 * and what its successor is
	 */
	private void relax(ArrayList<String> outwardEdges, int vIndex, int i){
		D[vIndex][i]=D[vIndex][i-1];
		for (String e: outwardEdges){
			Vertex v= graph.getEdges().get(e).getVEnd();
			int verIndex=getIndexOf(v.getLabel());
			double newValue=graph.getEdges().get(e).getWeight()+D[verIndex][i-1];
			if (newValue<D[vIndex][i]){
				D[vIndex][i]=newValue;
				successor[vIndex]=v.getLabel();
			}
		}
	}
	
	/**
	 * @return
	 * checks after the algorithm is complete if there are any negative cycles
	 */
	public boolean check(){
		for (Edge e: graph.getEdges().values()){
			int indexStart=getVertexOrdering().indexOf(e.getVStart().getLabel());
			int indexEnd=getVertexOrdering().indexOf(e.getVEnd().getLabel());
			if (distance[indexStart]+e.getWeight()<distance[indexEnd]){
				return false;
			}
		}
		return true;
	}
	
	
	public Graph getGraph() {
		return graph;
	}

	
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	

	
	public double[] getDistance() {
		return distance;
	}

	
	public void setDistance(double[] distance) {
		this.distance = distance;
	}	

	public ArrayList<String> getVertexOrdering() {
		return vertexOrdering;
	}

	public void setVertexOrdering(ArrayList<String> vertexOrdering) {
		this.vertexOrdering = vertexOrdering;
	}
	
	/**
	 * @return
	 * determines which vertices have shortest paths of length less than infinity. 
	 */
	public ArrayList<String> getReachableVertices(){
		ArrayList<String> rv= new ArrayList<String>();
		int n=vertexOrdering.size();
		for (String v: vertexOrdering){
			if (!(D[getIndexOf(v)][n-1]>=compareInfinity)){
				rv.add(v);
			}
		}
		return rv;
	}
	
	public String getVerSuccessor(String v){
		return successor[getIndexOf(v)];
	}
	
	public int getIndexOf(String v){
		return vertexOrdering.indexOf(v);
	}
	public static void main(String[] args) {
		String[] vertices={"a", "b", "c", "d", "e", "f","s"};
		String[] edges= {"a,b","b,a","b,c","c,d","c,e","d,b","d,f","e,e","e,f","f,e","f,d",
				"s,a","s,b","s,c","s,d","s,e","s,f"};
		HashMap<String,Double> weights= new HashMap<String, Double>();
		weights.put("a,b", 2d);
		weights.put("b,a", -2d);
		weights.put("b,c", -4d);
		weights.put("c,d", -4d);
		weights.put("c,e", -4d);
		weights.put("d,b", 8d);
		weights.put("d,f", 8d);
		weights.put("e,e", 2d);
		weights.put("e,f", 2d);
		weights.put("f,e", -1d);
		weights.put("f,d", -1d);
		weights.put("s,a", 0d);
		weights.put("s,b", 0d);
		weights.put("s,c", 0d);
		weights.put("s,d", 0d);
		weights.put("s,e", 0d);
		weights.put("s,f", 0d);
		Graph g= new Graph(vertices, edges, weights); 
		System.out.println("Running Bellman Ford on small sample graph with vertices:");
		System.out.println(g.getVertices().keySet());
		System.out.println("and edges:");
		System.out.println(g.getEdges().keySet());
		System.out.println("with weights:");
		System.out.println(weights);
		System.out.println("Starting algorithm.");
		BellmanFord bf= new BellmanFord(g, "e");
		bf.findSP();
		System.out.println("Results (Format is vertex: shortest path to source s, successor in path):");
		for(String v: bf.getGraph().getVertices().keySet()){
			System.out.println(v+": "+bf.D[bf.getIndexOf(v)][bf.getGraph().getVertices().size()-1]+", "+bf.successor[bf.getIndexOf(v)]);
		}
	}


}
