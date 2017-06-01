package grid;

import graph.Graph;
import graph.Vertex;
import help.Helper;
import help.NegCycleFilter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Stanimir
 * Class generates graphs from the GridS family
 */
public class GridSquare{

	private double intLayerMin;

	private double intLayerMax;

	private double layerMin;

	private double layerMax;

	private final Helper help=new Helper();

	private int numVertices;
	
	/**
	 * @param numVertices
	 * @param layerMin
	 * @param layerMax
	 * @param intLayerMin
	 * @param intLayerMax
	 * GridS generator
	 */
	public GridSquare(int numVertices, double layerMin, double layerMax, double intLayerMin, double intLayerMax) {
		super();
		this.numVertices=numVertices;
		this.intLayerMin=intLayerMin;
		this.intLayerMax=intLayerMax;
		this.layerMin=layerMin;
		this.layerMax=layerMax;
	}
	
	/**
	 * @param hasSource
	 * @return
	 * generates GridS graph
	 */
	public Graph generateGraph(boolean hasSource){
		int X= (int) Math.sqrt(numVertices);
		int Y=X;
		int numV=numVertices;
		if (hasSource){
			numV++;
		}
		ArrayList<String> vertices=help.generateVertices(numV);
		String source=null;
		if (hasSource){
			source=vertices.remove(0);
			numV--;
		}
		ArrayList<String> edges=new ArrayList<String>();
		ArrayList<String> firstLayer=new ArrayList<String>();
		HashMap<String, Double> weights=new HashMap<String,Double>();
		String[][] vArr= new String[X][Y];
		int index=0;
		for (int i=0; i<X; i++){
			for (int j=0; j<Y; j++){
				vArr[i][j]=vertices.get(index);
				index++;
				if (j==0){
					firstLayer.add(vArr[i][j]);
				}
			}
		}
		for (int i=0; i<X; i++){
			for (int j=0; j<Y; j++){
				if(i+1<X){
					String e=vArr[i][j]+","+vArr[i+1][j];
					double weight=help.generateRandomWeight(layerMin, layerMax);
					edges.add(e);
					weights.put(e, weight);
				}
				else if(i+1==X){
					String e=vArr[i][j]+","+vArr[0][j];
					double weight=help.generateRandomWeight(layerMin, layerMax);
					edges.add(e);
					weights.put(e, weight);
				}
				if (j+1<Y){
					String e=vArr[i][j]+","+vArr[i][j+1];
					double weight=help.generateRandomWeight(intLayerMin, intLayerMax);
					edges.add(e);
					weights.put(e, weight);
				}
				else if (j+1==Y){
					String e=vArr[i][j]+","+vArr[i][0];
					double weight=help.generateRandomWeight(intLayerMin, intLayerMax);
					edges.add(e);
					weights.put(e, weight);
				}
			}
		}
		if(hasSource){
			for (String v: firstLayer){
				String e=source+","+v;
				edges.add(e);
				double weight=help.generateRandomWeight(layerMin, layerMax);
				weights.put(e, weight);
			}
			vertices.add(source);
		}
		Graph g= new Graph(vertices,edges,weights);
		return g;
	}
	
	public static void main(String[] args){
		System.out.println("GridS generator");
		GridSquare gs= new GridSquare(10,1,4,5,10);
		Graph g=gs.generateGraph(true);
		for (Vertex v: g.getVertices().values()){
			System.out.println(v.getLabel());
			System.out.println("Inwards: "+v.getCollection().getInwards());
			System.out.println("Outwards: "+v.getCollection().getOutwards());
		}
		NegCycleFilter ncf= new NegCycleFilter(g);
		ncf.addCycles(1, 3);
		System.out.println(ncf.getG());
	}
}