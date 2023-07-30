package general;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import Overlay.OverlayThread;
import dbus.mpris.MPrisCTL;
import plugins.Manager;

import remoteProcessingClient.Client;

public class Main {
	private static Main main = new Main();
	public Boolean restarted = false;
	private Boolean RST = false;
	public OverlayThread oth;
	public Thread othth;
	public Thread dcTh;
	public MPrisCTL mpc;
	public Boolean QtInitialized = false;
	private Boolean init = false;
	public com.discord.DiscordRPCStatus dcRPC;
	VoiceAssistant va;
	public configuration.Manager man = new configuration.Manager();
	public String vocab = "programs shutdown reconfigure stop the hey javi pause resume next previous set volume to percent open hide show overlay what is the cpu usage start conversation ten twenty thirty forty fifty sixty seventy eighty ninety ";
	public int sensibility = 20;

	public static void main(String[] args) {
		Properties props = System.getProperties();
		try {
			if (new File("config").exists() && new File("plugins").exists()) {
				main.vocab = main.vocab + main.man.get("progNames").replaceAll(";", " ");
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("--server")) {
						props.setProperty("javax.net.ssl.keyStore", "keys/server-key.jks");
						props.setProperty("javax.net.ssl.keyStorePassword", System.getenv("KEY_PASSPHRASE"));
						Thread t = new Thread() {
							public void run() {
								new remoteProcessingServer.ServerTTS().start();
							}
						};
						t.start();
						new remoteProcessingServer.ServerSTT().start();
						System.exit(0);
					}
				}
				boolean mimic = false;
				String host = "";
				if (args.length == 2){
					if (args[0].equals("--mimic3")){
						mimic = true;
						host = args[1];
					}
				}else if(args.length == 4){
					if (args[2].equals("--mimic3")){
						mimic = true;
						host = args[3];
					}
				}

				Logger.getRootLogger().setLevel(Level.OFF);
				System.out.println("initializing modules...");
				System.out.println("Overlay...");
				Thread.sleep(2000);
				if (main.man.get("overlay-module-activated").equalsIgnoreCase("yes")) {
					main.oth = new OverlayThread();
					main.othth = new Thread(main.oth);
					main.othth.start();
					main.QtInitialized = true;
					System.out.println("Done!");
				} else {
					System.out.println("--disabled!--");
				}
				System.out.println("Discord-RPC...");
				if (main.man.get("discord-rpc-module-activated").equalsIgnoreCase("yes")) {
					main.dcRPC = new com.discord.DiscordRPCStatus();
					main.dcTh = new Thread(main.dcRPC);
					main.dcTh.start();
					System.out.println("Done!");
				} else {
					System.out.println("--disabled!--");
				}
				System.out.println("MPrisCTL...");
				if (main.man.get("mpris-module-activated").equalsIgnoreCase("yes")) {
					main.mpc = new MPrisCTL();
					Thread mpt = new Thread() {
						public void run() {
							main.mpc.init();
						}
					};
					mpt.start();
					System.out.println("Done!");
				}else{
					System.out.println("--disabled!--");
				}
				System.out.println();
				System.out.println("-------------------------------------------------------------");
				System.out.println("                  STARTING TO LOAD PLUGINS                   ");
				System.out.println("-------------------------------------------------------------");
				System.out.println();
				Manager mana = new Manager();
				mana.loadPlugins();

				System.out.println();
				System.out.println("-------------------------------------------------------------");
				System.out.println("                  FINISHED LOADING PULGINS                   ");
				System.out.println("-------------------------------------------------------------");
				System.out.println();

				main.init = true;
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("--client") && args.length == 2) {
						props.setProperty("javax.net.ssl.trustStore", "keys/client-trust.jks");
						props.setProperty("javax.net.ssl.trustStorePassword", System.getenv("KEY_PASSPHRASE"));
						System.out.println("Test passed");
						Client conncli = new Client();
						main.va = new VoiceAssistant(true, conncli, mimic, host);
						Thread clientTH = new Thread() {
							public void run() {
								conncli.connect(args[1], main.vocab, main.va);
							}
						};
						clientTH.start();
					}else{
						main.va = new VoiceAssistant(false, null, mimic, host);
					}
				} else {
					main.va = new VoiceAssistant(false, null, mimic, host);
				}
				Thread tva = new Thread(main.va);
				main.va.sensibility = Integer.parseInt(main.man.get("rms-threshold"));
				tva.start();
				while (!main.RST) {
					Thread.sleep(10000);
				}
				main.RST = false;
				main(null);
			} else {
				new firstTimeSetup.Setup().begin();
			}
		} catch (Exception w) {

		}
	}

	public static Main getMain() {
		return main;
	}

	public void shutdown() {
		va.isRunning = false;
	}

	public void restart() {
		main.va.restart();
	}
}
