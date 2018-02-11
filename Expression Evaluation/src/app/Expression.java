package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    		expr = expr.replaceAll("\\s+",""); //removes white spaces
    		String[] terms = expr.split("[-+*/()\\]]"); //splits at operators, parenthesis and closed bracket
    		for(int i = 0; i<terms.length; i++) {
    			//System.out.println(terms[i]);
    			if(!terms[i].equals("") || !terms[i].equals(" ")) {
    				//checks if string is an Int, if not lets does something in the catch
    				try {
    					Float.parseFloat(terms[i]);
    				} catch (NumberFormatException e) {
    					
    					if(terms[i].contains("[")) { //checks if term is an array
    						int nameStart = 0;
    						int nameEnd = 0;
    						for(int j = 0; j<terms[i].length(); j++) {
    							if(terms[i].charAt(j)=='[') {
    								nameEnd = j;
    								arrays.add(new Array(terms[i].substring(nameStart, nameEnd)));
    								nameStart = j+1;
    							}
    						}
    					} else { //if term is not an array
    						//System.out.println("var: " + terms[i]);
    						vars.add(new Variable(terms[i]));
   					}

    				}
    			}
    		}
    		
//    		System.out.println("vars:");
//    		for(int i = 0; i<vars.size(); i++) {
//    			System.out.println(vars.get(i).name);
//    		}
//    		
//    		System.out.println("arrays:");
//    		for(int i = 0; i<arrays.size(); i++) {
//    			System.out.println(arrays.get(i).name);
//    		}
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    private static boolean isNum(String s) {
    		try {
    			Float.parseFloat(s);
    			return true;
    		} catch (NumberFormatException e) {
    			return false;
    		}
    }
    
    private static boolean isOperator(char c) {
    		if (c == '+' || c == '-' || c == '*' || c == '/') {
    			return true;
    		}
    		return false;
    }
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    		/** COMPLETE THIS METHOD **/
    		expr = expr.replaceAll("\\s+",""); //removes all spaces
    		
    		//System.out.println("eval(" + expr + ")");
    		
    		/*BASE CASES*/
    		Variable var = new Variable(expr);
    		if(vars.contains(var)) { //check if its a var
    			return vars.get(vars.indexOf(var)).value;
    		} else if(isNum(expr)) { //check if its a int
    			return Float.parseFloat(expr);
    		} 
    		
//    		else if(expr.contains("[")) { //checks if its an array
//    			Array arr = new Array(expr.substring(0, expr.indexOf('[')));
//    			System.out.println(arr.name);
//    			if(arrays.contains(arr)) {
//    				Array arrFromList = arrays.get(arrays.indexOf(arr));
//    				String innerBracket = expr.substring(expr.indexOf('[')+1, expr.length()-1);
//    				System.out.println(innerBracket);
//    				return arrFromList.values[(int)evaluate(innerBracket, vars, arrays)];
//    				
//    			}
//    		}
    		
    		
    		
    		
    		//handles parentheses
    		int innerParenStart = 0;
    		int innerParenEnd = 0;
    		String innerParen = "";
    		int parenLayers = 0;
    		for(int i=0; i<expr.length(); i++) {
    			if(expr.charAt(i)=='(') {
    				parenLayers++;
    				if(parenLayers==1) innerParenStart=i+1;
    			}
    			if(expr.charAt(i)==')') {
    				parenLayers--;
    				if(parenLayers==0) {
    					innerParenEnd=i-1;
    					float evaluatedParen = evaluate(expr.substring(innerParenStart, innerParenEnd+1), vars, arrays);
    					String resultString = expr.substring(0, innerParenStart-1) + evaluatedParen + expr.substring(innerParenEnd+2);
    					return evaluate(resultString, vars, arrays);
    				} 
    			}
    		}
    		
    		//handles bracket
    		int innerBracketStart = 0;
    		int innerBracketEnd = 0;
    		String innerContent = "";
    		int bracketLayers = 0;
    		int arrNameStart = 0;
    		int arrNameEnd = 0;
    		boolean inBracket = false;
    		for(int i = 0; i<expr.length(); i++) {
    			if(isOperator(expr.charAt(i)) && !inBracket) arrNameStart = i+1;
    			if(expr.charAt(i)=='[') {
    				//System.out.println("opening bracket at index: " + i);
    				bracketLayers++;
    				if(bracketLayers==1) {
    					inBracket = true;
    					innerBracketStart=i+1;
    					arrNameEnd = i;
    				}
    			}
    			if(expr.charAt(i)==']') {
    				//System.out.println("closing bracket at index: " + i);
    				bracketLayers--;
    				if(bracketLayers==0) {
    					innerBracketEnd=i-1;
    					float evaluatedInnerBracket = evaluate(expr.substring(innerBracketStart, innerBracketEnd+1), vars, arrays);
    					String arrName = expr.substring(arrNameStart, arrNameEnd);
    					Array arr = new Array(arrName);
    					
    					float arrValue = arrays.get(arrays.indexOf(arr)).values[(int)evaluatedInnerBracket];
    					String resultString = expr.substring(0, arrNameStart) + arrValue + expr.substring(i+1);
    					return evaluate(resultString, vars, arrays);
    				} 
    			}
    		}
    		
    		
    		//separates expression at addition and subtraction signs
    		//for(int i = 0; i<expr.length(); i++) {
    		for(int i = expr.length()-1; i>=0; i--) {
			if(isOperator(expr.charAt(i))) {
				if(expr.charAt(i) == '-') {
					return evaluate(expr.substring(0, i), vars, arrays) - evaluate(expr.substring(i+1), vars, arrays);
				}
				if(expr.charAt(i) == '+') {
					return evaluate(expr.substring(0, i), vars, arrays) + evaluate(expr.substring(i+1), vars, arrays);
				} 
			}
		}
    		
    		//solves multiplication and division cases
    		for(int i = expr.length()-1; i>=0; i--) {
    			if(isOperator(expr.charAt(i))) {
    				if(expr.charAt(i) == '/') {
    					return evaluate(expr.substring(0, i), vars, arrays) / evaluate(expr.substring(i+1), vars, arrays);
    				} 
    				if(expr.charAt(i) == '*') {
    					return evaluate(expr.substring(0, i), vars, arrays) * evaluate(expr.substring(i+1), vars, arrays);
    				}
    			}
    		}
    		
    		
    		
    		
    		return 0;
    }
}
