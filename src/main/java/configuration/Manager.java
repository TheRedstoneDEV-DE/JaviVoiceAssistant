package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Manager {
	Properties prop = new Properties();
	Boolean loaded = false;

	public void set(String Pname, String content) {
		try {
			load();
			OutputStream out = new FileOutputStream("config/VoiceAssistant.properties");
			prop.setProperty(Pname, content);
			prop.store(out, "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String get(String Pname) {
		load();
		return prop.getProperty(Pname);
	}

	InputStream getConfig() throws FileNotFoundException {
		return new FileInputStream(new File("config/VoiceAssistant.properties"));
	}
	void load() {
		if (!loaded && new File("config/VoiceAssistant.properties").exists()) {
			try {
				InputStream in = new Manager().getConfig();
				prop.load(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
