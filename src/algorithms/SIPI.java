package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class SIPI {
	private static final String MAX="Max";
	private static final String MIN="Min";

	private HashMap<String, String> vertexOwner;

	private Graph graph;

	private ArrayList<String> vertexOrdering;

	private HashMap<String,String> minStrategy;

	private HashMap<String,String> maxStrategy;

	private HashMap<String,Double> gain;

	private HashMap<String,Double> bias;

	private ArrayList<String> minMeanCycle;

	private double minMeanCycleVal;

	private double infinity;

	private ArrayList<String> vMin;

	private ArrayList<String> vMax;

	/**
	 * @param graph
	 * @param vMin
	 * @param vMax
	 * 
	 * sets up the SIPI algorithm
	 */
	public SIPI(Graph graph, ArrayList<String> vMin, ArrayList<String> vMax) {
		super();
		this.graph = new Graph(graph);
		double MAXVAL=this.graph.getLargesAbsoluteWeight()*this.graph.getVertices().size();
		this.infinity=Double.NEGATIVE_INFINITY+MAXVAL;
		this.minStrategy=new HashMap<String,String>();
		this.maxStrategy=new HashMap<String,String>();
		this.gain=new HashMap<String,Double>();
		this.bias=new HashMap<String,Double>();
		this.vertexOrdering=new ArrayList<String>();
		this.vertexOrdering.addAll(this.graph.getVertices().keySet());
		this.vertexOwner= new HashMap<String, String>();
		this.vMin=vMin;
		this.vMax=vMax;
		for (String vertex: vMin){
			this.vertexOwner.put(vertex, MIN);
			this.gain.put(vertex, infinity);
			this.bias.put(vertex, infinity);
		}
		for (String vertex: vMax){
			this.vertexOwner.put(vertex, MAX);
			this.gain.put(vertex, infinity);
			this.bias.put(vertex, infinity);
		}
	}
	
	
	/**
	 * @param graphX
	 * 
	 * uses Karp class to find the minimum mean cycle of graph
	 */
	public void findMinMeanCycle(Graph graphX){
		Karp bf= new Karp(graphX, "s");
		bf.findSP();
		minMeanCycleVal=bf.calculateKarp();
		minMeanCycle=bf.extractCycle();
	}
	
	/**
	 * @param graphSize
	 * @return
	 * 
	 * finds the vertex in the minimum mean cycle with smallest index
	 */
	public String getMinOrderV(int graphSize){
		int min=graphSize;
		String minV=null;
		for (String v: minMeanCycle){
			if(vertexOrdering.indexOf(v)<=min){
				min=vertexOrdering.indexOf(v);
				minV=v;
			}
		}
		return minV;
	}
	
	/**
	 * @param graphX
	 * @return
	 * 
	 * refactors the graph so that the edge weights become weight-minimumCycleMean
	 */
	public Graph refactorGraph(Graph graphX){
		Graph g= new Graph(graphX);
		for (Edge e: g.getEdges().values()){
			double newWeight=e.getWeight()-getMinMeanCycleVal();
			g.changeEdgeWeight(e.getLabel(), newWeight);
		}
		return g;
	}
	
	/**
	 * @param bf
	 * @param reachable
	 * @param minOrderV
	 * 
	 * Compute Min's strategy
	 * uses Bellman Ford
	 */
	public void findMinStrategy( BellmanFord bf, ArrayList<String> reachable, String minOrderV){
		for (String v: reachable){
			if (isMin(v)){
				if(v.equals(minOrderV)){
					ArrayList<String> succEdges=graph.getVertices().get(v).getCollection().getOutwards();
					for (String succEdge: succEdges){
						String succ=succEdge.split(",")[1];
						if (minMeanCycle.contains(succ)){
							minStrategy.put(v,succ);
						}
					}
				}else{
					minStrategy.put(v, bf.getVerSuccessor(v));
				}
			}
		}
	}
	
	/**
	 * @param bf
	 * @param reachable
	 * calculates Gain and Bias for each vertex from which there is a path to the minimum mean cycle.
	 */
	public void calculateGainBias( BellmanFord bf, ArrayList<String> reachable){
		int n=reachable.size();
		for (String v: reachable){
			gain.put(v, minMeanCycleVal);
			bias.put(v, bf.getD()[bf.getIndexOf(v)][n-1]);
		}
	}
	
	/**
	 * @return
	 * computes graph Gx based on Max's strategy (every Max vertex has only one outgoing edge)
	 */
	public Graph computeGraphX(){
		Graph graphX=new Graph(graph);
		for (Edge e: graph.getEdges().values()){
			String startL=e.getVStart().getLabel();
			String endL=e.getVEnd().getLabel();
			if(!isMin(startL)){
				if(!endL.equals(maxStrategy.get(startL))){
					graphX.removeEdge(e.getLabel());
				}
			}
		}
		return graphX;
	}
	
	/**
	 * @param graph
	 * @param remainingV
	 * @return
	 * computes sub-graph of original graph which consists of vertices with no paths to current minimum mean cycle.
	 */
	public Graph computeSubGraph(Graph graph, ArrayList<String> remainingV){
		Graph subGraph=new Graph(graph);
		for (String v: graph.getVertices().keySet()){
			if (!remainingV.contains(v)){
				subGraph.removeVertex(v);
			}
		}
		return subGraph;
	}
	/**
	 * Computes optimal Min Strategy and Max Strategy
	 */
	public void findStrategies(){
		computeArbitraryMaxStrategy();
		boolean maxSChanged=true;
		while(maxSChanged){
			Graph graphX=computeGraphX();
			ArrayList<String> remainingVertices=new ArrayList<String>();
			remainingVertices.addAll(graph.getVertices().keySet());
			while(remainingVertices.size()>1){
				Graph subGraphX=this.computeSubGraph(graphX, remainingVertices);
				findMinMeanCycle(subGraphX);
				Graph refactored=refactorGraph(subGraphX);
				String minOrderV=getMinOrderV(subGraphX.getVertices().size());
				if(minOrderV==null){
					remainingVertices.clear();
					break;
				}
				BellmanFord bf=new BellmanFord(refactored, minOrderV);
				bf.findSP();
				ArrayList<String> reachable=bf.getReachableVertices();
				findMinStrategy(bf, reachable, minOrderV);
				calculateGainBias(bf, reachable);
				remainingVertices.removeAll(reachable);
			}
			maxSChanged=improveMaxStrategy();
		}
		
	}
	
	/**
	 * computes an arbitrary (random) Strategy for Max
	 */
	public void computeArbitraryMaxStrategy(){
		for (String vS: vMax){
			Vertex v=graph.getVertices().get(vS);
			if (v.getCollection().getOutwards().size()>0){
				String succEdge=v.getCollection().getOutwards().get(0);
				String succ=succEdge.split(",")[1];
				maxStrategy.put(v.getLabel(), succ);
			}else{
				maxStrategy.put(v.getLabel(), v.getLabel());
			}
		}
	}
	
	/**
	 * @return
	 * improve Max's strategy using Gain and Bias
	 */
	private boolean improveMaxStrategy() {
		boolean changed=false;
		for (String vS: vMax){
				Vertex v=graph.getVertices().get(vS);
				ArrayList<String> outwards=v.getCollection().getOutwards();
				String currentSucc=maxStrategy.get(v.getLabel());
				for (String edge: outwards){
					Vertex vEnd=graph.getEdges().get(edge).getVEnd();
					if (vEnd.getLabel().equals(currentSucc)){
						continue;
					}
					if (gain.get(vEnd.getLabel())>gain.get(v.getLabel())){
						if(gain.get(vEnd.getLabel())>gain.get(currentSucc)){
							maxStrategy.put(v.getLabel(), vEnd.getLabel());
							changed=true;
						}
					}else if((double)gain.get(vEnd.getLabel())==(double)gain.get(v.getLabel())){
						double biasV=bias.get(v.getLabel());
						double edgeW=graph.getEdges().get(edge).getWeight();
						double biasVEnd=bias.get(vEnd.getLabel())+edgeW-gain.get(v.getLabel());
						if (biasVEnd>biasV){
							String currEdge=v.getLabel()+","+currentSucc;
							double currEWeight=graph.getEdges().get(currEdge).getWeight();
							double biasVCurrent=bias.get(currentSucc)+currEWeight-gain.get(v.getLabel());
							if (biasVEnd>biasVCurrent){
								maxStrategy.put(v.getLabel(), vEnd.getLabel());
								changed=true;
							}
						}
					}
				}
			}
		return changed;
	}
	
	/**
	 * @param v
	 * @return
	 * check if vertex belongs to Min
	 */
	public boolean isMin(String v){
		return vMin.contains(v);
	}
	
	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public ArrayList<String> getVertexOrdering() {
		return vertexOrdering;
	}

	public void setVertexOrdering(ArrayList<String> vertexOrdering) {
		this.vertexOrdering = vertexOrdering;
	}

	public ArrayList<String> getMinMeanCycle() {
		return minMeanCycle;
	}

	public void setMinMeanCycle(ArrayList<String> minMeanCycle) {
		this.minMeanCycle = minMeanCycle;
	}

	public double getMinMeanCycleVal() {
		return minMeanCycleVal;
	}

	public void setMinMeanCycleVal(double minMeanCycleVal) {
		this.minMeanCycleVal = minMeanCycleVal;
	}

	public HashMap<String,String> getMinStrategy() {
		return minStrategy;
	}

	public HashMap<String,String> getMaxStrategy() {
		return maxStrategy;
	}

	public static void main(String[] args) {
		String[] vertices={"a", "b", "c", "d", "e", "f"};
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
		ArrayList<String> vMax=new ArrayList<String>();
		ArrayList<String> vMin=new ArrayList<String>();
		vMax.add("a");
		vMax.add("c");
		vMax.add("e");
		vMin.add("b");
		vMin.add("d");
		vMin.add("f");
		System.out.println("Running SI/PI on small sample graph with vertices:");
		System.out.println(g.getVertices().keySet());
		System.out.println("and edges:");
		System.out.println(g.getEdges().keySet());
		System.out.println("Starting algorithm.");
		System.out.println("with weights:");
		System.out.println(weights);
		System.out.println("Starting algorithm.");
		SIPI si=new SIPI(g, vMin, vMax);
		si.findStrategies();
		System.out.println("Strategy for Min: "+si.minStrategy);
		System.out.println("Strategy for Max: "+si.maxStrategy);
		System.out.println("Gain (format is vertex:gain): "+si.gain);
		System.out.println("Bias (format is vertex:bias): "+si.bias);
	}

}
