package Overlay;

import io.qt.widgets.*;

public class OverlayThread implements Runnable {
	public static Overlay o;
	public static QApplication app;
	public static String artist = "null";
	public static String title = "null";

	public void run() {
		String SFargs = "Main,text";
		String[] FArgs = SFargs.split(",");
		try {
			app.initialize(FArgs);
			o = new Overlay();
			app.exec();
		} catch (Exception e) {
			System.out.println("Thread for Overlay had failed!");
			System.out.println("Exception:");
			e.printStackTrace();
		}
	}
}
