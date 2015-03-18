import java.io.Serializable;
import java.util.ArrayList;

/** An instance of Index represents an indexed list of name, score pairs */
public class Index implements Serializable {

	private static final long serialVersionUID = -4302167588789784267L;
	// valid java variables start with letters, numbers, '$' or '_'
	final static int start = '$'; // first possible char
	final static int end = 'z'; // last possible char
	final static int char_space = end - start + 1; // size of array of character space
	private Node root = new Node();
	public int size = 0; // size of the index

	/**
	 * add string n with score s
	 * 
	 * @param n
	 *            name
	 * @param s
	 *            score
	 */
	void add(String n, int s) {
		Datum d = new Datum(n, s);

		String[] strings = generateStrings('_', n);
		size++; // started inserting a new string
		for (int i = 0; i < strings.length; i++) {

			int undersCount = countPrefix('_', strings[i]);
			String stripped = stripPrefix('_', strings[i]);

			addhelper(stripped, d, root, undersCount);
		}
	}

	/**
	 * recursive body of add
	 * 
	 * @param s
	 *            substring to add
	 * @param d
	 *            datum pair to add
	 * @param n
	 *            current node
	 * @param undersCount
	 *            number of underscores of datum
	 */
	private void addhelper(String s, Datum d, Node n, int undersCount) {
		if (s.equals(""))
			return;
		int firstLetter = s.charAt(0);
		int ind = firstLetter - start;
		//check that a node exists at position
		if (n.children[ind] == null) {
			n.children[ind] = new Node((char) firstLetter);
		}
		
		//make sure node was not already visited by string set
		if (!n.children[ind].isVisited(size, undersCount)) {
			n.children[ind].addtoRank(d, undersCount);
			n.children[ind].visit(size, undersCount);
		}
		addhelper(s.substring(1), d, n.children[ind], undersCount);

	}

	/**
	 * search the index for string s
	 * 
	 * @param s
	 *            string to search
	 * @return
	 */
	public Datum[] search(String s) {
		if (s == null || s.equals(""))
			return null;
		String stripped = stripPrefix('_', s);
		int undersCount = countPrefix('_', s);

		Node n = searchHelper(stripped, root);
		if (n == null) {
			return null;
		} else {
			return n.getRank(undersCount);
		}
	}

	/**
	 * recursive body of search
	 * 
	 * @param s
	 *            substring to search
	 * @param n
	 *            current node being traversed
	 * @return
	 */
	private Node searchHelper(String s, Node n) {
		if (n == null)
			return null;
		if (s.equals(""))
			return n;
		int firstLetter = s.charAt(0);
		return searchHelper(s.substring(1), n.children[firstLetter - start]);
	}

	/**
	 * search the index for a string s and return the result as a string
	 * 
	 * @param s
	 *            string to search
	 * @return
	 */
	public String searchToString(String s) {
		Datum[] result = search(s);
		if (result == null)
			return "[]";

		String toreturn = result[0] + "";
		for (int i = 1; i < result.length; i++) {
			if (result[i] != null)
				toreturn += "," + result[i].toString();
		}
		return "[" + toreturn + "]";
	}

	/*
	 * modified from
	 * http://stackoverflow.com/questions/6179192/how-to-get-all-the
	 * -occurrence-character-on-the-prefix-of-string-in-java-in-a-sim
	 */
	/**
	 * counts the occurrences of a prefix p in string s
	 * 
	 * @param p
	 *            prefix
	 * @param s
	 *            string
	 * @return
	 */
	public static int countPrefix(char p, String s) {
		int count = 0;
		for (char d : s.toCharArray()) {
			if (d == p)
				count++;
			else
				break;
		}
		return count;
	}

	/*
	 * modified from
	 * http://stackoverflow.com/questions/6179192/how-to-get-all-the
	 * -occurrence-character-on-the-prefix-of-string-in-java-in-a-sim
	 */
	/**
	 * strips the prefix containing all letters p in string s. If s contains
	 * only letters p, returns a single p.
	 * 
	 * @param p
	 *            prefix
	 * @param s
	 *            string to search in
	 * @return
	 */
	public static String stripPrefix(char p, String s) {
		if (s.equals(""))
			return s;
		int length = 0;
		while (length < s.length() && s.charAt(length) == p) {
			length++;
		}
		if (length == s.length())
			return p + "";
		else
			return s.substring(length);
	}
	
