package Overlay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

import configuration.Manager;
import dbus.mpris.MPrisCTL;
import firstTimeSetup.Ui;
import io.qt.core.QBasicTimer;
import io.qt.core.QRect;
import io.qt.core.QTimerEvent;
import io.qt.core.Qt;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.widgets.QWidget;
import types.Metadata;

public class Overlay extends QWidget {
	public boolean hidden = false;
	public boolean AIactive = false;
	private boolean hstate = false;
	private Boolean NVgpu = false;
	private Boolean flip = false;
	public String artist = "null";
	public String title = "null";
	public Boolean reconf = false;
	public Boolean progconf = false;
	private int gpuTemp = 0;
	dbus.mpris.MPrisCTL mp = new MPrisCTL();
	private String lastWord = "";
	Manager man = new Manager();
	private final DecimalFormat df = new DecimalFormat("0.0");
	private int step;
	private QBasicTimer timer;

	public Overlay() {
		setWindowFlags(Qt.WindowType.FramelessWindowHint, Qt.WindowType.WindowTransparentForInput,
				Qt.WindowType.WindowStaysOnTopHint, Qt.WindowType.NoDropShadowWindowHint,
				Qt.WindowType.WindowMaximizeButtonHint,
				Qt.WindowType.X11BypassWindowManagerHint);
		setAttribute(Qt.WidgetAttribute.WA_NoSystemBackground);
		setAttribute(Qt.WidgetAttribute.WA_TranslucentBackground);
		String[] xy = man.get("overlay-position").split(",");
		if (man.get("mpris-module-activated").equalsIgnoreCase("yes")) {
			setGeometry(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]), 700, 300);
			setFixedHeight(300);
			setFixedWidth(700);
		} else {
			setGeometry(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]), 620, 300);
			setFixedHeight(300);
			setFixedWidth(620);
		}
		NVgpu = man.get("system-status-module-nvgpu-activated").equalsIgnoreCase("yes");
		showFullScreen();
		step = 0;
		timer = new QBasicTimer();
		timer.start(1000, this);
		if (man.get("mpris-module-activated").equalsIgnoreCase("yes"))
			mp.init();
	}

	@Override
	protected void paintEvent(QPaintEvent event) {
		int spacing = 16;
		int cpuUsage = getCpuUsage();
		int cpuTemp = readTemp();

		if (flip) {
			if (NVgpu) {
				try {
					ProcessBuilder pb = new ProcessBuilder("/usr/bin/nvidia-smi", "--query-gpu=temperature.gpu",
							"--format=csv");
					gpuTemp = Integer.parseInt((IOUtils.toString(pb.start().getInputStream(), StandardCharsets.UTF_8))
							.replaceAll("temperature.gpu", "").replaceAll("\\n", ""));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				;
			}
			flip = !flip;
		} else {
			flip = !flip;
		}

		QPainter pn = new QPainter(this);

		// define headline Font and size
		QFont fontHeadline = new QFont();
		fontHeadline.setPixelSize(23);
		fontHeadline.setFamily("DotMatrix");

		// define normal text font
		QFont fontNormal = new QFont();
		fontNormal.setPixelSize(18);
		fontNormal.setFamily("DotMatrix");

		pn.setFont(fontHeadline);
		pn.setPen(new QColor(Qt.GlobalColor.green));
		pn.drawText(20, 24, "AI Overlay");
		pn.setFont(fontNormal);
		pn.drawText(10, 24 + spacing, "AI Status: " + AIactive);
		pn.drawText(10, 24 + spacing * 2,
				"AI Memory Use: "
						+ df.format(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() / 1000) + "/"
						+ df.format(Runtime.getRuntime().totalMemory() / 1000));
		pn.drawText(10, 24 + spacing * 3, "Last Keyword:");
		pn.drawText(10, 24 + spacing * 4, lastWord);
		pn.drawText(10, 24 + spacing * 5, "CPU Temp.: " + cpuTemp + "°C");
		pn.drawText(10, 24 + spacing * 6, "GPU Temp.: " + gpuTemp + "°C");
		pn.drawRect(350, 10, 10, 130);
		pn.fillRect(new QRect(350, 140, 10, ((cpuUsage * 130) / 100) * -1), new QColor(Qt.GlobalColor.green));
		pn.drawText(345, 156, "CPU");
		pn.drawRect(383, 10, 10, 130);
		pn.fillRect(
				new QRect(383, 140, 10,
						(int) ((((100 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
								/ Runtime.getRuntime().totalMemory()) * 130) / 100) * -1)),
				new QColor(Qt.GlobalColor.green));
		pn.drawText(377, 156, "MEM");
		Metadata metadat;
		if (man.get("mpris-module-activated").equalsIgnoreCase("yes")) {
			if ((metadat = mp.getMetadata()) != null) {
				pn.drawText(420, 24 + spacing * 2, "Playing:");
				pn.drawText(420, 24 + spacing * 3, metadat.getTitle());
				if (metadat.getAlbum() != null) {
					pn.drawText(420, 24 + spacing * 4, "of " + metadat.getAlbum());
				}
				pn.drawText(420, 24 + spacing * 5, "by " + metadat.getArtist());
			}
		}

		if (cpuUsage > 90) {
			pn.setFont(fontHeadline);
			pn.setPen(new QColor(Qt.GlobalColor.yellow));
			pn.drawText(10, 24 + spacing * 10, "WATCHDOG REPORT: CPU maxed out at more than 90%!");
		}
		if (cpuTemp > 80) {
			pn.setFont(fontHeadline);
			pn.setPen(new QColor(Qt.GlobalColor.red));
			pn.drawText(10, 24 + spacing * 12, "WATCHDOG REPORT: CPU over 80°C !WARNING OVERHEATING!");
		}
	}

	@Override
	protected void timerEvent(QTimerEvent e) {
		if (e.timerId() == timer.timerId()) {
			++step;
			if (!hidden) {
				if (hstate) {
					show();
					hstate = false;
				}
			} else {
				if (!hstate) {
					hide();
					hstate = true;
				}
			}
			if (reconf) {
				reconf = false;
				Ui mainw = new Ui();
				mainw.show();
				mainw.setWindowTitle("Setup");
			}
			if (progconf) {
				progconf = false;
				progsetup.Ui mainw = new progsetup.Ui();
				mainw.show();
				mainw.setWindowTitle("Setup");
			}
			update();
		} else {
			super.timerEvent(e);
		}
	}

	public void setWord(String word) {

	}

	private String readLoadIndex() {
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

	private int getCpuUsage() {
		String[] measurement1 = readLoadIndex().split(" ");
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
		}
		String[] measurement2 = readLoadIndex().split(" ");
		Float load = (Float.parseFloat(measurement2[2]) - Float.parseFloat(measurement1[2])
				+ Float.parseFloat(measurement2[4]) - Float.parseFloat(measurement1[4]))
				* 100
				/ (Float.parseFloat(measurement2[2]) - Float.parseFloat(measurement1[2])
						+ Float.parseFloat(measurement2[4]) - Float.parseFloat(measurement1[4])
						+ Float.parseFloat(measurement2[5]) - Float.parseFloat(measurement1[5]));
		return load.intValue();
	}

	Boolean useOther = false;

	private Integer readTemp() {
		String temp = "";
		try {
			File myObj;
			myObj = new File(man.get("system-status-module-cpu-temperature-file"));
			Scanner myReader = new Scanner(myObj);
			temp = myReader.nextLine();
			myReader.close();
		} catch (FileNotFoundException e) {
			useOther = true;
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return Integer.parseInt(temp) / 1000;
	}
}
