import java.util.ArrayList;
import java.util.List;


public class Index {
	//valid java variables start with letters, numbers, '$' or '_'
	final static int start = '$';
	final static int end = 'z';
	final static int size = end-start+1;
	Node[] tries = new Node[size];
	
	//TODO: merge processing logic with addhelper
	void add(String n, int s){
		Datum d = new Datum(n,s);
		
		/* begin: same as logic in addhelper but did not merge due to different variables*/
		int undersCount = countPrefix('_', n);
		String stripped = stripPrefix('_',n);
		int firstLetter = stripped.charAt(0);
		int ind = firstLetter-start;
		if (tries[ind] == null) {
			tries[ind] = new Node((char)firstLetter); 
		}
		tries[ind].addtoRank(d,undersCount);
		addhelper(stripped.substring(1), d, tries[ind], undersCount);
		/*end: same*/
	}
	
	/**
	 * counts the occurrences of a prefix p in string s
	 * @param p
	 * @param s
	 * @return
	 */
	private int countPrefix(char p, String s){
		int count = 0;
		for (char d : s.toCharArray() ){
			if (d == p) count++;
			else break;
		}
		return count;
	}
	
	/* modified from http://stackoverflow.com/questions/6179192/how-to-get-all-the-occurrence-character-on-the-prefix-of-string-in-java-in-a-sim*/
	private String stripPrefix(char p, String s) {
		int length = 0;
		while (length < s.length() && s.charAt(length) == p) {
		    length++;
		}
		return s.substring(length);
	}
		
	
	private void addhelper(String s, Datum d, Node n, int undersCount){
		if (s.equals("")) return;
		int firstLetter = s.charAt(0);
		int ind = firstLetter-start;
		if (n.children[ind] == null){
			n.children[ind] = new Node((char)firstLetter);
		}
		n.children[ind].addtoRank(d, undersCount);
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
		final static int maxranksize = 10;
		char letter;
		Node[] children = new Node[size];
		ArrayList<Datum[]> underscores = new ArrayList<Datum[]>();
		//Datum[] ranking = new Datum[maxranksize]; //max to min ranking
		
		public Node(char l) {
			letter = l;
		}
		
		public Node(char l, Datum d, int undersCount){
			letter = l;
			addtoRank(d, undersCount);
		}
		
		void addtoRank(Datum d, int undersCount) {
			int i=0;	
			//switch to plain while
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
			catch (ArrayIndexOutOfBoundsException e){
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
	

	
	class Datum implements Comparable<Datum>{
		String name;
		int score;
		
		public Datum(String n, int s) {
			name = n;
			score = s;
		}
		
		@Override
		public int compareTo(Datum d) {
			//lower score < higher score
			return (score < d.score) ? -1 : (score == d.score) ? 0 : 1;
		}
		
		@Override
		public String toString() {
			return name+ ":" +score;
		}
	}
}
