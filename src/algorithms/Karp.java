package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import graph.Graph;
import graph.Vertex;


/**
 * @author Stanimir
 *
 *Implementation of Karp's algorithm for finding minimum mean cycles.
 */
public class Karp {

	private Graph graph;

	private double[][] D;

	private ArrayList<String> vertexOrdering;

	private int infinity=Integer.MAX_VALUE-1000;

	private String source;

	private String minVertex;

	private String[][] S;
	
	/**
	 * @param graph
	 * @param source
	 * 
	 * sets up the Karp algorithm
	 */
	public Karp(Graph graph, String source) {
		super();
		this.graph =graph;
		this.source=source;
		addSource();
		this.D=new double[this.graph.getVertices().size()][this.graph.getVertices().size()+1];
		this.S=new String[this.graph.getVertices().size()][this.graph.getVertices().size()+1];
		this.vertexOrdering=new ArrayList<String>();
		this.vertexOrdering.addAll(this.graph.getVertices().keySet());
		for (String v: this.vertexOrdering){
			int index= this.vertexOrdering.indexOf(v);
			this.D[index][0]=infinity;
			this.S[index][0]=source;
			if (v.equalsIgnoreCase(source)){
				this.D[index][0]=0;
			}
		}
	}


	public String[][] getS() {
		return S;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public double[][] getD() {
		return D;
	}

	public ArrayList<String> getVertexOrdering() {
		return vertexOrdering;
	}

	/**
	 * finds shortest paths from a source to all vertices
	 * Works like Bellman Ford.
	 */
	public void findSP(){
		for (int i=1; i<graph.getVertices().size()+1; i++){
			for (String v: vertexOrdering){
				Vertex vertex=graph.getVertices().get(v);
				int vIndex=vertexOrdering.indexOf(v);
				ArrayList<String> inwardEdges=vertex.getCollection().getInwards();
				double newVal=getMinPredecessor(inwardEdges, i-1);
				String predecessor=getPredecessor(inwardEdges, i-1);
				D[vIndex][i]=newVal;
				S[vIndex][i]=predecessor;
			}
		}
	}
	
	/**
	 * @param inwardEdges
	 * @param index
	 * @return
	 * calculates shortest path so far to a specific vertex, used by findSP()
	 */
	private double getMinPredecessor(ArrayList<String> inwardEdges, int index){
		double min=infinity;
		for (String e: inwardEdges){
			if((graph.getEdges().get(e)==null)){
				System.out.println("Null is: "+e);
			}
			Vertex v= graph.getEdges().get(e).getVStart();
			int vIndex=vertexOrdering.indexOf(v.getLabel());
			double newValue=graph.getEdges().get(e).getWeight()+D[vIndex][index];
			if (newValue<min){
				min=newValue;
			}
		}
		return min;
	}
	
	/**
	 * @param inwardEdges
	 * @param index
	 * @return
	 * finds the predecessor of a vertex on its shortest path so far. used in findSP()
	 */
	private String getPredecessor(ArrayList<String> inwardEdges, int index){
		double min=infinity;
		String predecessor=null;
		for (String e: inwardEdges){
			Vertex v= graph.getEdges().get(e).getVStart();
			int vIndex=vertexOrdering.indexOf(v.getLabel());
			double newValue=graph.getEdges().get(e).getWeight()+D[vIndex][index];
			if (newValue<min){
				min=newValue;
				predecessor=v.getLabel();
			}
		}
		return predecessor;
	}
	
	/**
	 * adds a source vertex to graph that has an outgoing edge to every vertex
	 */
	private void addSource(){
		graph.insertVertex(source);
		for (Object v: graph.getVertices().keySet()){
			if(!v.equals(source)){
				String edge=source+","+v;
				graph.insertEdge(edge, 0);
			}
		}
	}
	
	/**
	 * @return
	 * 
	 * finds the value of the minimum cycle mean.
	 */
	public double calculateKarp(){
		int n= graph.getVertices().size();
		double min=infinity;
		for(String v: vertexOrdering){
			int vIndex=vertexOrdering.indexOf(v);
			double max=-infinity;
			for (int k=0; k<n; k++){
				double denom= n-k;
				double numerator=D[vIndex][n]-D[vIndex][k];
				double result= numerator/denom;
				if (result>max){
					max=result;
				}
			}
			if (max<min){
				minVertex=v;
				min=max;
			}
		}
		return min;
	}
	

	public String getMinVertex() {
		return minVertex;
	}
	
	public String[] extractShortestPath(){
		String mv= minVertex;
		int n=graph.getVertices().size();
		String[] path=new String[n+1];
		path[n]=source;
		for (int i=0; i<n;i++){
			path[i]=mv;
			mv=S[vertexOrdering.indexOf(mv)][n];
		}
		return path;
	}
	
	/**
	 * @return
	 * returns actual minimum mean cycle
	 */
	public ArrayList<String> extractCycle(){
		String mv= minVertex;
		int n=graph.getVertices().size();
		String[] path=new String[n+1];
		path[n]=source;
		ArrayList<String> cycle=new ArrayList<String>();
		ArrayList<String> counter=new ArrayList<String>();
		for (int i=0; i<n;i++){
			path[i]=mv;
			mv=S[vertexOrdering.indexOf(mv)][n];
			if(mv==null){
				return new ArrayList<String>();
			}
			if (counter.indexOf(mv)==-1){
				counter.add(mv);
			}else{
				int cIndex=counter.indexOf(mv);
				cycle.addAll(counter.subList(cIndex, counter.size()));
				return cycle;
			}
		}
		return null;
	}
	
	/**
	 * @return
	 * returns actual minimum mean cycle
	 */
	public ArrayList<String> findMinCycle(){
		String[] path=extractShortestPath();
		ArrayList<String> cycle=new ArrayList<String>();
		ArrayList<String> counter=new ArrayList<String>();
		for (String v:path){
			if (counter.indexOf(v)==-1){
				counter.add(v);
			}else{
				int cIndex=counter.indexOf(v);
				cycle.addAll(counter.subList(cIndex, counter.size()));
				return cycle;
			}
		}
		return null;
	}
	
	/**
	 * @return
	 * picks out the shortest path for each vertex 
	 * out of the shortest paths that use different numbers of edges.
	 */
	public HashMap<String, Double> getBestPaths(){
		HashMap<String,Double> minSP=new HashMap<String,Double>();
		for (String v: this.getGraph().getVertices().keySet()){
			int vIndex= this.getVertexOrdering().indexOf(v);
			double min=this.getD()[vIndex][0];
			minSP.put(v,min);
			for (int i=0; i< this.getGraph().getVertices().size()+1; i++){
				if (this.getD()[vIndex][i]<min){
					minSP.put(v, this.getD()[vIndex][i]);
				}
			}
		}
		return minSP;
	}
	
	/**
	 * @param minSP
	 * @return
	 * 
	 * gets all vertices to which there is a shortest path of finite length
	 */
	public ArrayList<String> getReachableVertices(HashMap<String, Double> minSP){
		ArrayList<String> rv= new ArrayList<String>();
		for (String v: minSP.keySet()){
			if (!(minSP.get(v)>=infinity)){
				rv.add(v);
			}
		}
		return rv;
	}
	
	/**
	 * @return
	 * extracts shortest paths
	 */
	public HashMap<String, Integer> extractSP(){
		HashMap<String,Integer> minSP=new HashMap<String,Integer>();
		for (String v: this.getGraph().getVertices().keySet()){
			int vIndex= this.getVertexOrdering().indexOf(v);
			double min=this.getD()[vIndex][0];
			minSP.put(v, 0);
			for (int i=0; i< this.getGraph().getVertices().size()+1; i++){
				if (this.getD()[vIndex][i]<min){
					minSP.put(v, i);
				}
			}
		}
		return minSP;
	}
	public static void main(String[] args) {
		String[] vertices={"a", "b", "c", "d", "e", "f","s"};
		String[] edges= {"a,b","b,a","b,c","c,d","c,e","d,b","d,f","e,e","e,f","f,e","f,d"};
		HashMap<String,Double> weights= new HashMap<String, Double>();
		weights.put("a,b", 2d);
		weights.put("b,a", -4d);
		weights.put("b,c", -4d);
		weights.put("c,d", -4d);
		weights.put("c,e", -4d);
		weights.put("d,b", 8d);
		weights.put("d,f", 8d);
		weights.put("e,e", 2d);
		weights.put("e,f", 2d);
		weights.put("f,e", -1d);
		weights.put("f,d", -1d);
		Graph g= new Graph(vertices, edges, weights);
		System.out.println("Running Karp on small sample graph with vertices:");
		System.out.println(g.getVertices().keySet());
		System.out.println("and edges:");
		System.out.println(g.getEdges().keySet());
		System.out.println("with weights:");
		System.out.println(weights);
		System.out.println("Starting algorithm.");
		Karp bf= new Karp(g, "s");
		bf.findSP();
		double min= bf.calculateKarp();
		System.out.println("Minimum cycle mean: "+min);
		ArrayList<String> cycle=bf.findMinCycle();
		System.out.print("Actual cycle: ");
		for (String c: cycle){
			System.out.print(c+",");
		}
	}

}
