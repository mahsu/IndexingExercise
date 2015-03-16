import java.util.ArrayList;

public class Index{
	//valid java variables start with letters, numbers, '$' or '_'
	final static int start = '$'; //first possible char
	final static int end = 'z'; //last possible char
	final static int char_space = end-start+1; //size of array
	Node[] tries = new Node[char_space]; //each element corresponds to a first letter
	int size = 0;

	void add(String n, int s){
		Datum d = new Datum(n,s);
		
		String[] strings = generateStrings('_',n);
		size++; //started inserting a new string
		for (int i=0; i<strings.length; i++){
			int undersCount = countPrefix('_', strings[i]);
			String stripped = stripPrefix('_',strings[i]);
			int firstLetter = stripped.charAt(0);
			int ind = firstLetter-start;
			if (tries[ind] == null) {
				tries[ind] = new Node((char)firstLetter); 
			}
			if (tries[ind].visitedBy != size) {
				tries[ind].addtoRank(d,undersCount);
				tries[ind].visitedBy = size;
			}
			addhelper(stripped.substring(1), d, tries[ind], undersCount);
		}
	}
	
	/**
	 * counts the occurrences of a prefix p in string s
	 * @param p
	 * @param s
	 * @return
	 */
	/* modified from http://stackoverflow.com/questions/6179192/how-to-get-all-the-occurrence-character-on-the-prefix-of-string-in-java-in-a-sim */
	public static int countPrefix(char p, String s){
		int count = 0;
		for (char d : s.toCharArray() ){
			if (d == p) count++;
			else break;
		}
		return count;
	}
	
	/* modified from http://stackoverflow.com/questions/6179192/how-to-get-all-the-occurrence-character-on-the-prefix-of-string-in-java-in-a-sim */
	/**
	 * strips the prefix containing all letters p of a string s
	 * @param p
	 * @param s
	 * @return
	 */
	public static String stripPrefix(char p, String s) {
		int length = 0;
		while (length < s.length() && s.charAt(length) == p) {
		    length++;
		}
		return s.substring(length);
	}
	
	/**
	 * Pre-generate a list of strings based on delimeter
	 * @return
	 */
	public static String[] generateStrings(char delim, String s) {
		//todo: maybe optimize for strings that dont have delim
		ArrayList<String> list = new ArrayList<String>();
		int nextUnders = 0;
		do {
			list.add(s);
			s = stripPrefix('_', s);
			nextUnders = s.indexOf("_");
			s = s.substring(nextUnders+1); //NOTE: should have +1 if search is first '_' exclusive
		}
		while (nextUnders !=-1);
		
		String[] strings = new String[list.size()];
		return list.toArray(strings);
	}
	
	private void addhelper(String s, Datum d, Node n, int undersCount){
		if (s.equals("")) return;
		int firstLetter = s.charAt(0);
		int ind = firstLetter-start;
		if (n.children[ind] == null){
			n.children[ind] = new Node((char)firstLetter);
		}
		if (n.children[ind].visitedBy != size) {
			n.children[ind].addtoRank(d, undersCount);
			n.children[ind].visitedBy = size;
		}
		addhelper(s.substring(1),d, n.children[ind], undersCount);
		
	}
	//s is not null
	public String search(String s){
		if (s == null || s.equals("")) return "[]";
		String stripped = stripPrefix('_',s);
		int firstLetter = stripped.charAt(0);
		Node root = tries[firstLetter-start];
		if (root == null) return "[]";
		
		int undersCount = countPrefix('_', s);
		
		Node n = searchHelper(stripped.substring(1),root);
		Datum[] result = n.getRank(undersCount);
		if (result == null) return "[]";
		
		String toreturn = result[0] + "";
		for(int i=1; i<result.length; i++){
			if (result[i] != null)
				toreturn += "," +result[i].toString();
		}
		return "[" + toreturn + "]";
	}
	
	
	
	private Node searchHelper(String s, Node n){
		if (n == null) return null;
		if (s.equals("")) return n;
		int firstLetter = s.charAt(0);
		return searchHelper(s.substring(1),n.children[firstLetter-start]);
	}
	
	/** An instance of Node represents a node in the trie corresponding to the first letter**/
	class Node {
		int visitedBy = -1;
		final static int maxranksize = 10;
		char letter;
		Node[] children = new Node[char_space];
		ArrayList<Datum[]> underscores = new ArrayList<Datum[]>(); //max # of underscores not known ahead of time
		
		public Node(char l) {
			letter = l;
		}
		
		public Node(char l, Datum d, int undersCount){
			letter = l;
			addtoRank(d, undersCount);
		}
		
		void addtoRank(Datum d, int undersCount) {
			int i=0;	
			//todo: switch to plain while
			do{
					Datum[] ranking = null;
					try {
						ranking = underscores.get(i);
						insertToArray(d, ranking);
						underscores.set(i, ranking);
					}
					catch(IndexOutOfBoundsException e){
						ranking = new Datum[maxranksize];
						insertToArray(d, ranking);
						underscores.add(i,ranking);
					}	
						i++;
				}
				while (i <= undersCount);
			}
		
		Datum[] getRank(int undersCount) {
			try {
				return underscores.get(undersCount);
			}
			catch (IndexOutOfBoundsException e){
				return null;
			}

		}
		
		/**
		 * Insert an item into an array (from big to small), shifting elements to the right
		 * Last element is shifted out
		 * @param item
		 * @param array
		 */
		<T extends Comparable<T>> void insertToArray(T item, T[] array) {
			for (int i=0; i< array.length; i++) {
				if (array[i] == null) {
					array[i] = item;
					break;
				}
				else if (item.compareTo(array[i]) >= 1) {
					for (int j=array.length-1; j>i; j--) {
						array[j] = array[j-1];
					}
					array[i]= item;
					break;
				}
			}
		}
	}
	
	/** an instance of Datum represents a name, score pair*/
	class Datum implements Comparable<Datum>{
		String name;
		int score;
		
		public Datum(String n, int s) {
			name = n;
			score = s;
		}
		
		@Override
		public int compareTo(Datum d) {
			//lower score < higher score (also from some stackoverflow)
			return (score < d.score) ? -1 : (score == d.score) ? 0 : 1;
		}
		
		@Override
		public String toString() {
			return name+ ":" +score;
		}
	}
}
