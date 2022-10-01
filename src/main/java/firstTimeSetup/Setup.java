package firstTimeSetup;

import java.io.File;

import configuration.Manager;

public class Setup {
	public void begin() {
		System.out.println("WARNING: no configurations found... entering Firsttime-Setup");
		System.out.println("Creating directories...");
		new File("config").mkdirs();
		new File("plugins").mkdirs();
		System.out.println("...Done!");
		Ui.init();
	}
}
