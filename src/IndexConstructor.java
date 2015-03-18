import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

//run with args:
//<a0: location to save serialized file>
public class IndexConstructor {

	public static void main(String[] args) throws IOException {
		Index i = new Index();

		try {
			i = RandomIndexGenerator.makeRandomIndex();
			System.out.println("Saving serialized index in " + args[0] + "...");
			FileOutputStream fileOut = new FileOutputStream(args[0]);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(i);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data saved in " + args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
