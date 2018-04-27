package structures;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


import apps.*;

public class Driver {
	public static void main(String[] args) {
		try {
			Graph g = new Graph("graph1.txt");
			PartialTreeList L = MST.initialize(g);
			
			printPtList(L);
			
			ArrayList<PartialTree.Arc> answer = MST.execute(L);
			System.out.println();
			for(PartialTree.Arc arc : answer) {
				System.out.println(arc);
			}
			
			
			
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}
	
	public static void printPtList(PartialTreeList L) {
		Iterator<PartialTree> iter = L.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
		System.out.println();
	}
}
