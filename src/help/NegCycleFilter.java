package help;

import java.util.ArrayList;

import graph.Graph;

/**
 * @author Stanimir
 *Filter for adding a different number of negative cycles to graphs
 */
public class NegCycleFilter {
	
	private Helper help;
	
	private Graph g;
	
	public NegCycleFilter(Graph g) {
		super();
		this.g=new Graph(g);
		this.help= new Helper();
	}
	
	/**
	 * Add cycle of type 1
	 */
	public void add1(){
		addCycles(0,0);
	}
	/**
	 * Add cycle of type 2
	 */
	public void add2(){
		addCycles(1,3);
	}
	/**
	 * Add cycle of type 3
	 */
	public void add3(){
		addCycles((int)Math.sqrt(g.getVertices().size()),3);
	}
	/**
	 * Add cycle of type 4
	 */
	public void add4(){
		addCycles((int)Math.pow(g.getVertices().size(), 1.0/3),(int)Math.sqrt(g.getVertices().size()));
	}
	/**
	 * Add cycle of type 5
	 */
	public void add5(){
		addCycles(1,g.getVertices().size());
	}
	
	/**
	 * @param numCycles
	 * @param numEdges
	 * add a specific number of cycles of a specific size
	 */
	public void addCycles(int numCycles, int numEdges){
		ArrayList<String> usedVertices= new ArrayList<String>();
		for (int i=0; i<numCycles; i++){
			usedVertices=addCycle(usedVertices, numEdges);
		}
	}
	
	/**
	 * @param usedVertices
	 * @param numEdges
	 * @return
	 * add a single cycle
	 */
	public ArrayList<String> addCycle(ArrayList<String> usedVertices, int numEdges){
		ArrayList<String> vertices= new ArrayList<String>();
		vertices.addAll(g.getVertices().keySet());
		int startInt=help.generateRandInt(vertices.size());
		String start=vertices.get(startInt);
		boolean checkStart= usedVertices.contains(start);
		while(checkStart){
			startInt=help.generateRandInt(g.getVertices().size());
			start=vertices.get(startInt);
			checkStart=usedVertices.contains(start);
		}
		String next=start;
		usedVertices.add(start);
		int edges=0;
		while (edges<numEdges){
			int currIndex=help.generateRandInt(vertices.size());
			String vertex=vertices.get(currIndex);
			boolean check= usedVertices.contains(vertex);
			if (!check){
				String edge=next+","+vertex;
				if(!g.getEdges().containsKey(edge)){
					double weight=0;
					if (edges==0){
						weight=-1;
					}
					g.insertEdge(edge, weight);
					usedVertices.add(vertex);
					next=vertex;
					edges++;
				}
			}
		}
		String lastEdge=next+","+start;
		g.insertEdge(lastEdge, 0);
		return usedVertices;
	}
	
	public Graph getG() {
		return g;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
