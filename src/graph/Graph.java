package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.lang.Math;

import graph.Vertex;
import graph.Edge;
import graph.IncidenceCollection;

/**
 * @author Stanimir
 *Class which implements the Graph data structure. Uses Edge, Vertex and IncidenceCollection classes
 */
public class Graph implements Cloneable{
	
	private HashMap<String,Vertex> vertices;
	
	private HashMap<String,Edge> edges;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Graph clone(){
		Graph cloned;
		try {
			cloned = (Graph) super.clone();
			HashMap<String,Vertex> newVer=new HashMap<String,Vertex>();
			for (Vertex k: cloned.getVertices().values()){
				newVer.put(k.getLabel(), k.clone());
			}
			cloned.vertices=newVer;
			HashMap<String,Edge> newEdges= new HashMap<String,Edge>();
			for (Edge k: cloned.getEdges().values()){
				newEdges.put(k.getLabel(), k.clone());
			}
			cloned.edges=newEdges;
			return cloned;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param g
	 * Graph constructor
	 */
	public Graph(Graph g){
		super();
		this.vertices=new HashMap<String,Vertex>();
		this.edges=new HashMap<String,Edge>();
		vertices.putAll(g.getVertices());
		edges.putAll(g.getEdges());
	}
	/**
	 * @param vertices
	 * @param edges
	 * @param weights
	 * Graph constructor
	 */
	public Graph(String[] vertices, String[] edges, HashMap<String,Double> weights){
		super();
		this.vertices=new HashMap<String,Vertex>();
		this.edges=new HashMap<String,Edge>();
		for (String vertexLabel: vertices){
			insertVertex(vertexLabel);
		}
		
		for (String edge: edges){
			double weight=weights.get(edge);
			insertEdge(edge, weight);
		}
	}
	
	/**
	 * @param vertices
	 * @param edges
	 * Graph constructor
	 */
	public Graph(String[] vertices, String[] edges){
		super();
		this.vertices=new HashMap<String,Vertex>();
		this.edges=new HashMap<String,Edge>();
		for (String vertexLabel: vertices){
			insertVertex(vertexLabel);
		}
		
		for (String edge: edges){
			insertEdge(edge, 0);
		}
	}
	
	/**
	 * @param vertices2
	 * @param edges2
	 * @param weights
	 * Graph constructor
	 */
	public Graph(ArrayList<String> vertices2, ArrayList<String> edges2,
			HashMap<String, Double> weights) {
		super();
		this.vertices=new HashMap<String,Vertex>();
		this.edges=new HashMap<String,Edge>();
		for (String vertexLabel: vertices2){
			insertVertex(vertexLabel);
		}
		
		for (String edge: edges2){
			double weight=weights.get(edge);
			insertEdge(edge, weight);
		}
	}

	/**
	 * @param vertexLabel
	 * 
	 * inserts vertex in graph
	 */
	public void insertVertex(String vertexLabel){
		Vertex vertex= new Vertex(vertexLabel);
		IncidenceCollection ic= new IncidenceCollection(vertexLabel);
		vertex.setCollection(ic);
		this.vertices.put(vertexLabel, vertex);
	}
	
	/**
	 * @param edge
	 * @param weight
	 * 
	 * inserts edge in graph
	 */
	public void insertEdge(String edge, double weight){
		String[] edgeString=edge.split(",");
		Vertex startV= new Vertex(this.vertices.get(edgeString[0]));
		Vertex endV= new Vertex(this.vertices.get(edgeString[1]));
		startV.getCollection().addOutwards(edge);
		endV.getCollection().addInwards(edge);
		if(edgeString[0].equals(edgeString[1])){
			startV.getCollection().addInwards(edge);
			endV.getCollection().addOutwards(edge);
		}
		vertices.put(startV.getLabel(), startV);
		vertices.put(endV.getLabel(), endV);
		int collectionRefStart=startV.getCollection().getOutwards().indexOf(edge);
		int collectionRefEnd=endV.getCollection().getInwards().indexOf(edge);
		Edge e= new Edge(weight, startV, endV, collectionRefStart, collectionRefEnd);
		this.edges.put(edge, e);
	}
	
	/**
	 * @param key
	 * @param edge
	 * inserts edge in graph (use other insert)
	 */
	public void insertEdge(String key, Edge edge){
		this.edges.put(key, edge);
	}
	
	public HashMap<String, Vertex> getVertices() {
		return vertices;
	}

	public HashMap<String, Edge> getEdges() {
		return edges;
	}
	
	/**
	 * @param edge
	 * @return
	 * 
	 * return the start and end vertex of an edge
	 */
	public Vertex[] endVertices(String edge){
		return edges.get(edge).endVertices();
	}
	
	/**
	 * @param vertex
	 * @param edge
	 * @return
	 * 
	 * get the opposite vertex of an edge
	 */
	public Vertex opposite(String vertex, String edge){
		Vertex[] endVertices=edges.get(edge).endVertices();
		if (endVertices[0].getLabel()==vertex){
			return endVertices[1];
		}else if(endVertices[1].getLabel()==vertex){
			return endVertices[0];
		}else{
			return null;
		}
	}
	
	/**
	 * @param vertex
	 * @return
	 * 
	 * return the incident edges to a vertex
	 */
	public Edge[] incidentEdges(String vertex){
		ArrayList<String> incE= vertices.get(vertex).getCollection().getCollection();
		Edge[] incidentEdges= new Edge[incE.size()];
		for (int i=0;i<incE.size();i++){
			incidentEdges[i]=edges.get(incE.get(i));
		}
		return incidentEdges;
	}
	
	/**
	 * @param v1
	 * @param v2
	 * @return
	 * 
	 * check if two vertices are adjacent
	 */
	public boolean areAdjacent(String v1, String v2){
		Vertex vertex1=vertices.get(v1);
		Vertex vertex2=vertices.get(v2);
		if (vertex1.getCollection().size()<vertex2.getCollection().size()){
			for (String edge: vertex1.getCollection().getCollection()){
				String[] eStr=edge.split(",");
				if (eStr[0].equals(v2) || eStr[1].equals(v2)){
					return true;
				}
			}
		}else{
			for (String edge: vertex2.getCollection().getCollection()){
				String[] eStr=edge.split(",");
				if (eStr[0].equals(v1) || eStr[1].equals(v1)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param edge
	 * @param weight
	 * change edge weight
	 */
	public void changeEdgeWeight(String edge, double weight){
		Edge e= new Edge(edges.get(edge));
		e.setWeight(weight);
		edges.put(e.getLabel(), e);
	}
	
	/**
	 * @param edge
	 * remove edge
	 */
	public void removeEdge(String edge){
		Edge e=edges.get(edge);
		Vertex vStart=new Vertex(vertices.get(e.getVStart().getLabel()));
		vStart.getCollection().removeEdge(edge, false);
		vertices.put(vStart.getLabel(), vStart);
		Vertex vEnd=new Vertex(vertices.get(e.getVEnd().getLabel()));
		vEnd.getCollection().removeEdge(edge, true);
		vertices.put(vEnd.getLabel(), vEnd);
		edges.remove(edge);
	}
	
	/**
	 * @param vertex
	 * 
	 * remove vertex
	 */
	public void removeVertex(String vertex){
		Vertex v=vertices.get(vertex);
		ArrayList<String> incidentEdges=v.getCollection().getCollection();
		for (String edge: incidentEdges){
			removeEdge(edge);
		}
		vertices.remove(vertex);
	}
	
	/**
	 * @return
	 * 
	 * return largest absolute weight in graph
	 */
	public double getLargesAbsoluteWeight(){
		Collection<Edge> e= this.getEdges().values();
		double maxAbs=0;
		for (Edge edge : e){
			double eWeight=Math.abs(edge.getWeight());
			if (eWeight>maxAbs){
				maxAbs=eWeight;
			}
		}
		return maxAbs;
	}
	
	/**
	 * @param e
	 * flip edge the opposite direction
	 */
	public void flipEdge(String e){
		Edge edge=this.getEdges().get(e).clone();
		Vertex vStart=edge.getVStart();
		Vertex vEnd=edge.getVEnd();
		ArrayList<String> startOut=vStart.getCollection().getOutwards();
		ArrayList<String> endIn=vEnd.getCollection().getInwards();
		ArrayList<String> startIn=vStart.getCollection().getInwards();
		ArrayList<String> endOut=vEnd.getCollection().getOutwards();
		edge.flipEdge();
		String newE=edge.getLabel();
		if (this.getEdges().containsKey(newE)){
			double oldWeight= this.getEdges().get(e).getWeight();
			double newWeight=this.getEdges().get(newE).getWeight();
			this.changeEdgeWeight(e, newWeight);
			this.changeEdgeWeight(newE, oldWeight);
		}else{
			endIn.remove(e);
			startOut.remove(e);
			startIn.add(edge.getLabel());
			endOut.add(edge.getLabel());
			this.getVertices().get(vStart.getLabel()).getCollection().setInwards(startIn);
			this.getVertices().get(vStart.getLabel()).getCollection().setOutwards(startOut);
			this.getVertices().get(vEnd.getLabel()).getCollection().setInwards(endIn);
			this.getVertices().get(vEnd.getLabel()).getCollection().setOutwards(endOut);
			this.getEdges().remove(e);
			this.getEdges().put(edge.getLabel(), edge);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Create Graph");
		String[] vertices={"a", "b", "c", "d", "e", "f","g","h","x","y"};
		String[] edges= {"a,b","a,c", "b,c","b,d","b,e","c,f","d,h","e,h","e,g","f,b","f,g", "g,g"};
		Graph g= new Graph(vertices, edges);
		System.out.println("Vertices: "+g.getVertices().keySet());
		System.out.println("Edges: "+g.getEdges().keySet());
		System.out.println(g.getVertices().get("g").getCollection().getOutwards());
		System.out.println(g.getVertices().get("c").getCollection().getCollection());
		System.out.println(g.getEdges().get("g,g"));

	}

}