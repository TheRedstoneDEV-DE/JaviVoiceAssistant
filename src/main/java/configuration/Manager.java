package configuration;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

public class Manager {
	 Properties prop =  new Properties();
	 public String get(String Pname) {
		 load();
		 return prop.getProperty(Pname);
	 }
	 InputStream getInternalConfig() {
		 return getClass().getResourceAsStream("VoiceAssistant.properties");
	 }
	 void load() {
		 try {
		 InputStream in = new Manager().getInternalConfig();
		 prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
}
