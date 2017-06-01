package help;

import java.util.Random;
/**
 * @author Stanimir
 *Generates random name for vertex
 */
public class VertexNameGenerator implements Cloneable {

	private final String letters="abcdefghijklmnopqrstuvwxyz0123456789";

	private final int numLetters=letters.length();

	private int nameSize;

	private Random randGenerator;

	public VertexNameGenerator(int nameSize) {
		super();
		this.nameSize = nameSize;
		this.randGenerator=new Random();
	}

	/**
	 * @return
	 * generates random name
	 */
	public String getRandomName(){
		String name="";
		for (int i=0; i<nameSize; i++){
			int index=randGenerator.nextInt(numLetters);
			char c=letters.charAt(index);
			name=name+c;
		}
		return name;
	}


	public static void main(String[] args) {
		VertexNameGenerator vng=new VertexNameGenerator(4);
		System.out.println(vng.getRandomName());
	}

}
