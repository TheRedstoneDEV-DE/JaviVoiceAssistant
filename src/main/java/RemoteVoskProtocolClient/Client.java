package RemoteVoskProtocolClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
	public static void send(byte[] b) throws IOException {
		if (calculateRMSLevel(b)>17) {
			Socket socket = new Socket("192.168.0.105", 6781);

			// Create input and output streams to read from and write to the server
			OutputStream out = socket.getOutputStream();
			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(socket.getInputStream()));

			out.write(b);

			// in.close();
			out.flush();
			out.close();
			socket.close();
		}

	}

	public static double calculateRMSLevel(byte[] audioData) {
		long lSum = 0;
		for (int i = 0; i < audioData.length; i++)
			lSum = lSum + audioData[i];

		double dAvg = lSum / audioData.length;

		double sumMeanSquare = 0d;

		for (int j = 0; j < audioData.length; j++)
			sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);
		double averageMeanSquare = sumMeanSquare / audioData.length;
		return (Math.pow(averageMeanSquare, 0.5d) + 0.5);
	}
}
