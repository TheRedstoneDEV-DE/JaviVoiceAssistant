package general;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import Overlay.OverlayThread;
import RemoteVoskProtocolClient.ClientMain;
import dbus.mpris.MPrisCTL;
import plugins.Manager;

public class Main {
	private static Main main = new Main();
	public Boolean restarted = false;
	private Boolean RST = false;
	public OverlayThread oth;
	public Thread othth;
	public Thread dcTh;
	public MPrisCTL mpc;
	public Boolean QtInitialized = false;
	public com.discord.DiscordRPCStatus dcRPC;
	public ClientMain clim = new ClientMain();
	VoiceAssistant va;
	public String vocab = "reconfigure stop kate dolphin the hey computer pause resume next previous set volume to percent open steam hide show overlay what is the cpu usage start conversation ten twenty thirty forty fifty sixty seventy eighty ninety";
	public int sensibility = 20;
	configuration.Manager man = new configuration.Manager();

	public static void main(String[] args) {
		try {
			if (new File("config").exists() && new File("plugins").exists()) {
				Logger.getRootLogger().setLevel(Level.OFF);
				System.out.println("initializing modules...");
				System.out.println("Overlay...");
				Thread.sleep(2000);
				if (main.man.get("overlay-module-activated").equalsIgnoreCase("yes")) {
					main.oth = new OverlayThread();
					main.othth = new Thread(main.oth);
					main.othth.start();
					main.QtInitialized=true;
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
				if (main.man.get("mpris-module-activated").equalsIgnoreCase("yes"))
					main.mpc = new MPrisCTL();
				Thread mpt = new Thread() {
					public void run() {
						main.mpc.init();
					}
				};
				mpt.start();
				System.out.println("Done!");

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
				
				
				if (!main.man.get("use-local-recognition").equalsIgnoreCase("yes")) {
					Thread VClithr = new Thread() {
						public void run() {
							main.clim.start();
						}
					};
					VClithr.start();
				} else {
					main.va = new VoiceAssistant();
					Thread tva = new Thread(main.va);
					main.va.sensibility = Integer.parseInt(main.man.get("rms-threshold"));
					tva.start();
				}
				while (!main.RST) {
					Thread.sleep(10000);
				}
				main.RST=false;
				main(null);
			}else {
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
		RST=true;
		restarted=true;
		va.isRunning = false;
	}
}
