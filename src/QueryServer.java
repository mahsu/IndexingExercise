import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

//run w/ arguments:
// <a0: location of serialized index>
public class QueryServer {
	public static void main(String[] args) throws IOException {
		Index i = null;
		try {
			System.out.println("Reading serialized index...");
			System.out.println("This will take a while.");
			FileInputStream fileIn = new FileInputStream(args[0]);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			i = (Index) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Serialized index not found.");
			c.printStackTrace();
			return;
		}
		System.out.println("Deserialized Index at " + args[0]);
		System.out.println("Index size is " + i.size);

		while (true) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter Query: ");
			String q = br.readLine();
			Datum[] results = i.search(q);
			if (results == null) {
				System.out.println("No matches found.");
			} else {
				for (int j = 0; j < results.length; j++) {
					if (results[j] != null)
						System.out.println(j + 1 + ". " + results[j].toString());
				}
			}
		}
	}
}