	/**
	 * Pre-generate a list of substrings based on delimeter excludes first
	 * occurrence of delimeter in each substring
	 * 
	 * @param delim
	 *            delimeter
	 * @param s
	 *            string to generate from
	 * @return
	 */
	public static String[] generateStrings(char delim, String s) {
		// todo: maybe optimize for strings that dont have delim
		ArrayList<String> list = new ArrayList<String>();
		int nextUnders = 0;
		do {
			if (!s.equals("")) {
				list.add(s);
				s = stripPrefix('_', s);
				nextUnders = s.indexOf("_");
				//NOTE: should have +1 if search is first '_' exclusive
				s = s.substring(nextUnders + 1);
			} else
				break;
		} while (nextUnders != -1);

		String[] strings = new String[list.size()];
		return list.toArray(strings);
	}
	

	/**
	 * An instance of Node represents a node in the trie corresponding to the
	 * first letter
	 **/
	class Node implements Serializable {

		private static final long serialVersionUID = -5309925796811559500L;
		int _visitedBy = -1; // last string that visited the node
		int _visitedUnders = -1; // last number of underscores of string that
									// visited node
		final static int maxranksize = 10; // max top score count
		char letter; // letter represented by node
		Node[] children = new Node[char_space];
		//max # of prefix underscores, max size not known ahead of time
		ArrayList<Datum[]> underscores = new ArrayList<Datum[]>(); 

		public Node(){
		}
		
		public Node(char l) {
			letter = l;
		}

		/**
		 * Determines if a node has been visited with current index size and
		 * underscore count. compare size with internal _visitedBy and
		 * _visitedUnders. if false, reset the underscore count.
		 * 
		 * @param visitor
		 * @param unders
		 * @return
		 */
		public boolean isVisited(int visitor, int unders) {
			if (visitor == _visitedBy) {
				if (unders <= _visitedUnders)
					return true;
				else
					return false;
			}
			_visitedUnders = -1;
			return false;
		}

		/**
		 * Visit a node
		 * 
		 * @param lastvisited
		 *            last visited by
		 * @param unders
		 *            number of underscores of last visitor
		 */
		public void visit(int lastvisited, int unders) {
			_visitedBy = lastvisited;
			_visitedUnders = unders;
		}

		/**
		 * add datum d to current node's ranking corresponding to
		 * underscoreCount. Start from 0 underscores to undersCount (inclusive)
		 * 
		 * @param d
		 *            data point
		 * @param undersCount
		 *            number of underscores
		 */
		void addtoRank(Datum d, int undersCount) {
			if (_visitedUnders == -1)
				addtoRank(d, undersCount, 0);
			else if (undersCount > _visitedUnders)
				if (_visitedUnders == 0) { // handle case where not visited with
											// underscore differently
					addtoRank(d, undersCount, _visitedUnders + 1);
				} else
					addtoRank(d, undersCount, undersCount - _visitedUnders);
		}

		/**
		 * add datum d to current node's ranking corresponding to
		 * underscoreCount
		 * 
		 * @param d
		 *            data point
		 * @param undersCount
		 *            number of underscores
		 * @param startInd
		 * 			  first index to add d to rank
		 */
		void addtoRank(Datum d, int undersCount, int startInd) {
			int i = startInd;
			while (i <= undersCount) {
				Datum[] ranking = null;
				// since working with an arraylist, need to add if out of bounds, otherwise get
				try {
					ranking = underscores.get(i);
					insertToArray(d, ranking);
					underscores.set(i, ranking);
				} catch (IndexOutOfBoundsException e) {
					ranking = new Datum[maxranksize];
					insertToArray(d, ranking);
					underscores.add(i, ranking);
				}
				i++;
			}
		}

		/**
		 * get the rank corresponding to number of underscores.
		 * 
		 * @param undersCount
		 *            number of underscores
		 * @return
		 */
		Datum[] getRank(int undersCount) {
			try {
				return underscores.get(undersCount);
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		}

		/**
		 * Insert an item into an array (from big to small), shifting elements
		 * to the right. Last element is shifted out
		 * 
		 * @param item
		 * @param array
		 */
		<T extends Comparable<T>> void insertToArray(T item, T[] array) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					array[i] = item;
					break;
				} else if (item.compareTo(array[i]) >= 1) {
					for (int j = array.length - 1; j > i; j--) {
						array[j] = array[j - 1];
					}
					array[i] = item;
					break;
				}
			}
		}
	}
}
