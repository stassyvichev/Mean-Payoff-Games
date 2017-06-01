package algorithms;

import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * @author Stanimir
 * Implementation of BCDGR algorithm for solving EGs
 *
 */
public class BCDGR {
	private static final String MAX="Max";
	private static final String MIN="Min";
	
	private Graph graph;
	
	private HashMap<String, Double> battery;
	
	private HashMap<String, Boolean> inStack;
	
	private HashMap<String, String> vertexOwner;
	
	private Stack<String> affectedVertices;
	
	private double M;
	
	
	/**
	 * @param graph
	 * @param vMin
	 * @param vMax
	 * 
	 * sets up the algorithm and everything required
	 */
	public BCDGR(Graph graph, ArrayList<String> vMin,  ArrayList<String> vMax) {
		super();
		this.graph = new Graph(graph);
		//initiate max upper bound
		this.M=this.graph.getLargesAbsoluteWeight()*this.graph.getVertices().size();
		//initiate battery with 0 for each node,
		//vertexOwnership with each vertex either being Min or Max vertex,
		//add all vertices to affectedVertices stack,
		//set inStack=true for each vertex.
		this.battery= new HashMap<String, Double>();
		this.inStack= new HashMap<String, Boolean>();
		this.vertexOwner= new HashMap<String, String>();
		this.affectedVertices= new Stack<String>();
		for (String vertex: vMin){
			this.battery.put(vertex, 0d);
			this.inStack.put(vertex, true);
			this.vertexOwner.put(vertex, MIN);
			this.affectedVertices.push(vertex);
		}
		for (String vertex: vMax){
			this.battery.put(vertex, 0d);
			this.inStack.put(vertex, true);
			this.vertexOwner.put(vertex, MAX);
			this.affectedVertices.push(vertex);
		}
	}
	
	/**
	 * @param v
	 * @return
	 * 
	 * checks if vertex belongs to Min
	 */
	public boolean isMin(String v){
		return vertexOwner.get(v)==MIN;
	}
	
	/**
	 * @param vertex
	 * @return
	 * 
	 * increments the battery of a vertex so it can survive one step of an EG
	 */
	private double incrementSingleBattery(Vertex vertex){
		ArrayList<String> incidentEdges= vertex.getCollection().getOutwards();
		String e=incidentEdges.get(0);
		double eWeight=graph.getEdges().get(e).getWeight();
		double vbattery=battery.get(graph.getEdges().get(e).getVEnd().getLabel());
		double max=vbattery-eWeight;
		double min=vbattery-eWeight;
		for (String edge: incidentEdges){
			double edgeWeight=graph.getEdges().get(edge).getWeight();
			double nextVbattery=battery.get(graph.getEdges().get(edge).getVEnd().getLabel());
			double sum=nextVbattery-edgeWeight;
			if (sum>max){
				max=sum;
			}else if(sum<min){
				min=sum;
			}
		}
		if (isMin(vertex.getLabel())){
			return max;
		} else{
			return min;
		}
	}
	
	/**
	 * Runs the actual method, calculates the battery levels for all vertices
	 */
	public HashMap<String,Double> getBatteryLevel(){
		//while there are still vertices to be checked
		while(!(affectedVertices.empty())){
			//get a vertex from the stack
			String vLabel=affectedVertices.pop();
			inStack.put(vLabel, false);
			Vertex vertex=graph.getVertices().get(vLabel);
			
			if (vertex.getCollection().getOutwards().size()>0){
				//increase the battery level accordingly
				double newBattery=incrementSingleBattery(vertex);
				//if battery level was increased
				if (newBattery > this.battery.get(vertex.getLabel())){
					//set new battery level 
					this.battery.put(vertex.getLabel(), newBattery);
					//get all vertices with edges going into the current vertex 
					ArrayList<String> inwardEdges=vertex.getCollection().getInwards();
					//pick those that have battery level less than M and who aren't currently 
					//in affectedVertices and add them to affectedVertices.
					for(String edge : inwardEdges){
						String inVertex=graph.getEdges().get(edge).getVStart().getLabel();
						if (!(inStack.get(inVertex)) && (battery.get(inVertex)<2*M)){
							affectedVertices.push(inVertex);
							inStack.put(inVertex, true);
						}
						}
					}
			}
			
		}
		return battery;
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
		ArrayList<String> vMax=new ArrayList<String>();
		ArrayList<String> vMin=new ArrayList<String>();
		vMax.add("a");
		vMax.add("c");
		vMax.add("e");
		vMin.add("b");
		vMin.add("d");
		vMin.add("f");
		System.out.println("Running BCDGR on small sample graph with vertices:");
		System.out.println(g.getVertices().keySet());
		System.out.println("and edges:");
		System.out.println(g.getEdges().keySet());
		System.out.println("with weights:");
		System.out.println(weights);
		System.out.println("Starting algorithm.");
		BCDGR bca=new BCDGR(g, vMin, vMax);
		System.out.println("Results (in format vertex=battery level): "+bca.getBatteryLevel());
		
	}

}
