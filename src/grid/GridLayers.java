package grid;

import graph.Graph;
import graph.Vertex;
import help.Helper;
import help.NegCycleFilter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Stanimir
 *Class for generating graphs of the GridL family
 */
public class GridLayers{

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
	 * Sets up generator
	 */
	public GridLayers(int numVertices, double layerMin, double layerMax, double intLayerMin, double intLayerMax) {
		super();
		this.numVertices=numVertices;
		this.intLayerMin=intLayerMin;
		this.intLayerMax=intLayerMax;
		this.layerMin=layerMin;
		this.layerMax=layerMax;
	}
	
	/**
	 * @param hasSource
	 * @param div
	 * @return
	 * Generates GridL graph
	 */
	public Graph generateGraph(boolean hasSource, int div){
		int X= numVertices/div;
		int Y=div;
		int numV=numVertices+1;
		ArrayList<String> vertices=help.generateVertices(numV);
		String source=vertices.remove(0);
		numV=vertices.size();
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
					double weight=help.generateRandomWeight(intLayerMin, intLayerMax);
					edges.add(e);
					weights.put(e, weight);
				}
				else if(i+1==X){
					String e=vArr[i][j]+","+vArr[0][j];
					double weight=help.generateRandomWeight(intLayerMin, intLayerMax);
					edges.add(e);
					weights.put(e, weight);
				}
				if (j+1<Y){
					String e=vArr[i][j]+","+vArr[i][j+1];
					double weight=help.generateRandomWeight(layerMin, layerMax);
					edges.add(e);
					weights.put(e, weight);
				}
				else if (j+1==Y){
					String e=vArr[i][j]+","+vArr[i][0];
					double weight=help.generateRandomWeight(layerMin, layerMax);
					edges.add(e);
					weights.put(e, weight);
				}
				
				int f=0;
				//1 for testing, 5 for actual
				while (f<5){
					int x=help.generateRandInt(X-i);
					int vIndex=help.generateRandInt(Y);
					String vEnd=vArr[i+x][vIndex];
					String e= vArr[i][j]+","+vEnd;
					if (!edges.contains(e)){
						double weight=help.generateRandomWeight(intLayerMin, intLayerMax)*(X-i)*(X-i);
						edges.add(e);
						weights.put(e, weight);
						f++;
					}
				}
			}
			int l=0;
			//change to 1 for testing, 64 for actual
			while(l<64){
				int vIndex1=help.generateRandInt(Y);
				int vIndex2=help.generateRandInt(Y);
				String v1=vArr[i][vIndex1];
				String v2=vArr[i][vIndex2];
				String e=v1+","+v2;
				if((!v1.equals(v2))&&(!edges.contains(e))){
					double weight=help.generateRandomWeight(layerMin, layerMax);
					edges.add(e);
					weights.put(e, weight);
					l++;
				}
			}
		}
		for (String v: firstLayer){
			String e=source+","+v;
			edges.add(e);
			double weight=help.generateRandomWeight(layerMin, layerMax);
			weights.put(e, weight);
		}
		vertices.add(source);
		Graph g= new Graph(vertices,edges,weights);
		return g;
	}

	public static void main(String[] args){
		System.out.println("GridL generator");
//		GridLayers gl= new GridLayers(9,1,5,5,10);
//		Graph g=gl.generateGraph(true, 3);
//		for (Vertex v: g.getVertices().values()){
//			System.out.println(v.getLabel());
//			System.out.println("Inwards: "+v.getCollection().getInwards());
//			System.out.println("Outwards: "+v.getCollection().getOutwards());
//		}
//		NegCycleFilter ncf= new NegCycleFilter(g);
//		ncf.addCycles(1, 3);
//		System.out.println(ncf.getG());
	}
}