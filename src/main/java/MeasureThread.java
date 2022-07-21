import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class MeasureThread implements Runnable {
	Boolean flipFlop = false;
	Boolean flipFlop2 = false;
	public static Boolean RpcEnabled = true;
	String output = "";
	int timer = 0;
	private static final DecimalFormat df = new DecimalFormat("0.0");
	public void run() {
		while (true) { 
				try {
					
					if (flipFlop) {
						flipFlop=false;
						if (Main.nvGPUEnabled){
							ProcessBuilder pb = new ProcessBuilder("/usr/bin/nvidia-smi", "--query-gpu=temperature.gpu", "--format=csv");
							output = IOUtils.toString(pb.start().getInputStream(), StandardCharsets.UTF_8);
						}
						if (flipFlop2 && RpcEnabled) {
							flipFlop2=false;
							if (timer < 20) {
								Main.dcRPC.presence.largeImageKey = "image01big";
								Main.dcRPC.presence.details = "CPU-Usage:";
								Main.dcRPC.presence.state = df.format(Main.oth.o.cpuUsage)+"%";
								Main.dcRPC.lib.Discord_UpdatePresence(Main.dcRPC.presence);
							}else if(timer < 40) {
								Main.dcRPC.presence.details = "CPU-Temperature:";
								Main.dcRPC.presence.state = Main.oth.o.cpuTemp+"°C";
								Main.dcRPC.lib.Discord_UpdatePresence(Main.dcRPC.presence);
							}else if(timer < 60) {
								Main.dcRPC.presence.details = "GPU-Temperature:";
								Main.dcRPC.presence.state = Main.oth.o.gpuTemp+"°C";
								Main.dcRPC.lib.Discord_UpdatePresence(Main.dcRPC.presence);
							}else if(timer < 80) {
								Main.dcRPC.presence.details = "VM-Memory:";
								Main.dcRPC.presence.state = df.format((Main.oth.o.MemUsed/Main.oth.o.MemTot)*100)+"%";
								Main.dcRPC.lib.Discord_UpdatePresence(Main.dcRPC.presence);
							}
						}else {
							flipFlop2=true;
						}
					}else {
						flipFlop=true;
					}
					
					if (Main.nvGPUEnabled){
						Integer.parseInt(output.replaceAll("temperature.gpu","").replaceAll("\\n", ""));
					}else{
						Main.oth.o.gpuTemp = 0;
					}
					Main.oth.o.cpuTemp = readTemp();
					Main.oth.o.cpuUsage = getCpuUsage();
					Main.oth.o.MemUsed = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
					Main.oth.o.MemTot = Runtime.getRuntime().totalMemory();
					Main.oth.o.cpuMeterHeight = (int) ((Main.oth.o.cpuUsage * 130)/100);
					Main.oth.o.memMeterHeight = (int) (((100*(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/Runtime.getRuntime().totalMemory()) * 130)/100);
					
					
					
					if (timer < 90) {
						timer++;
					}else {
						timer=0;
					}
					Thread.sleep(300);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public static String readLoad() {
		String cpuLoad = "";
		try {
			File myObj = new File("/proc/stat");
			Scanner myReader = new Scanner(myObj);
			cpuLoad = myReader.nextLine();
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return cpuLoad;
	}

	public static Float getCpuUsage() {
		String cpuLoad = "";
		String[] measurement1 = readLoad().split(" ");
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
		}
		String[] measurement2 = readLoad().split(" ");
		Float load = (Float.parseFloat(measurement2[2]) - Float.parseFloat(measurement1[2])
				+ Float.parseFloat(measurement2[4]) - Float.parseFloat(measurement1[4]))
				* 100
				/ (Float.parseFloat(measurement2[2]) - Float.parseFloat(measurement1[2])
						+ Float.parseFloat(measurement2[4]) - Float.parseFloat(measurement1[4])
						+ Float.parseFloat(measurement2[5]) - Float.parseFloat(measurement1[5]));
		return load;
	}
	static Boolean useOther =false;
	public static Integer readTemp() {
		String temp = "";
		try {
			File myObj;
			myObj = new File(Main.CpuStatFile);
			Scanner myReader = new Scanner(myObj);
			temp = myReader.nextLine();
			myReader.close();
		} catch (FileNotFoundException e) {
			useOther=true;
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return Integer.parseInt(temp) / 1000;
	}
}
