package algorithms;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Stanimir
 *
 *Implementation of the SIEG algorithm for solving EGs.
 */
public class SIEG {
	private static final String MAX="Max";
	private static final String MIN="Min";

	private HashMap<String, String> vertexOwner;

	private Graph graph;

	private ArrayList<String> vertexOrdering;

	private HashMap<String,String> minStrategy;

	private HashMap<String,String> maxStrategy;

	private HashMap<String, Double> battery;

	/**
	 * @param graph
	 * @param vMin
	 * @param vMax
	 * 
	 * sets up SIEG algorithm
	 */
	public SIEG(Graph graph, ArrayList<String> vMin, ArrayList<String> vMax) {
		super();
		this.graph = new Graph(graph);
		this.minStrategy=new HashMap<String,String>();
		this.maxStrategy=new HashMap<String,String>();
		this.vertexOrdering=new ArrayList<String>();
		this.vertexOrdering.addAll(this.graph.getVertices().keySet());
		this.vertexOwner= new HashMap<String, String>();
		this.battery= new HashMap<String, Double>();
		for (String vertex: vMin){
			this.vertexOwner.put(vertex, MIN);
			this.battery.put(vertex, 0d);
		}
		for (String vertex: vMax){
			this.vertexOwner.put(vertex, MAX);
			this.battery.put(vertex, 0d);
		}
	}
	
	/**
	 * computes an arbitrary (random) strategy for Min
	 */
	public void computeArbitraryMinStrategy(){
		for (Vertex v: graph.getVertices().values()){
			if(isMin(v.getLabel())){
				if (v.getCollection().getOutwards().size()>0){
					String succEdge=v.getCollection().getOutwards().get(0);
					String succ=succEdge.split(",")[1];
					minStrategy.put(v.getLabel(), succ);
				}else{
					minStrategy.put(v.getLabel(), v.getLabel());
				}
			}
		}
	}
	
	/**
	 * @return
	 * 
	 * computes graph Gx based on Min's strategy (every Min vertex has only one outgoing edge)
	 */
	public Graph computeGraphX(){
		Graph graphX=new Graph(graph);
		for (Edge e: graph.getEdges().values()){
			String startL=e.getVStart().getLabel();
			String endL=e.getVEnd().getLabel();
			if(isMin(startL)){
				if(!endL.equals(minStrategy.get(startL))){
					graphX.removeEdge(e.getLabel());
				}
			}
		}
		return graphX;
	}
	
	/**
	 * finds the optimal strategies for Min and Max
	 */
	public void findStrategies(){
		computeArbitraryMinStrategy();
		boolean minSChanged=true;
		while(minSChanged){
			Graph graphX=computeGraphX();
			findMaxBattery(graphX);
			minSChanged=improveMinStrategy();
		}
	}
	
	/**
	 * @return
	 * Improves Min's strategy after improvement step
	 */
	private boolean improveMinStrategy() {
		boolean changed=false;
		HashMap<String, String> oldMin=(HashMap<String, String>) minStrategy.clone();
		for (Vertex vStart: graph.getVertices().values()){
			if(isMin(vStart.getLabel())){
				for (String e: vStart.getCollection().getOutwards()){
					String vEnd=e.split(",")[1];
					double weight=graph.getEdges().get(e).getWeight();
					if (battery.get(vStart.getLabel())<(battery.get(vEnd)-weight)){
						if(!minStrategy.get(vStart.getLabel()).equals(vEnd)){
							minStrategy.put(vStart.getLabel(), vEnd);
						}
					}
				}
			}
		}
		if(!oldMin.equals(minStrategy)){
			changed=true;
		}
		return changed;
	}

	/**
	 * @param graphX
	 * 
	 * improvement step: finds battery levels Max needs to survive current strategy of Min.
	 * Works by looking for the set of vertices with battery level zero. 
	 * Uses ModDijkstra.
	 */
	public void findMaxBattery(Graph graphX){
		ArrayList<String> zeroSet=computeInitialZeroSet(graphX);
		boolean changed=true;
		HashMap<String, Double> d=new HashMap<String, Double>();
		for (Vertex v: graphX.getVertices().values()){
			d.put(v.getLabel(), 0d);
		}
		while (changed){
			ModDijkstra md= new ModDijkstra(graphX, zeroSet, d);
			md.findDistances();
			d=md.getD0();
			ArrayList<String> newZeroSet= new ArrayList<String>();
			for (String v: zeroSet){
				double max=Double.NEGATIVE_INFINITY;
				ArrayList<String> outward= graphX.getVertices().get(v).getCollection().getOutwards();
				for (String e: outward){
					double weight= graphX.getEdges().get(e).getWeight();
					double val=weight-d.get(v)+d.get(e.split(",")[1]);
					if (val>max){
						max=val;
					}
				}
				if (max>=0){
					newZeroSet.add(v);
				}
			}
			if(zeroSet.equals(newZeroSet)){
				changed=false;
			}
			zeroSet=newZeroSet;
		}
		for( String v: graphX.getVertices().keySet()){
			if (zeroSet.contains(v)){
				battery.put(v, 0d);
			}else{
				battery.put(v, -d.get(v));
			}
		}
	}
	
	/**
	 * @param graphX
	 * @return
	 * 
	 * computes the initial over estimation of the set of vertices with zero battery levels
	 */
	public ArrayList<String> computeInitialZeroSet(Graph graphX){
		ArrayList<String> zeroSet=new ArrayList<String>();
		for (Edge e:graphX.getEdges().values()){
			if (e.getWeight()>=0){
				if (!zeroSet.contains(e.getVStart().getLabel())){
					zeroSet.add(e.getVStart().getLabel());
				}
			}
		}
		return zeroSet;
	}
	/**
	 * @param v
	 * @return
	 * check if vertex belongs to Min
	 */
	public boolean isMin(String v){
		return vertexOwner.get(v)==MIN;
	}
	
	public static void main(String[] args) {
		String[] vertices={"a", "b", "c", "d", "e", "f"};
		String[] edges= {"a,b","b,a","b,c","c,d","c,e","d,b","d,f","e,e","e,f","f,e","f,d"};
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
		Graph g= new Graph(vertices, edges, weights);
		System.out.println("Running SIEG on small sample graph with vertices:");
		System.out.println(g.getVertices().keySet());
		System.out.println("and edges:");
		System.out.println(g.getEdges().keySet());
		System.out.println("Starting algorithm.");
		System.out.println("with weights:");
		System.out.println(weights);
		System.out.println("Starting algorithm.");
		ArrayList<String> vMax=new ArrayList<String>();
		ArrayList<String> vMin=new ArrayList<String>();
		vMax.add("a");
		vMax.add("c");
		vMax.add("e");
		vMin.add("b");
		vMin.add("d");
		vMin.add("f");
		SIEG sa= new SIEG(g, vMin, vMax);
		sa.findStrategies();
		System.out.print("Results (in format vertex=battery level): ");
		System.out.println(sa.battery);
	}

}
