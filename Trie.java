package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode root = new TrieNode(null, null, null);
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		for (int i = 0; i < allWords.length; i++) {
			String word = allWords[i];
			insertNode(root, word, i, allWords);
		}
		
		return root;
	}
	
	private static int prefixCompare(String prefix, String word) {
		int indexLast = -1;
		int length = 0;
		
		if (prefix.length() > word.length()) {
			length = word.length();
		} else {
			length = prefix.length();
		}
		
		for (int j = 0; j < length; j++) {
			if ((word.substring(0, j+1).equals(prefix.substring(0,j+1)))) {
				indexLast++;
			}
		}
		
		return indexLast;
	}
	
	private static void insertNode(TrieNode subroot, String substr, int index, String[] allWords) {
		short substring = 0;
		short lastPosition = (short) (substr.length() -1);
		
		if (subroot.firstChild == null) { // for first node added
			Indexes newIndexes = new Indexes(index, substring, lastPosition);
			TrieNode insertNode = new TrieNode(newIndexes, null, null);
			
			subroot.firstChild = insertNode;
		} else {
			recursiveInsertNode(subroot, "", substr, index, allWords);
		}
		
	}	
	
	private static void recursiveInsertNode(TrieNode subroot, String prefix, String word, int index, String[] allWords) {
		
		TrieNode ptr = subroot.firstChild; TrieNode prevSibling = subroot;
		short substring = 0;
		short lastPosition = (short) (word.length() -1);

		
		while(ptr != null) {
			int currentPtrIndex = ptr.substr.wordIndex;
			short startWord = ptr.substr.startIndex;
			short lastWord = ptr.substr.endIndex;
			
			String substringPtr = allWords[currentPtrIndex].substring(substring, lastWord+1); // get current substring
			
			int endIndex = prefixCompare(substringPtr, word);

			if (endIndex > -1 && endIndex == ptr.substr.endIndex) {
				   
	               recursiveInsertNode(ptr, substringPtr, word, index, allWords);
	               return;

			} else if (endIndex > -1 && endIndex < ptr.substr.endIndex) {
				
               String s = word.substring(0, endIndex+1);
               
               if(prefix.indexOf(s) == -1) {
                   TrieNode temp = new TrieNode(new Indexes(currentPtrIndex,(short)(endIndex+1), lastWord),ptr.firstChild,null);
                   ptr.substr = new Indexes(currentPtrIndex,startWord,(short)endIndex);
                   ptr.firstChild = temp;
                   temp.sibling = new TrieNode(new Indexes(index,(short)(endIndex+1),lastPosition), null,null);
                   return;
               }	
			}
			
			prevSibling = ptr;
			ptr = ptr.sibling;
		}
		
		if (subroot.substr != null) {
			substring = (short)(subroot.substr.endIndex+1);
			prevSibling.sibling = new TrieNode(new Indexes(index,substring,lastPosition),null,null);
		} else {
			prevSibling.sibling = new TrieNode(new Indexes(index,substring,lastPosition),null,null);
		}
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		TrieNode ptr = root.firstChild;
		
		ArrayList<TrieNode> results = new ArrayList<TrieNode>();
		
		while(ptr != null) {
			
			String substringPtr = allWords[ptr.substr.wordIndex].substring(0, ptr.substr.endIndex+1);
		
			int endIndex = prefixCompare(prefix, substringPtr);
		
			if (endIndex > -1 && endIndex == ptr.substr.endIndex) {
				recursiveAddListItem(ptr, allWords, prefix, results);
			}
			
			ptr = ptr.sibling;
			
		}
		
		if (results.size() == 0) {
			return null;
		}
		
		return results;
	}
	
	private static void recursiveAddListItem(TrieNode subroot, String[] allWords, String prefix, ArrayList<TrieNode> results) {
		TrieNode ptr = subroot.firstChild;
		
		if (ptr == null) {
			results.add(subroot);
		}
		
		while (ptr != null) {
			String substringPtr = allWords[ptr.substr.wordIndex].substring(0, ptr.substr.endIndex+1);
		
			int endIndex = prefixCompare(prefix, substringPtr);
			
			if (substringPtr.substring(0, endIndex+1).equals(prefix) || prefix.substring(0, endIndex+1).equals(substringPtr)) {
				recursiveAddListItem(ptr, allWords, prefix, results);
			} 
			
			ptr = ptr.sibling;
		}
		
	}
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }