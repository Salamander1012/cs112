package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		HashMap<String,Occurrence> map = new HashMap<String,Occurrence>();
		Scanner sc = new Scanner(new File(docFile));
		while(sc.hasNext()) {
			String word = sc.next();
			word = getKeyword(word);
			if(word == null) {
				continue;
			}
			
			if(!map.containsKey(word)) {
				Occurrence o = new Occurrence(docFile, 1);
				map.put(word, o);
			} else {
				map.get(word).frequency++;
			}
		}
		
		if(map.containsKey("")) {
			map.remove("");
		}
		
		return map;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/

		for (String s : kws.keySet()) {
			
			if(keywordsIndex.containsKey(s)) {
				keywordsIndex.get(s).add(kws.get(s));
//				System.out.println(s);
				insertLastOccurrence(keywordsIndex.get(s));
			} else {
				ArrayList<Occurrence> os = new ArrayList<Occurrence>();
				os.add(kws.get(s));
				keywordsIndex.put(s, os);
			}
			
			
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		word = removeLeadingAndTrailingPunctuations(word);
		word = word.toLowerCase();
		
		if(containsPunctuations(word)) {
			return null;
		}
		
		if(isNoiseWord(word)) {
			return null;
		}
		
		return word;
	}
	
	private boolean isNoiseWord(String s) {
		if(noiseWords.contains(s)) {
			return true;
		}
		return false;
	}
	
	private String removeLeadingAndTrailingPunctuations(String word) {
		String cleanWord = word;
		
		int lastLeadingPuncIndex = -1;
		for(int i = 0; i<word.length(); i++) {
			if(isPunctuation(word.charAt(i))) {
				lastLeadingPuncIndex = i;
			} else {
				break;
			}
		}
		cleanWord = cleanWord.substring(lastLeadingPuncIndex + 1);
		
		int firstTrailingPuncIndex = cleanWord.length();
		for(int i = cleanWord.length()-1; i>=0; i--) {
			if(isPunctuation(cleanWord.charAt(i))) {
				firstTrailingPuncIndex = i;
			} else {
				break;
			}
		}
		
		if(cleanWord.length()==1) {
			cleanWord = cleanWord.substring(firstTrailingPuncIndex-1);
		} else {
			cleanWord = cleanWord.substring(0, firstTrailingPuncIndex);
		}
		
		return cleanWord;
	}
	
	private boolean containsPunctuations(String word) {
		for(int i=0; i<word.length(); i++) {
			if(isPunctuation(word.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	private boolean isPunctuation(char c) {
		if (c == '.' || c == ',' || c == '?' || c == ':' || c == '!' || c == '\'' || c == '"' || c == '(' || c == ')' || c == '_' || c == '-' || c == '[' || c == ']' || c == ';') {
			return true;
		}
		return false;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		
//		for(Occurrence k : occs) {
//			System.out.print(k+ " ");
//		}
//		System.out.println();
		if(occs.size() == 1) {
			return null;
		}
		ArrayList<Integer> mids = new ArrayList<Integer>();
		
		Occurrence lastOccurrence = occs.get(occs.size()-1);
		occs.remove(lastOccurrence);
		int correctIndex = -1;
		int l = 0, r = occs.size() - 1;
        while (l <= r)	{
            int m = l + (r-l)/2;
            mids.add(m);
            
            if (occs.get(m).frequency == lastOccurrence.frequency) {
                break;
            }
            
            if (occs.get(m).frequency > lastOccurrence.frequency) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        
        correctIndex = l + (r-l)/2;
		
		occs.add(correctIndex, lastOccurrence);
		
//		for(Occurrence k : occs) {
//			System.out.print(k+ " ");
//		}
//		System.out.println();
//		System.out.println();
		
		return mids;
	}

	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
		ArrayList<String> docs = new ArrayList<String>();
		if(this.keywordsIndex.containsKey(kw1)) {
			for(Occurrence o : this.keywordsIndex.get(kw1)) {
				occs.add(o);
				insertLastOccurrence(occs);
			}
		}
		if(this.keywordsIndex.containsKey(kw2)) {
			for(Occurrence o : this.keywordsIndex.get(kw2)) {
				occs.add(o);
				insertLastOccurrence(occs);
			}
		}
		
		for(Occurrence o : occs) {
			if(!docs.contains(o.document)) {
				docs.add(o.document);
			}
		}
		
		if(docs.size()<=5) {
			return docs;
		} else {
			ArrayList<String> top5Docs = new ArrayList<String>();
			for(int i = 0; i<5; i++) {
				top5Docs.add(docs.get(i));
			}
			return top5Docs;
		}
		
		

	
	}
}
