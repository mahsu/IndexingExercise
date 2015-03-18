import java.io.Serializable;

/** an instance of Datum represents a name, score pair */
class Datum implements Comparable<Datum>, Serializable {

	private static final long serialVersionUID = -1319893657251638772L;
	String name;
	int score;

	public Datum(String n, int s) {
		name = n;
		score = s;
	}

	@Override
	public int compareTo(Datum d) {
		// lower score < higher score (also from some stackoverflow)
		return (score < d.score) ? -1 : (score == d.score) ? 0 : 1;
	}

	@Override
	public String toString() {
		return name + ":" + score;
	}
}
