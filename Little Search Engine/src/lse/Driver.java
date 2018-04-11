package lse;

import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class Driver {
	public static void main(String[] args) {
		LittleSearchEngine google = new LittleSearchEngine();
		try {
			google.makeIndex("testdocs.txt", "noisewords.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("-------");
		for(String k : google.keywordsIndex.keySet()) {
			System.out.print("'" + k + "'" + ": ");
			for(Occurrence o : google.keywordsIndex.get(k)) {
				System.out.print(o + " -> ");
			}
			System.out.println();
			System.out.println();
		}
		
		ArrayList<String> top5 = google.top5search("world", "deep");
		
		for(String doc : top5) {
			System.out.println(doc);
		}
		
		

	}
}