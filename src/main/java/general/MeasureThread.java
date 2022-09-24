package general;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

import configuration.Manager;

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
	Boolean nvGPU = false;
	Manager man = new Manager();
	private static final DecimalFormat df = new DecimalFormat("0.0");
	public void run() {
		nvGPU=man.get("system-status-module-nvgpu-activated").equalsIgnoreCase("yes");
		while (true) { 
				try {
					
					if (flipFlop) {
						flipFlop=false;
						if (nvGPU){
							ProcessBuilder pb = new ProcessBuilder("/usr/bin/nvidia-smi", "--query-gpu=temperature.gpu", "--format=csv");
							output = IOUtils.toString(pb.start().getInputStream(), StandardCharsets.UTF_8);
						}
						if (flipFlop2 && RpcEnabled) {
							flipFlop2=false;
							if (timer < 20) {
								Main.getMain().dcRPC.presence.largeImageKey = "image01big";
								Main.getMain().dcRPC.presence.details = "CPU-Usage:";
								Main.getMain().dcRPC.presence.state = df.format(Main.getMain().oth.o.cpuUsage)+"%";
								Main.getMain().dcRPC.lib.Discord_UpdatePresence(Main.getMain().dcRPC.presence);
							}else if(timer < 40) {
								Main.getMain().dcRPC.presence.details = "CPU-Temperature:";
								Main.getMain().dcRPC.presence.state = Main.getMain().oth.o.cpuTemp+"°C";
								Main.getMain().dcRPC.lib.Discord_UpdatePresence(Main.getMain().dcRPC.presence);
							}else if(timer < 60) {
								Main.getMain().dcRPC.presence.details = "GPU-Temperature:";
								Main.getMain().dcRPC.presence.state = Main.getMain().oth.o.gpuTemp+"°C";
								Main.getMain().dcRPC.lib.Discord_UpdatePresence(Main.getMain().dcRPC.presence);
							}else if(timer < 80) {
								Main.getMain().dcRPC.presence.details = "VM-Memory:";
								Main.getMain().dcRPC.presence.state = df.format((Main.getMain().oth.o.MemUsed/Main.getMain().oth.o.MemTot)*100)+"%";
								Main.getMain().dcRPC.lib.Discord_UpdatePresence(Main.getMain().dcRPC.presence);
							}
						}else {
							flipFlop2=true;
						}
					}else {
						flipFlop=true;
					}
					
					if (nvGPU){
						Integer.parseInt(output.replaceAll("temperature.gpu","").replaceAll("\\n", ""));
					}else{
						Main.getMain().oth.o.gpuTemp = 0;
					}
					Main.getMain().oth.o.cpuTemp = readTemp();
					Main.getMain().oth.o.cpuUsage = getCpuUsage();
					Main.getMain().oth.o.MemUsed = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
					Main.getMain().oth.o.MemTot = Runtime.getRuntime().totalMemory();
					Main.getMain().oth.o.cpuMeterHeight = (int) ((Main.getMain().oth.o.cpuUsage * 130)/100);
					Main.getMain().oth.o.memMeterHeight = (int) (((100*(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/Runtime.getRuntime().totalMemory()) * 130)/100);
					
					
					
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

	public String readLoad() {
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

	public Float getCpuUsage() {
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
	Boolean useOther =false;
	public Integer readTemp() {
		String temp = "";
		try {
			File myObj;
			myObj = new File(man.get("system-status-module-cpu-temperature-file"));
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
