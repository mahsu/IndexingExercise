import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/** Random Index Generator creates a random index **/
public class RandomIndexGenerator {

	public static ArrayList<String> words;
	public static Random r = new Random();
	public static int basicStrings = 100000; // regular words
	public static int singleUnderscores = 50000; // abc_def_ghi
	public static int manyUnderscores = 10000; // abc_______def___ghi

	public static Index makeRandomIndex() throws IOException {
		Index i = new Index();
		words = fileToList();

		System.out.println("Adding basic strings");
		for (int j = 0; j < 100000; j++) {
			i.add(randWord(), randomScore());
		}
		System.out.println("Done");

		System.out.println("Adding single underscores");
		for (int j = 0; j < 50000; j++) {
			StringBuffer s = new StringBuffer();
			for (int k = 0; k < r.nextInt(3) + 1; k++) {
				s.append(randWord() + "_");
			}
			i.add(s.toString(), randomScore());
			if (r.nextInt(basicStrings / 5) == 0) {
				System.out.println(j + "/" + basicStrings);
			}
		}
		System.out.println("Done");

		System.out.println("Adding many underscore strings");
		for (int j = 0; j < 10000; j++) {
			StringBuffer s = new StringBuffer();
			for (int k = 0; k < r.nextInt(3) + 1; k++) {
				s.append(randWord() + repeat("_", r.nextInt(3) + 1));
			}
			i.add(s.toString(), randomScore());
			if (r.nextInt(manyUnderscores / 5) == 0) {
				System.out.println(j + "/" + manyUnderscores);
			}
		}
		System.out.println("Done");
		return i;
	}

	/**
	 * generate a random score
	 * 
	 * @return
	 */
	public static int randomScore() {
		int max = Integer.MAX_VALUE;
		int min = Integer.MIN_VALUE;
		return r.nextInt(max - min) + min;
	}

	/**
	 * generate a random word from a wordlist. Assume words is initialized
	 * 
	 * @return
	 */
	public static String randWord() {
		return words.get(r.nextInt(words.size()));
	}

	/**
	 * repeat a string s n times
	 * 
	 * @param s
	 * @param times
	 * @return
	 */
	public static String repeat(String s, int times) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < times; i++) {
			b.append(s);
		}
		return b.toString();
	}

	// from:
	// http://stackoverflow.com/questions/6599678/read-from-file-and-display-random-line
	// assume word list is small enough to be loaded into memory
	/**
	 * convert a file to an arraylist
	 * 
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> fileToList() throws IOException {
		// Read in the file into a list of strings
		BufferedReader reader = null;
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		try {
			reader = new BufferedReader(new FileReader("5000-words.txt"));
			line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		if (reader != null) {
			reader.close();
		}
		return lines;
	}
}
