
public class WebServerThread implements Runnable {
	public static String state = "";

	public void run() {
		if (Main.MprisEnabled) {
			MPrisCTL.loadNative();
		}
		WebServer.start();
	}
}
