package structures;
import java.io.IOException;
import java.util.Iterator;


import apps.*;

public class Driver {
	public static void main(String[] args) {
		try {
			Graph g = new Graph("graph2.txt");
			PartialTreeList L = MST.initialize(g);
			
			Iterator<PartialTree> iter = L.iterator();
			while (iter.hasNext()) {
				System.out.println(iter.next());
			}
			System.out.println();
			
			
			MST.execute(L);
			
			
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}
}
