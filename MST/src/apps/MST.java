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
			
			ArrayList<PartialTree.Arc> list = new ArrayList<PartialTree.Arc>();
			ArrayList<PartialTree.Arc> used = new ArrayList<PartialTree.Arc>();
			
			while(ptlist.size() > 1) {
				System.out.println(ptlist.size());
				
				//Step 3
				PartialTree PTX = ptlist.remove();
				System.out.println("PTX. "+PTX);
				MinHeap<PartialTree.Arc> PQX = PTX.getArcs();
				
				//Step 4
				PartialTree.Arc a = PQX.deleteMin();
				used.add(a);
				System.out.println("a: "+ a + "   v1: " + a.v1 + "   v2: " + a.v2);
				
				//Step 5
				while(a.v2.name.equals(PTX.getRoot().name) || inUsed(a, list)) {
					a = PQX.deleteMin();
				}
				
				Vertex v1 = a.v1;
				Vertex v2 = a.v2;
				
				//Step 6
				list.add(a);
				
				//Step 7
				System.out.println("partial tree containing " + v2);
				PartialTree PTY = ptlist.removeTreeContaining(v2);
				System.out.println("PTY. "+PTY);
				MinHeap<PartialTree.Arc> PQY = PTY.getArcs();
				
				//Step 8
				PTX.merge(PTY);
				System.out.println("MERGED: " + PTX);
				ptlist.append(PTX);
				
				System.out.println();

				
			}
		
		return list;
	}
	
	private static boolean inUsed(PartialTree.Arc a, ArrayList<PartialTree.Arc> used) {
		for(PartialTree.Arc arc : used) {
			if(arc.v1.name.equals(a.v1.name) && arc.v2.name.equals(a.v2.name)) {
				return true;
			} else if (arc.v1.name.equals(a.v2.name) && arc.v2.name.equals(a.v1.name)) {
				return true;
			}
		}
		
		return false;
	}
	
}
