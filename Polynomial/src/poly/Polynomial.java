package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	
	
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node sum = null;
		while(ptr1!=null || ptr2!=null) {
			//if two terms are present
			if(ptr1!=null && ptr2!=null) {
				//if they have the same degree
				if(ptr1.term.degree == ptr2.term.degree) {
					//create a new Node
					float newCoeff = ptr1.term.coeff + ptr2.term.coeff;
					int newDegree = ptr1.term.degree;
					Node node = new Node(newCoeff, newDegree, null);
					//check if coeff is 0 so that you can make node null
					if(newCoeff==0.0) {
						node = null;
					}
					//if sum doesnt have any terms
					if(sum == null) {
						sum = node;
					} else { //traverse sum so you can add the new node to the end
						Node ptr = sum;
						while(ptr.next!=null) {
							ptr = ptr.next;
						}
						ptr.next = node;
					}
					
					//traverse ptr1 and ptr2
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
					
				} else if (ptr1.term.degree > ptr2.term.degree) {
					Node node = new Node(ptr2.term.coeff, ptr2.term.degree, null);
					if(sum == null) {
						sum = node;
					} else {
						Node ptr = sum;
						while(ptr.next!=null) {
							ptr = ptr.next;
						}
						ptr.next = node;
					}
				
					ptr2 = ptr2.next;
				} else {
					
					Node node = new Node(ptr1.term.coeff, ptr1.term.degree, null);
					if(sum == null) {
						sum = node;
					} else {
						Node ptr = sum;
						while(ptr.next!=null) {
							ptr = ptr.next;
						}
						ptr.next = node;
					}

					ptr1 = ptr1.next;
				}
			} else if (ptr1!= null) {
			
				Node node = new Node(ptr1.term.coeff, ptr1.term.degree, null);
				if(sum == null) {
					sum = node;
				} else {
					Node ptr = sum;
					while(ptr.next!=null) {
						ptr = ptr.next;
					}
					ptr.next = node;
				}

				ptr1 = ptr1.next;
			} else {

				Node node = new Node(ptr2.term.coeff, ptr2.term.degree, null);
				if(sum == null) {
					sum = node;
				} else {
					Node ptr = sum;
					while(ptr.next!=null) {
						ptr = ptr.next;
					}
					ptr.next = node;
				}

				ptr2 = ptr2.next;
			}
		}
		
		return sum;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	
	/*
	 * (4x^2 + 3x + 4)(3x^2 + 2x + 1)
	 * (12x^4 + 8x^3 + 4x^2)
	 * 
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node sum = null;
		for(Node ptr1 = poly1; ptr1!=null; ptr1 = ptr1.next) {
			for(Node ptr2 = poly2; ptr2!=null; ptr2 = ptr2.next) {
				Node node = new Node(ptr1.term.coeff*ptr2.term.coeff, ptr1.term.degree+ptr2.term.degree, null);
				if(sum==null) {
					sum = node;
				} else {
					sum = add(sum, node);
				}
			}		
		}
		return sum;
		
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		float sum = 0;
		for(Node ptr = poly; ptr!=null; ptr = ptr.next) {
			sum += ptr.term.coeff * Math.pow(x, ptr.term.degree);
		}
		return sum;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
