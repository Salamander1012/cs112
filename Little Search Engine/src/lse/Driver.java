package lse;

import java.io.FileNotFoundException;
import java.util.*;

public class Driver {
	public static void main(String[] args) {
		LittleSearchEngine google = new LittleSearchEngine();
		try {
			google.makeIndex("docs.txt", "noisewords.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		ArrayList<Integer> arr = new ArrayList<Integer>();
//		arr.add(10);
////		arr.add(9);
////		arr.add(8);
////		arr.add(6);
////		arr.add(5);
//		
//		google.insertInRightOrder(arr, 11);
	}
}