package plugins;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CFGManager {
	 static Properties prop =  new Properties();
	 public static String get(String Pname, InputStream stream) {
		 try {
			prop.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return prop.getProperty(Pname);
	 }
}
