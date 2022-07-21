import Overlay.OverlayThread;
import RemoteVoskProtocolClient.ClientMain;

public class Main {
	public static OverlayThread oth;
	public static Thread othth;
	public static WebServerThread wth;
	public static Thread dcTh;
	public static com.discord.DiscordRPCStatus dcRPC;
	public static JNIDesktopBinding jdbind;
	public static ClientMain clim = new ClientMain();
	public static String MPrisLibPath = "";
	public static Boolean MprisEnabled = false;
	public static Boolean RGBEnabled = false;
	public static String RGBLibFile = "";
	public static String RGBSerPort = "";
	public static Boolean SysstatEnabled = false;
	public static String CpuStatFile = "";
	public static Boolean OverlayEnabled = false;
	public static Boolean RPCEnabled = false;
	public static Boolean localRec = false;
	public static Boolean nvGPUEnabled = false;
	public static int sensibility = 0;

	public static void main(String[] args) {
		configure();
		try {
			System.out.println("initializing modules...");
			System.out.println("Overlay...");
			Thread.sleep(2000);
			if (OverlayEnabled) {
				oth = new OverlayThread();
				othth = new Thread(oth);
				othth.start();
				System.out.println("Done!");
			}else {
				System.out.println("--disabled!--");
			}
			System.out.println("System-status...");
			if (SysstatEnabled) {
				MeasureThread mt = new MeasureThread();
				Thread thrm = new Thread(mt);
				thrm.start();
				System.out.println("Done!");
			}else {
				System.out.println("--disabled!--");
			}
			wth = new WebServerThread();
			Thread thw = new Thread(wth);
			thw.start();
			System.out.println("Discord-RPC...");
			if (RPCEnabled) {
				dcRPC = new com.discord.DiscordRPCStatus();
				dcTh = new Thread(dcRPC);
				dcTh.start();
				System.out.println("Done!");
			}else {
				System.out.println("--disabled!--");
			}
			System.out.println("RGB...");
			if (RGBEnabled) {
				jdbind = new JNIDesktopBinding();
				Thread jdbindthr = new Thread(jdbind);
				jdbindthr.start();
				System.out.println("Done!");
			}else {
				System.out.println("--disabled!--");
			}
			if (!localRec) {
				Thread VClithr = new Thread() {
					public void run() {
						clim.start();
					}
				};
				VClithr.start();
			}else {
				VoiceAssistant va = new VoiceAssistant();
				Thread tva = new Thread(va);
				tva.start();
			}
			VoiceAssistant.initTTS();
			while (true) {
				Thread.sleep(60000);
			}
		} catch (Exception w) {

		}
		/*
		 * local voice recognition removed and replaced with Vosk on external mainframe
		 */
		// VoiceAssistant.start();
	}

	private static void configure() {
		if (configuration.Manager.get("mpris-module-activated").equalsIgnoreCase("yes")) {
			MprisEnabled = true;
		}
		MPrisLibPath = configuration.Manager.get("mpris-module-lib-path");
		if (configuration.Manager.get("rgb-control-module-activated").equalsIgnoreCase("yes")) {
			RGBEnabled = true;
		}
		RGBLibFile = configuration.Manager.get("rgb-control-module-lib-path");
		RGBSerPort = configuration.Manager.get("rgb-control-module-serialport");
		if (configuration.Manager.get("system-status-module-activated").equalsIgnoreCase("yes")) {
			SysstatEnabled = true;
		}
		CpuStatFile = configuration.Manager.get("system-status-module-cpu-temperature-file");
		if (configuration.Manager.get("overlay-module-activated").equalsIgnoreCase("yes")) {
			OverlayEnabled = true;
		}
		if (configuration.Manager.get("discord-rpc-module-activated").equalsIgnoreCase("yes")) {
			RPCEnabled = true;
		}
		if (configuration.Manager.get("use-local-recognition").equalsIgnoreCase("yes")) {
			localRec = true;
		}
		if (configuration.Manager.get("system-status-module-nvgpu-activated").equalsIgnoreCase("yes")) {
			nvGPUEnabled = true;
		}
		sensibility = Integer.parseInt(configuration.Manager.get("rms-threshold"));

	}
}
