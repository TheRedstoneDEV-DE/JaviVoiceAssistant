package Overlay;

import io.qt.widgets.*;

public class OverlayThread implements Runnable {
	public Overlay o;
	public QApplication app;

	public void run() {
		String SFargs = "Main,text";
		String[] FArgs = SFargs.split(",");
		try {
			if (!general.Main.getMain().restarted) {
				app.initialize(FArgs);
				o = new Overlay();
				app.exec();
			}else { 
				o = new Overlay();
				app.exec();
			}
		} catch (Exception e) {
			System.out.println("Thread for Overlay had failed!");
			System.out.println("Exception:");
			e.printStackTrace();
		}
	}
}
