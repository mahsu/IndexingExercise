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
		int firstLetter = d.name.charAt(0);
		int ind = firstLetter-start;
		if (tries[ind] == null) {
			tries[ind] = new Node((char)firstLetter); 
		}
		tries[ind].addtoRank(d);
			addhelper(d.name.substring(1), d, tries[ind]);
	}
	
	private void addhelper(String s, Datum d, Node n){
		if (s.equals("")) return;
		int firstLetter = s.charAt(0);
		int ind = firstLetter-start;
		if (n.children[ind] == null){
			n.children[ind] = new Node((char)firstLetter);
		}
		n.children[ind].addtoRank(d);
		addhelper(s.substring(1),d, n.children[ind]);
		
	}
	//s is not null
	public String search(String s){
		if (s == null || s.equals("")) return "[]";
		int firstLetter = s.charAt(0);
		Node root = tries[firstLetter-start];
		if (root == null) return "[]";
		Datum[] result = searchHelper(s.substring(1),root).ranking;
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
	
	
	class Node {
		final static int maxranksize = 10;
		char letter;
		Node[] children = new Node[size];
		Datum[] ranking = new Datum[maxranksize]; //max to min ranking
		
		public Node(char l) {
			letter = l;
		}
		
		public Node(char l, Datum d){
			letter = l;
			addtoRank(d);
		}
		
		void addtoRank(Datum d) {
			insertToArray(d, ranking);
		}
		
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
