package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Stanimir
 *Class used to manage the edges incident to a specific vertex
 */
public class IncidenceCollection implements Cloneable {
	
	private String vertex;
	
	private ArrayList<String>  inwards;
	
	private ArrayList<String>  outwards;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public IncidenceCollection clone(){
		IncidenceCollection cloned;
		try {
			cloned = (IncidenceCollection) super.clone();
			cloned.setInwards((ArrayList<String>)cloned.getInwards().clone());
			cloned.setOutwards((ArrayList<String>)cloned.getOutwards().clone());
			cloned.setVertex((String) cloned.getVertex()); 
			return cloned;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		}
	
	/**
	 * @param vertex
	 * @param inwards
	 * @param outwards
	 * IncidenceCollection constructor
	 */
	public IncidenceCollection(String vertex, ArrayList<String>  inwards, ArrayList<String>  outwards){
		this.vertex=vertex;
		this.inwards=inwards;
		this.outwards=outwards;
	}
	
	/**
	 * @param vertex
	 * IncidenceCollection constructor
	 */
	public IncidenceCollection(String vertex){
		this.inwards=new ArrayList<String>();
		this.outwards=new ArrayList<String>();
	}
	
	/**
	 * @param collection
	 * IncidenceCollection constructor
	 */
	public IncidenceCollection(IncidenceCollection collection) {
		this.vertex=collection.getVertex();
		this.inwards=new ArrayList<String>(collection.getInwards());
		this.outwards=new ArrayList<String>(collection.getOutwards());
	}
	/**
	 * @return the edges entering this vertex
	 */
	public ArrayList<String> getInwards() {
		return inwards;
	}

	public void setInwards(ArrayList<String> inwards) {
		this.inwards = inwards;
	}

	/**
	 * @return the edges leaving this vertex
	 */
	public ArrayList<String> getOutwards() {
		return outwards;
	}

	public void setOutwards(ArrayList<String> outwards) {
		this.outwards = outwards;
	}

	/**
	 * @return  the vertex  this collection is associated with
	 */
	public String getVertex() {
		return vertex;
	}

	/**
	 * @param vertex
	 */
	public void setVertex(String vertex) {
		this.vertex = vertex;
	}

	/**
	 * @return all edges incident to the vertex
	 */
	public ArrayList<String> getCollection() {
		ArrayList<String> collection=new ArrayList<String>();
		Set<String> col = new HashSet<String>(inwards);
		col.addAll(outwards);
		collection.addAll(col);
		return collection;
	}
	
	/**
	 * @param e
	 * add an edge which enters this vertex
	 */
	public void addInwards(String e){
		this.inwards.add(e);
	}
	
	/**
	 * @param e
	 * add an edge that leaves this vertex
	 */
	public void addOutwards(String e){
		this.outwards.add(e);
	}
	
	/**
	 * @return the number of edges incident to the vertex
	 */
	public int size(){
		return inwards.size()+outwards.size();
	}
	
	/**
	 * @param edge
	 * @param inward
	 * remove an edge from the collection
	 */
	public void removeEdge(String edge, boolean inward){
		if (inward){
			inwards.remove(edge);
		}else{
			outwards.remove(edge);
		}
	}
	
	public static void main(String[] args){
		System.out.println("Incidence Collection");
		ArrayList<String> inwards=new ArrayList<String>();
		inwards.add("b,a");
		ArrayList<String> outwards=new ArrayList<String>();
		outwards.add("a,b");
		IncidenceCollection ic= new IncidenceCollection("a", inwards, outwards);
		ic.addInwards("e,e");
		ic.addOutwards("e,e");
		System.out.println(ic.getOutwards());
	}
}
