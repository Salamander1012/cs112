package lse;

import java.io.FileNotFoundException;

public class Driver {
	public static void main(String[] args) {
		LittleSearchEngine google = new LittleSearchEngine();
		try {
			google.makeIndex("docs.txt", "noisewords.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}