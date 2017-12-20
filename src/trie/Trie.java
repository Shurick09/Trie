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
		TrieNode root = new TrieNode(null,null,null);
		Indexes firstIndex = new Indexes(0, (short)0, (short)(allWords[0].length() - 1)); 
		root.firstChild = new TrieNode(firstIndex,null, null);
		if (allWords.length == 1) {
			return root;
		}
		for (int i = 1; i < allWords.length; i++) {
			root = add(root, allWords[i], allWords, i);
		}
		return root;
	}
	
	private static TrieNode add(TrieNode root, String word, String[] allWords, int i) {
		int imp = 0;
		int start = 0;
		TrieNode ptr = root;
		ptr = ptr.firstChild;
		ptr = root;
		ptr = ptr.firstChild;
		while (true) {
			while (ptr.firstChild == null) {
				if (compareToB(word, allWords[ptr.substr.wordIndex], ptr.substr.startIndex)) {
					int tempEnd = ptr.substr.endIndex;
					ptr.substr.endIndex = (short)((compareToS(word, allWords[ptr.substr.wordIndex], ptr.substr.startIndex) + imp - 1));
					Indexes ind1 = new Indexes(ptr.substr.wordIndex, (short)(compareToS(word, allWords[ptr.substr.wordIndex], ptr.substr.startIndex) + imp), (short)tempEnd);
					Indexes ind2 = new Indexes(i, (short)(compareToS(word, allWords[ptr.substr.wordIndex], ptr.substr.startIndex) + imp), (short) (word.length() - 1));
					ptr.firstChild = new TrieNode(ind1,null, null);
					ptr.firstChild.sibling = new TrieNode(ind2,null,null);
					return root;
				}
				else {
					if (ptr.sibling == null) {
						Indexes temp = new Indexes(i, (short)start, (short)(word.length() - 1));
						ptr.sibling = new TrieNode(temp,null,null);	
						return root;
					}
					else {
						ptr = ptr.sibling;
					}
				}
			}
			while (ptr.firstChild != null) {
				if (allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1).equals((word.substring(ptr.substr.startIndex, ptr.substr.endIndex + 1)))) {
					imp = ptr.substr.endIndex + 1;
					ptr = ptr.firstChild;
				}
				/*
				 * Worked on for specific case, but could not finish before due time
				else if (compareToS(word,allWords[ptr.substr.wordIndex],0) != 0) {
					Indexes ind4 = ptr.firstChild.substr;
					ind4.startIndex = (short)(ind4.startIndex + (compareToS(word,allWords[ptr.substr.wordIndex],0)));
					Indexes ind5 = ptr.firstChild.sibling.substr;
					ind5.startIndex = (short)(ind5.startIndex + (compareToS(word,allWords[ptr.substr.wordIndex],0)));
					ptr.firstChild.substr.endIndex = ptr.substr.endIndex;
					ptr.substr.endIndex = (short)(compareToS(word,allWords[ptr.substr.wordIndex],0) - 1);
					Indexes ind3 = new Indexes(i, (short)(compareToS(word, allWords[ptr.substr.wordIndex], ptr.substr.startIndex) + imp), (short) (word.length() - 1));
					ptr.firstChild.sibling.substr = ind3;
					ptr.firstChild.firstChild = new TrieNode(ind4,null,null);
					ptr.firstChild.firstChild.sibling = new TrieNode(ind5,null,null);	
					return root;
				}
				*/
				else {
					if (ptr.sibling == null){
						Indexes ind3 = new Indexes(i,(short)(imp + 1),(short)(allWords[i].length() - 1));
						ptr.sibling = new TrieNode(ind3, null, null);
						return root;
					}
					else {
						ptr = ptr.sibling;
					}
				}
				
			}
		}
	}
	
	private static boolean compareToB(String a, String b, int x) {
		if (a.charAt(x)== b.charAt(x)) {
			return true;
		}
		return false;
	}
	
	private static int compareToS(String a, String b, int x) {
		int c = 0;
		String small = "";
		if (a.length() <= b.length()) {
			small = a;
		}
		else {
			small = b;
		}
		for (int i = x; i < small.length(); i++) {
			if (a.charAt(i) == b.charAt(i)) {
				c++;
			}
			else {
				break;
			}
		}
		return c;
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
		TrieNode ptr = root;
		ptr = ptr.firstChild;
		ArrayList<TrieNode> matches = new ArrayList<TrieNode>();
		while(true) {
			while(ptr.firstChild == null) {
				if (allWords[ptr.substr.wordIndex].startsWith(prefix)) {
					matches.add(ptr);
				}
				if (ptr.sibling != null) {
					ptr = ptr.sibling;
				}
				else {
					if (matches.isEmpty()) {
						return null;
					}
					else {
						return matches;
					}
				}
			}
			while (ptr.firstChild != null) {
				if (prefix.length() > ptr.substr.endIndex + 1) {
					if (prefix.substring(0, ptr.substr.endIndex + 1).equals(allWords[ptr.substr.wordIndex].substring(0, ptr.substr.endIndex + 1))) {
						matches.addAll(completionList(ptr, allWords, prefix));
						if (ptr.sibling != null) {
							ptr = ptr.sibling;
						}
						else {
							if (matches.isEmpty()) {
								return null;
							}
							else {
								return matches;
							}
						}
					}
					else {
						if (ptr.sibling != null) {
							ptr = ptr.sibling;
						}
						else {
							if (matches.isEmpty()) {
								return null;
							}
							else {
								return matches;
							}
						}
					}
				}
				else {
					if (prefix.equals(allWords[ptr.substr.wordIndex].substring(0, prefix.length()))) {
						matches.addAll(completionList(ptr, allWords, prefix));
						if (ptr.sibling != null) {
							ptr = ptr.sibling;
						}
						else {
							if (matches.isEmpty()) {
								return null;
							}
							else {
								return matches;
							}
						}
					}
					else {
						if (ptr.sibling != null) {
							ptr = ptr.sibling;
						}
						else {
							if (matches.isEmpty()) {
								return null;
							}
							else {
								return matches;
							}
						}
					}
				}
			}	
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
