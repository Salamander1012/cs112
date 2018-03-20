package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	private String getTagName(String s) {
		s = s.replaceAll("<", "");
		//s = s.replaceAll("</", "");
		s = s.replaceAll("/", "");
		s = s.replaceAll(">", "");
		return s;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		Stack<TagNode> stack = new Stack<TagNode>();
		TagNode ptr = root;
		while(sc.hasNext()) {
			String line = sc.nextLine();
			TagNode t = new TagNode(getTagName(line), null, null);
			if(ptr == null) {
				root = t;
				ptr = root;
				stack.push(ptr);
				continue;
			}
			if(line.contains("<") && !line.contains("</")) {
				if(stack.peek().firstChild==null) {
					ptr.firstChild = t;
					ptr = ptr.firstChild;
					stack.push(ptr);
				} else {
					ptr.sibling = t;
					ptr = ptr.sibling;
					stack.push(ptr);
				}
			} else if (!line.contains("<") && !line.contains("</")) {
				if(ptr.firstChild!=null) {
					ptr.sibling = new TagNode(line, null, null);
					ptr = ptr.sibling;
				} else {
					ptr.firstChild = new TagNode(line, null, null);
					ptr = ptr.firstChild;
				}
			} else {
				ptr = stack.pop();
			}
		}

		
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root;
		replaceTraversal(ptr, oldTag, newTag);
		
	}
	
	private void replaceTraversal(TagNode t, String oldTag, String newTag) {
		for(TagNode ptr = t; ptr!=null; ptr = ptr.sibling) {
			if(ptr.tag.equals(oldTag)) {
				ptr.tag = newTag;
				System.out.println("repalced");
			}
			if(ptr.firstChild!=null) {
				replaceTraversal(ptr.firstChild, oldTag, newTag);
			}
		}
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		if(root.tag.equals(tag)) {
			root = root.firstChild;
		}
		removeTagTraversal(root, tag);
	}
	
	private TagNode removeTagTraversal(TagNode t, String target) {
		if (t==null) return t;
	
		if(t.tag.equals(target)) {
			TagNode tempSib = t.sibling;
			t = t.firstChild;
			t.sibling = tempSib;
			t.firstChild = removeTagTraversal(t.firstChild, target);
			t.sibling = removeTagTraversal(t.sibling, target);
		} else {
			t.firstChild = removeTagTraversal(t.firstChild, target);
			t.sibling = removeTagTraversal(t.sibling, target);
		}
		
		return t;
	
		
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}