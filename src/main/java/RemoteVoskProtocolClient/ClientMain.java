package RemoteVoskProtocolClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class ClientMain {
	public static Boolean muted = false;
	public static Boolean restart = false;
	
	public static void start() {
		try {
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 60000, 16, 2, 4, 44100, false);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			TargetDataLine microphone;
			SourceDataLine speakers;

			microphone = (TargetDataLine) AudioSystem.getLine(info);

			microphone.open(format);
			microphone.start();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int numBytesRead;
			int CHUNK_SIZE = 1024;
			int bytesRead = 0;
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
			speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			speakers.open(format);
			speakers.start();
			byte[] b = new byte[4096];
			while (bytesRead <= 100000000 && !restart) {
				if (!muted) {
					numBytesRead = microphone.read(b, 0, CHUNK_SIZE);
					bytesRead += numBytesRead;
					out.write(b, 0, numBytesRead);
					speakers.write(b, 0, numBytesRead);
					try {
						Client.send(b);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//System.out.println(".");
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			speakers.drain();
            speakers.close();
            microphone.close();
            System.out.println("restart");
            restart=false;
            start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
