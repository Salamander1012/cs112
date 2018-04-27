package apps;

import structures.*;
import java.util.ArrayList;



public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
	
		/* COMPLETE THIS METHOD */
		PartialTreeList list = new PartialTreeList();
		
		for (int i=0; i < graph.vertices.length; i++) {
			PartialTree tree = new PartialTree(graph.vertices[i]);
			Vertex.Neighbor ptr = graph.vertices[i].neighbors;
			while(ptr !=null) {
				PartialTree.Arc arc = new PartialTree.Arc(graph.vertices[i], ptr.vertex, ptr.weight);
				tree.getArcs().insert(arc);
				ptr = ptr.next;
			}
			list.append(tree);
		}
	
		return list;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		

			
			PartialTree PTX = ptlist.remove();
//			System.out.println(PTX);
			MinHeap<PartialTree.Arc> PQX = PTX.getArcs();
			
			PartialTree.Arc a = PQX.getMin();
//			System.out.println(a);
			
			Vertex v1 = a.v1;
			Vertex v2 = a.v2;
			
//			if(ptContainsVertex(PTX, v2)) {
//				
//			}
			PartialTree PTY = ptlist.removeTreeContaining(v2);
//			System.out.println(PTY);
			MinHeap<PartialTree.Arc> PQY = PTY.getArcs();
			
			
			PTY.merge(PTX);
			
			ptlist.append(PTY);
			
		
		
		return null;
	}
	
	private static boolean ptContainsVertex(PartialTree PTX, Vertex v2) {
		Vertex ptr = PTX.getRoot();
		while(ptr != null) {
			if(ptr == v2) {
				return true;
			}
			ptr = ptr.parent;
		}
		return false;
	}
}
