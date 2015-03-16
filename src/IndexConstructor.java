import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;


public class IndexConstructor {

	public static void main(String[] args) throws IOException { 
		Index i = new Index();
		try
	      {
	         FileOutputStream fileOut =
	         new FileOutputStream(args[0]);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(i);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data saved in "+ args[0]);
	      }catch(IOException e)
	      {
	          e.printStackTrace();
	      }
    }

}
