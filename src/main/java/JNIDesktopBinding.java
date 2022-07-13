import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class JNIDesktopBinding implements Runnable{
	static DecimalFormat df = new DecimalFormat("0");
	public static OutputStream out;
	static Boolean auto;
	static double[] bak = null;
	public static JNIDesktopBinding jdbind = new JNIDesktopBinding();
	public void run() {
		// TODO Auto-generated method stub
		System.load(Main.RGBLibFile);
		bak = getAVG();
		System.setProperty("gnu.io.rxtx.SerialPorts", Main.RGBSerPort);
		try {
			auto=true;
			jdbind.connect(Main.RGBSerPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public native static double[] getAVG();
	
	public static void setColor(int[] color) {
		int r = color[0];
		int g = color[1];
		int b = color[2];
		if (!auto) {
			try {
				Thread.sleep(1000);// Wait so the transmission to microcontroller can be finished and isn't
									// interrupted
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out.write("clr\n".getBytes());
				out.write((df.format(r).toString() + "\n").getBytes());
				out.write((df.format(g).toString() + "\n").getBytes());
				out.write((df.format(b).toString() + "\n").getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Set color");
		}
		System.out.println("currently in auto mode!");
	}
	
	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			int timeout = 2000;
			CommPort commPort = portIdentifier.open(this.getClass().getName(), timeout);
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(2000000, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
				out = serialPort.getOutputStream();
				while (true) {
					if (auto) {
						double[] avg = getAVG();
						if (avg[0] != bak[0]) {
							out.write("clr\n".getBytes());
							out.write((df.format(avg[2]).toString() + "\n").getBytes());
							out.write((df.format(avg[1]).toString() + "\n").getBytes());
							out.write((df.format(avg[0]).toString() + "\n").getBytes());
							//out.flush();
							//System.out.print((df.format(avg[2]).toString()+"\n")+(df.format(avg[1]).toString()+"\n")+(df.format(avg[0]).toString()+"\n"));
							//System.out.println("Automatic adjust");
							bak = avg;
						}
					}

					Thread.sleep(100);
				}
			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}
}
