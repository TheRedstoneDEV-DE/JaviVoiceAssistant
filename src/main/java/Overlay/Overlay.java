package Overlay;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import io.qt.core.QBasicTimer;
import io.qt.core.QRect;
import io.qt.core.QTimerEvent;
import io.qt.core.Qt;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.widgets.QWidget;

public class Overlay extends QWidget {
	public boolean hidden = false;
	public boolean AIactive = false;
	public static float MemUsed = 0;
	public static float MemTot = 0;
	public static String lastCmd = "none";
	public static int cpuTemp = 0;
	public static int gpuTemp = 0;
	public static float cpuUsage = 0;
	public static int cpuMeterHeight = 0;
	public static int memMeterHeight = 0;
	public static int mem = 0;
	public static int counter = 0;
	private static final DecimalFormat df = new DecimalFormat("0.0");
	private int step;
	private QBasicTimer timer;

	public Overlay() {
		setWindowFlags(Qt.WindowType.FramelessWindowHint, Qt.WindowType.WindowTransparentForInput,
				Qt.WindowType.WindowStaysOnTopHint, Qt.WindowType.NoDropShadowWindowHint,
				Qt.WindowType.WindowSystemMenuHint, Qt.WindowType.WindowMaximizeButtonHint,
				Qt.WindowType.X11BypassWindowManagerHint);
		setAttribute(Qt.WidgetAttribute.WA_NoSystemBackground);
		setAttribute(Qt.WidgetAttribute.WA_TranslucentBackground);
		setGeometry(0, 1600, 620, 300);
		setFixedHeight(300);
		setFixedWidth(620);
		showFullScreen();
		step = 0;
		timer = new QBasicTimer();
		timer.start(60, this);
	}

	@Override
	protected void paintEvent(QPaintEvent event) {
		int spacing = 16;
		if (!hidden) {
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
			pn.drawText(10, 24 + spacing * 2, "AI Memory Use: " + df.format(MemUsed/1000) + "/" + df.format(MemTot/1000));
			pn.drawText(10, 24 + spacing * 3, "last AI Voicecommand:");
			pn.drawText(10, 24 + spacing * 4, lastCmd);
			pn.drawText(10, 24 + spacing * 5, "CPU Temp.: " + cpuTemp + "°C");
			pn.drawText(10, 24 + spacing * 6, "GPU Temp.: " + gpuTemp + "°C");
			pn.drawText(10, 24 + spacing * 7, "Mem stats: " + mem + "%");
			pn.drawRect(350,10,10,130);
			pn.fillRect(new QRect(350,140,10,cpuMeterHeight*-1), new QColor(Qt.GlobalColor.green));
			pn.drawText(345,156,"CPU");
			pn.drawRect(383,10,10,130);
			pn.fillRect(new QRect(383,140,10,memMeterHeight*-1), new QColor(Qt.GlobalColor.green));
			pn.drawText(377,156,"MEM");
			if (!OverlayThread.title.equalsIgnoreCase("null")) {
				pn.drawText(420,24 + spacing * 2,"Playing:");
				pn.drawText(420,24 + spacing * 3,OverlayThread.artist);
				pn.drawText(420,24 + spacing * 4,"by "+OverlayThread.title);
			}
			
			if (counter < 20) {
				counter++;
				pn.drawText(10, 24 + spacing * 9, "/");
			} else if (counter < 40) {
				counter++;
				pn.drawText(10, 24 + spacing * 9, "\\");
			} else {
				counter = 0;
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
	}

	@Override
	protected void timerEvent(QTimerEvent e) {
		if (e.timerId() == timer.timerId()) {
			++step;
			update();
		} else {
			super.timerEvent(e);
		}
	}
}
