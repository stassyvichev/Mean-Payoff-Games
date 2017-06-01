package rand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import help.Helper;
import help.NegCycleFilter;

/**
 * @author Stanimir
 *OLD, do not use
 */
public class SPRandGenerator implements Cloneable {
	
	public SPRandGenerator() {
		super();
	}
	
	/**
	 * @author  Stanimir
	 */
	public class RandLen implements Cloneable{
		private ArrayList<String> vertices;
		private ArrayList<String> edges;
		private HashMap<String, Double> weights;
		private ArrayList<String> varEdges;
		/**
		 * @uml.property  name="help"
		 * @uml.associationEnd  
		 */
		private Helper help;
		/**
		 * @param numVertices
		 * @param numEdges
		 */
		public RandLen(int numVertices, int numEdges) {
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
				String edge=vertex1+","+vertex2;
				if(!edges.contains(edge)){
					edges.add(edge);
					varEdges.add(edge);
				}
			}
		}
		
		public Graph generateInitialGraph(){
			for (String e: edges){
				weights.put(e, 1d);
			}
			Graph graph= new Graph(vertices, edges, weights);
			return graph;
		}
		
		public Graph generateGraph(double minWeight, double maxWeight){
			Random randGen=new Random();
			for (String e: varEdges){
				double weight=randGen.nextInt((int)(Math.abs(maxWeight)+Math.abs(minWeight)))+minWeight;
				weights.put(e, weight);
			}
			Graph graph= new Graph(vertices, edges, weights);
			return graph;
		}
	}
	
	/**
	 * @author  Stanimir
	 */
	public class Rand4 implements Cloneable{
		private int numVertices;
		private int numEdges;
		private double maxWeight;
		private double minWeight;
		/**
		 * @uml.property  name="help"
		 * @uml.associationEnd  
		 */
		private Helper help;
		/**
		 * @param numVertices
		 * @param maxWeight
		 * @param minWeight
		 */
		public Rand4(int numVertices, double minWeight,
				double maxWeight) {
			super();
			help= new Helper();
			this.numVertices = numVertices;
			this.maxWeight = maxWeight;
			this.minWeight = minWeight;
		}
		
		public Graph generateGraph(double numEdgeCompound){
			numEdges=(int) (numVertices*numEdgeCompound);
			ArrayList<String> vertices=help.generateVertices(numVertices);
			ArrayList<String> edges=new ArrayList<String>();
			HashMap<String, Double> weights= new HashMap<String, Double>();
			help.generateHamiltonianCycle(numVertices, vertices, edges, weights);
			Random randGen=new Random();
			while(edges.size()<numEdges){
				int v1=randGen.nextInt(numVertices);
				int v2=randGen.nextInt(numVertices);
				String vertex1=vertices.get(v1);
				String vertex2=vertices.get(v2);
				String edge=vertex1+","+vertex2;
				if(!edges.contains(edge)){
					edges.add(edge);
					double weight=randGen.nextInt((int)(Math.abs(maxWeight)+Math.abs(minWeight)))+minWeight;
					weights.put(edge,weight);
				}
			}
			Graph graph= new Graph(vertices, edges, weights);
			return graph;
		}
		
		public Graph generateDenseGraph(){
			int numEdgeCompound=numVertices/4;
			return generateGraph(numEdgeCompound);
		}
	}
	
	/**
	 * @author  Stanimir
	 */
	public class RandP implements Cloneable{
		/**
		 * @uml.property  name="initialGraph"
		 * @uml.associationEnd  
		 */
		private Graph initialGraph;

		/**
		 * @param numVertices
		 */
		public RandP(int numVertices) {
			super();
			Rand4 r4=new Rand4(numVertices, -10, 10);
			initialGraph=r4.generateGraph(4);
		}
		
		public Graph generateGraph(int minPotential, int maxPotential){
			Graph g=initialGraph.clone();
			HashMap<String,Integer> potentials=new HashMap<String, Integer>();
			Random randGen= new Random();
			for(String v: g.getVertices().keySet()){
				int potential= randGen.nextInt(Math.abs(maxPotential)+Math.abs(minPotential))+minPotential;
				potentials.put(v, potential);
			}
//			System.out.println(potentials);
			//lp(u,v)=l(u,v)+p(u)-p(v) ??
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
	}
	
	public static void main(String[] args) {
		SPRandGenerator rand= new SPRandGenerator();
		Rand4 r4= rand.new Rand4(10, -10d, 10d);
		Graph g= r4.generateGraph(2);
		System.out.println("Rand 4");
		for(Vertex v: g.getVertices().values()){
			System.out.println(v.getLabel());
		}
		for(Edge e: g.getEdges().values()){
			System.out.println(e.getLabel()+":"+e.getWeight());
		}
		System.out.println(g.getEdges().size());
		System.out.println("Adding negative cycle");
		
		NegCycleFilter ncf= new NegCycleFilter(g);
		ncf.addCycles(1, 3);
		System.out.println(ncf.getG().getEdges().size());
		for(Edge e: ncf.getG().getEdges().values()){
			if(!g.getEdges().containsKey(e.getLabel())){
				System.out.println(e.getLabel()+":"+e.getWeight());
			}
			
		}
//		System.out.println("Rand Len Initial");
//		RandLen rl= rand.new RandLen(5,20);
//		Graph g2= rl.generateInitialGraph();
//		for(Vertex v: g2.getVertices().values()){
//			System.out.println(v.getLabel());
//		}
//		for(Edge e: g2.getEdges().values()){
//			System.out.println(e.getLabel()+":"+e.getWeight());
//		}	
//		System.out.println("Rand Len actual");
//		Graph g3= rl.generateGraph(-10,10);
//		for(Vertex v: g3.getVertices().values()){
//			System.out.println(v.getLabel());
//		}
//		for(Edge e: g3.getEdges().values()){
//			System.out.println(e.getLabel()+":"+e.getWeight());
//		}
//		System.out.println("Rand P");
//		RandP rP=rand.new RandP(5);		
//		Graph g4= rP.generateGraph(0,10);
//		for(Vertex v: g4.getVertices().values()){
//			System.out.println(v.getLabel());
//		}
//		for(Edge e: g4.getEdges().values()){
//			System.out.println(e.getLabel()+":"+e.getWeight());
//		}
	}
}
