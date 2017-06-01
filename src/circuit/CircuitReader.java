package circuit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import graph.Graph;
import help.Helper;

/**
 * @author Stanimir
 *
 *Implementation of reader which takes circuit design from .netD file and converts it to a graph.
 *Please beware it may take some time to run.
 *Please enter the path to the ibm01.netD file on the CD in the path variable in the main method between the quotation marks
 *Please type it in the following format: path="C:\\Testing\\ibm01.netD"
 */
public class CircuitReader {

	private Helper help;

	/**
	 * sets up the CircuitReader
	 */
	public CircuitReader() {
		super();
		this.help = new Helper();
	}
	/**
	 * @param path
	 * counting number of nets and modules
	 */
	public void count(String path){
		BufferedReader reader = null;
		ArrayList<String> c= new ArrayList<String>();
		int count=0;
        try {
            reader = new BufferedReader(
                     new FileReader(path));
            for (int i=0; i<5;i++){
            	reader.readLine();
            }
            String line= reader.readLine();
            while(line != null){
            	String[] source=line.split(" ");
            	if(!source[1].equals("s")){
            		count++;
            	}
            	line=reader.readLine();
            }
            System.out.println(count);
        }catch (IOException e) {
            System.out.println("Exception:" + e);
            System.exit(1);
        }
	}
	
	/**
	 * @param path
	 * @return
	 * 
	 * generate graph from .netD file 
	 */
	public Graph generateGraph(String path){
		BufferedReader reader = null;
		ArrayList<String> vertices= new ArrayList<String>();
		ArrayList<String> edges= new ArrayList<String>();
		HashMap<String, Double> weights= new HashMap<String, Double>();
        try {
            reader = new BufferedReader(
                     new FileReader(path));
            for (int i=0; i<5;i++){
            	reader.readLine();
            }
            String line= "";
            String currentSource="";
            while(line != null){
            	line=reader.readLine();
            	if (line==null){
            		break;
            	}
            	String[] n=line.split(" ");
            	if(n[1].equals("s")){
            		currentSource=line;
            	}else{
            		String[] source=currentSource.split(" ");

    				if(!vertices.contains(source[0])){
						vertices.add(source[0]);
					}
					if(!vertices.contains(n[0])){
						vertices.add(n[0]);
					}
    				if(source[0].trim().startsWith("p")){
    					if(source[2].trim().equals("O")){
    						String edge=source[0]+","+n[0];
    						if(!edges.contains(edge)){
    							edges.add(edge);
        						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
        						weights.put(edge, weight);
    						}
    					}else if(source[2].trim().equals("I")){
    						String edge=n[0]+","+source[0];
    						if(!edges.contains(edge)){
	    						edges.add(edge);
	    						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
	    						weights.put(edge, weight);
    						}
    					}else{
    						String edge1=n[0]+","+source[0];
    						String edge2=source[0]+","+n[0];
    						if(!edges.contains(edge1)){
	    						edges.add(edge1);
	    						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
	    						weights.put(edge1, weight);
    						}
    						if(edges.contains(edge2)){
    							edges.add(edge2);
        						double weight2=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
        						weights.put(edge2, weight2);
    						}
    					}
    				}else if (source[0].trim().startsWith("a")){
    					if(source[2].trim().equals("O")){
    						if(n[2].trim().equals("I")){
    							String edge=source[0]+","+n[0];
    							if(!edges.contains(edge)){
    								edges.add(edge);
            						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
            						weights.put(edge, weight);
    							}
    						}else{
    							String edge=n[0]+","+source[0];
    							if(!edges.contains(edge)){
	        						edges.add(edge);
	        						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
	        						weights.put(edge, weight);
    							}
    						}
    					}else if(source[2].trim().equals("I")){
    						if(n[2].trim().equals("O")){
    							String edge=n[0]+","+source[0];
    							if(!edges.contains(edge)){
	        						edges.add(edge);
	        						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
	        						weights.put(edge, weight);
    							}
    						}else{
    							String edge=source[0]+","+n[0];
    							if(!edges.contains(edge)){
	        						edges.add(edge);
	        						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
	        						weights.put(edge, weight);
    							}
    						}
    					}else{
    						String edge1=n[0]+","+source[0];
    						String edge2=source[0]+","+n[0];
    						if(!edges.contains(edge1)){
	    						edges.add(edge1);
	    						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
	    						weights.put(edge1, weight);
    						}
    						if(!edges.contains(edge2)){
	    						edges.add(edge2);
	    						double weight2=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
	    						weights.put(edge2, weight2);
    						}
    					}
    				}
    			
            	}
            }
            reader.close();
        }catch (IOException e) {
            System.out.println("Exception:" + e);
            System.exit(1);
        }
        Graph g=new Graph(vertices, edges, weights);
        return g;
	}
	
