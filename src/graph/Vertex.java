package graph;

import graph.IncidenceCollection;

/**
 * @author Stanimir
 *implements Vertex, uses IncidenceCollection class
 */
public class Vertex implements Cloneable{
	
	private String label;
	
	private int positionRef;
	
	private IncidenceCollection collection;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Vertex clone(){
		Vertex cloned;
		try {
			cloned = (Vertex) super.clone();
			cloned.setLabel((String)cloned.getLabel());
			cloned.setPositionRef(cloned.getPositionRef());
			cloned.setCollection((IncidenceCollection)cloned.getCollection().clone());
			return cloned;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param label
	 * @param positionRef
	 * Vertex constructor
	 */
	public Vertex(String label, int positionRef) {
		super();
		this.label = label;
		this.positionRef = positionRef;
	}
	
	/**
	 * @param label
	 * Vertex constructor
	 */
	public Vertex(String label) {
		super();
		this.label = label;
	}
	
	/**
	 * @param vertex
	 * Vertex constructor
	 */
	public Vertex(Vertex vertex) {
		this.label=vertex.getLabel();
		this.positionRef=vertex.getPositionRef();
		this.collection=new IncidenceCollection(vertex.getCollection());
	}

	/**
	 * @return  the name of the vertex
	 * 
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label  rename vertex
	 * 
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return positionRef
	 */
	public int getPositionRef() {
		return positionRef;
	}

	/**
	 * @param positionRef
	 */
	public void setPositionRef(int positionRef) {
		this.positionRef = positionRef;
	}
	
	/**
	 * @return  the IncidenceCollection object, which contains the 
	 * edges leaving and entering this vertex
	 */
	public IncidenceCollection getCollection() {
		return collection;
	}

	/**
	 * @param collection  Set the IncidenceCollection object 
	 * that contains the incident edges
	 */
	public void setCollection(IncidenceCollection collection) {
		this.collection = collection;
	}
	
	public static void main(String[] args){
		System.out.println("Vertex");
		Vertex v=new Vertex("a");
		v.setCollection(new IncidenceCollection(v.getLabel()));
		v.getCollection().addInwards("a,b");
		Vertex v1=new Vertex(v);
		v1.getCollection().removeEdge("a,b", true);
		System.out.println(v.getCollection().getInwards());
		System.out.println(v1.getCollection().getInwards());
	}
}
