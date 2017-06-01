package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import graph.Graph;


/**
 * @author Stanimir
 *
 *Implementation of the modified version of the Dijkstra algorithm for finding longest paths, which is used in SIEG algorithm.
 */
public class ModDijkstra {

	private Graph graph;

	private ArrayList<String> zeroSet;

	private HashMap<String, Double> dp;

	private HashMap<String, Double> d0;

	private HashMap<String, Double> key;

	private PriorityQueue<String> q;

	private double infinity;
	
	/**
	 * @param graphX
	 * @param zeroSet
	 * @param dp
	 * 
	 * sets up the ModDijkstra algorithm
	 */
	public ModDijkstra(Graph graphX, ArrayList<String> zeroSet,
			HashMap<String, Double> dp) {
		super();
		this.graph = graphX;
		this.zeroSet = zeroSet;
		this.dp = dp;
		this.d0= new HashMap<String, Double>();
		this.key=new HashMap<String, Double>();
		Comparator<String> kc=new KeyComparator();
		this.q= new PriorityQueue<String>(this.graph.getVertices().size(),kc);
		this.infinity=Double.NEGATIVE_INFINITY;
	}
	
	/**
	 * finds and returns the longest paths to each vertex from a source.
	 * Algorithm works differently from normal Dijkstra.
	 */
	public void findDistances(){
		ArrayList<String> S=new ArrayList<String>();
		for (String v: graph.getVertices().keySet()){
			if (dp.get(v)>infinity){
				S.add(v);
			}
		}
		initiate(S);
		while(!q.isEmpty()){
			String u=q.poll();
			ArrayList<String> inwards=graph.getVertices().get(u).getCollection().getInwards();
			for (String e: inwards){
				String v=e.split(",")[0];
				if(S.contains(v) && !(zeroSet.contains(v))){
					if(graph.getEdges().get(e)==null){
						System.out.println(e+", "+ graph.getEdges().get(e));
						System.out.println(graph.getEdges().get(e).getWeight());
					}
					double weight=graph.getEdges().get(e).getWeight();
					double temp=key.get(u)+weight-dp.get(v)+dp.get(u);
					if(temp>key.get(v)){
						key.put(v,temp);
						if(!q.contains(v)){
							q.add(v);
						}
					}
				}
			}	
		}
		for (String v: S){
			d0.put(v, dp.get(v)+key.get(v));
		}
		
		for (String v: graph.getVertices().keySet()){
			if (!S.contains(v)){
				d0.put(v, infinity);
			}
		}
	}
	
	/**
	 * @param S
	 * 
	 * initialization of algorithm
	 */
	private void initiate(ArrayList<String> S){
		for (String v: S){
			key.put(v, infinity);
		}
		for (String v: zeroSet){
			key.put(v, 0d);
			q.add(v);
		}
	}
	
	/**
	 * @author Stanimir
	 *supporting class
	 */
	private class KeyComparator implements Comparator<String>{
		public int compare(String x, String y)
	    {
	        if (key.get(x) < key.get(y))
	        {
	            return -1;
	        }
	        if (key.get(x) > key.get(y))
	        {
	            return 1;
	        }
	        return 0;
	    }
	}
	
	public Graph getGraph() {
		return graph;
	}

	public HashMap<String, Double> getD0() {
		return d0;
	}

	public HashMap<String, Double> getKey() {
		return key;
	}

	public PriorityQueue<String> getQ() {
		return q;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