	/**
	 * @param path
	 * @return
	 * 
	 * Do not use, instead use generateGraph()
	 * generate graph from .netD file
	 */
	public Graph generateCircuit(String path){
		ArrayList<String> vertices= new ArrayList<String>();
		ArrayList<String> edges= new ArrayList<String>();
		HashMap<String, Double> weights= new HashMap<String, Double>();
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                     new FileReader(path));
            for (int i=0; i<5;i++){
            	reader.readLine();
            }
            String line= reader.readLine();
            while(line != null){
            	boolean readNext=true;
            	String[] source=line.split(" ");
            		line=reader.readLine();
            		while(line != null){
            			String[] n=line.split(" ");
            			if(n[1].trim().equals("s")){
            				readNext=false;
            				break;
            			}else{
            				if(!vertices.contains(source[0])){
    							vertices.add(source[0]);
    						}
    						if(!vertices.contains(n[0])){
    							vertices.add(n[0]);
    						}
            				if(source[0].trim().startsWith("p")){
            					if(source[2].trim().equals("O")){
            						String edge=source[0]+","+n[0];
            						edges.add(edge);
            						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
            						weights.put(edge, weight);
            					}else if(source[2].trim().equals("I")){
            						String edge=n[0]+","+source[0];
            						edges.add(edge);
            						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
            						weights.put(edge, weight);
            					}else{
            						String edge1=n[0]+","+source[0];
            						String edge2=source[0]+","+n[0];
            						edges.add(edge1);
            						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
            						weights.put(edge1, weight);
            						edges.add(edge2);
            						double weight2=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
            						weights.put(edge2, weight2);
            					}
            				}else if (source[0].trim().startsWith("a")){
            					if(source[2].trim().equals("O")){
            						if(n[2].trim().equals("I")){
            							String edge=source[0]+","+n[0];
                						edges.add(edge);
                						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
                						weights.put(edge, weight);
            						}else{
            							String edge=n[0]+","+source[0];
                						edges.add(edge);
                						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
                						weights.put(edge, weight);
            						}
            					}else if(source[2].trim().equals("I")){
            						if(n[2].trim().equals("O")){
            							String edge=n[0]+","+source[0];
                						edges.add(edge);
                						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
                						weights.put(edge, weight);
            						}else{
            							String edge=source[0]+","+n[0];
                						edges.add(edge);
                						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
                						weights.put(edge, weight);
            						}
            					}else{
            						String edge1=n[0]+","+source[0];
            						String edge2=source[0]+","+n[0];
            						edges.add(edge1);
            						double weight=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
            						weights.put(edge1, weight);
            						edges.add(edge2);
            						double weight2=help.generateRandomWeight(help.getMinRandWeight(), help.getMaxRandWeight());
            						weights.put(edge2, weight2);
            					}
            				}
            			}
            			line=reader.readLine();
            		}
            	if(readNext){
            		line=reader.readLine();
            	}else{
            		readNext=true;
            	}
            }
            reader.close();
        }
        catch (IOException e) {
            System.out.println("Exception:" + e);
            System.exit(1);
        }
        
        Graph g=new Graph(vertices, edges, weights);
        return g;
	}
	public static void main(String[] args) {
		//Enter the path to the ibm01.netD file on the CD in the path field between the quotation marks.
		//Please type it in the following format: "C:\\Testing\\ibm01.netD"
		System.out.println("Enter the path to the ibm01.netD file on the CD in the path field between the quotation marks.");
		System.out.println("Please type it in the following format: \"C:\\Testing\\ibm01.netD\"");
		CircuitReader cr=new CircuitReader();
		String path="C:\\Users\\Stanimir\\Dropbox\\Year 3\\Third Year Project\\Testing\\ibm01.netD";
		System.out.println("About to generate graph from the .netD file in location "+path);
		Graph g1=cr.generateCircuit(path);
		System.out.println("Graph generated:");
		System.out.println("Number of vertices:" +g1.getVertices().size());
		System.out.println("Number of edges: "+g1.getEdges().size());
	}

}
