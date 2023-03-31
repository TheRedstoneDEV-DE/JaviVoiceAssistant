package remoteProcessingClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

import general.Main;
import general.VoiceAssistant;

public class Client {
    OutputStream outTTS;
    OutputStream outSTT;
    public void connect(String ipaddress, String dict, VoiceAssistant va){
        try {
            SSLSocketFactory sslSocketFactory = 
            (SSLSocketFactory)SSLSocketFactory.getDefault();
            SSLSocket serverConnTTS = (SSLSocket) sslSocketFactory.createSocket(ipaddress, 8079);
            SSLSocket serverConnSTT = (SSLSocket)sslSocketFactory.createSocket(ipaddress, 8078);
            serverConnTTS.setEnabledCipherSuites(serverConnTTS.getSupportedCipherSuites());
            serverConnSTT.setEnabledCipherSuites(serverConnSTT.getSupportedCipherSuites());




            outTTS = serverConnTTS.getOutputStream();
            BufferedInputStream inTTS = new BufferedInputStream(serverConnTTS.getInputStream());
            outSTT = serverConnSTT.getOutputStream();
            BufferedReader inSTT = new BufferedReader(new InputStreamReader(serverConnSTT.getInputStream()));
            outSTT.write(("INIT_PACKET:dic="+dict).getBytes());
            
            AudioFormat format = new AudioFormat(Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);
			DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, format);
			Mixer.Info selectedMixer = null;
			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
				Mixer mixer = AudioSystem.getMixer(mixerInfo);
				if (mixer.isLineSupported(lineInfo)) {
					selectedMixer = mixerInfo;
					break;
				}
			}
            Mixer m = AudioSystem.getMixer(selectedMixer);
			SourceDataLine sdl = (SourceDataLine) m.getLine(lineInfo);
		    sdl.open(format);
			sdl.start();
            Thread t = new Thread(){
                int bytesRead; 
                byte[] audioArr = new byte[4096];
                public void run(){
                    try {
                        while ((bytesRead = inTTS.read(audioArr)) != -1){
                            if (bytesRead >= 0) {
                                if (sdl.available() >= 4096) {
                                    sdl.write(audioArr, 0, bytesRead);
                                    Thread.sleep(100);
                                }
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };
            t.start();
            while (true){
                String line = inSTT.readLine();
                if(line != null | line != ""){
                    System.out.println("Got Voicecommand from Server: "+line);
                    va.processCommand(line);
                }
            }


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LineUnavailableException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public void send(byte[] b){
        try {
            outTTS.write(b);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void sendAudio(byte[] b){
        try {
            outSTT.write(b);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
