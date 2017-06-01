package graph;

import graph.Vertex;

/**
 * @author Stanimir
 *
 *This class implements an Edge and all the methods required for handling edges.
 */
public class Edge implements Cloneable{

	private String label;

	private double weight;
	
	private int positionRef;
	
	private Vertex vStart;

	private Vertex vEnd;

	private int collectionRefStart;

	private int collectionRefEnd;
	
	public Edge clone(){
		Edge cloned;
		try {
			cloned = (Edge) super.clone();
			cloned.setLabel((String)cloned.getLabel());
			cloned.setPositionRef(cloned.getPositionRef());
			cloned.setVEnd((Vertex)cloned.getVEnd().clone());
			cloned.setVStart((Vertex)cloned.getVStart().clone());
			cloned.setWeight(cloned.getWeight());
			cloned.setCollectionRefEnd(cloned.getCollectionRefEnd());
			cloned.setCollectionRefStart(cloned.getCollectionRefStart());
			return cloned;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * @param e
	 * edge constructor
	 */
	public Edge(Edge e){
		super();
		this.label=e.getLabel();
		this.weight=e.getWeight();
		this.vStart = e.getVStart();
		this.vEnd = e.getVEnd();
		this.collectionRefStart = e.getCollectionRefStart();
		this.collectionRefEnd = e.getCollectionRefEnd();
	}
	/**
	 * @param weight
	 * @param vStart
	 * @param vEnd
	 * @param cStart
	 * @param cEnd
	 * 
	 * Edge constructor
	 */
	public Edge(double weight, Vertex vStart, Vertex vEnd, int cStart, int cEnd) {
		super();
		this.label=vStart.getLabel()+","+vEnd.getLabel();
		this.weight=weight;
		this.vStart = vStart;
		this.vEnd = vEnd;
		this.collectionRefStart = cStart;
		this.collectionRefEnd = cEnd;
	}
	
	/**
	 * @param vStart
	 * @param vEnd
	 * @param cStart
	 * @param cEnd
	 * Edge constructor
	 */
	public Edge( Vertex vStart, Vertex vEnd, int cStart, int cEnd) {
		super();
		this.weight=0;
		this.vStart = vStart;
		this.vEnd = vEnd;
		this.collectionRefStart = cStart;
		this.collectionRefEnd = cEnd;
	}
	
	/**
	 * @param label
	 * @param weight
	 * @param positionRef
	 * @param vStart
	 * @param vEnd
	 * @param cStart
	 * @param cEnd
	 * Edge constructor
	 */
	public Edge(String label, double weight, int positionRef, Vertex vStart, Vertex vEnd, int cStart, int cEnd) {
		super();
		this.label = label;
		this.weight=weight;
		this.positionRef = positionRef;
		this.vStart = vStart;
		this.vEnd = vEnd;
		this.collectionRefStart = cStart;
		this.collectionRefEnd = cEnd;
	}

	/**
	 * @param weight
	 * @param positionRef
	 * @param vStart
	 * @param vEnd
	 * @param cStart
	 * @param cEnd
	 * Edge constructor
	 */
	public Edge( double weight, int positionRef , Vertex vStart, Vertex vEnd, int cStart, int cEnd) {
		super();
		this.label = Integer.toString(positionRef);
		this.weight=weight;
		this.positionRef = positionRef;
		this.vStart = vStart;
		this.vEnd = vEnd;
		this.collectionRefStart = cStart;
		this.collectionRefEnd = cEnd;
	}
	/*
	 * Following methods are getters and setters for various variables of edges
	 */

	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}

	public Vertex getVStart() {
		return vStart;
	}

	public void setVStart(Vertex vStart) {
		this.vStart = vStart;
	}

	public Vertex getVEnd() {
		return vEnd;
	}

	public void setVEnd(Vertex vEnd) {
		this.vEnd = vEnd;
	}
	
	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public int getPositionRef() {
		return positionRef;
	}


	public void setPositionRef(int positionRef) {
		this.positionRef = positionRef;
	}


	public int getCollectionRefStart() {
		return collectionRefStart;
	}


	public void setCollectionRefStart(int cStart) {
		this.collectionRefStart = cStart;
	}


	public int getCollectionRefEnd() {
		return collectionRefEnd;
	}


	public void setCollectionRefEnd(int cEnd) {
		this.collectionRefEnd = cEnd;
	}
	
	public Vertex[] endVertices(){
		Vertex[] endVertices= {this.vStart, this.vEnd};
		return endVertices;
	}
	
	/**
	 * flip an edge's start and end vertices
	 */
	public void flipEdge(){
		Vertex verStart=getVStart();
		Vertex verEnd=getVEnd();
		setVStart(verEnd);
		setVEnd(verStart);
		setLabel(this.getVStart().getLabel()+","+this.getVEnd().getLabel());
	}
	
	public static void main(String[] args){
		System.out.println("Creating edge with vertices a and b with weight 4");
		Vertex a= new Vertex("a");
		Vertex b= new Vertex("b");
		Edge e=new Edge(4d,a,b,0,0);
		System.out.println("Label: "+e.getLabel());
		System.out.println("Start: "+e.getVStart().getLabel());
		System.out.println("End: "+e.getVEnd().getLabel());
		System.out.println("Weight: "+e.getWeight());
	}
}
