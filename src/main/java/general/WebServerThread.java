package general;

public class WebServerThread implements Runnable {
	public static String state = "";

	public void run() {
		WebServer.start();
	}
}
