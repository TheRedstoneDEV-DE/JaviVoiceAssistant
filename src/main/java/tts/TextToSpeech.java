package tts;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.modules.synthesis.Voice;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;

import remoteProcessingClient.Client;

public class TextToSpeech {

	private MaryInterface marytts;
	private Boolean remote;
	private Client client;
	private Boolean mimic;
	private String host;

	public TextToSpeech(boolean isRemote, Client connection, boolean mimic, String host) {
		remote = isRemote;
		client=connection;
		this.mimic=mimic;
		this.host = host;
		if (!isRemote && !mimic) {
			try {
				marytts = new LocalMaryInterface();

			} catch (MaryConfigurationException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			}
		}
		System.out.println("TTS >> Initialised!");
	}

	public void speak(String text) {
		if (!mimic) {
			if (remote) {
				client.send(("TTS_PACKET:" + text + "\n").getBytes());
			} else {

				try (AudioInputStream audio = marytts.generateAudio(text)) {
					AudioPlayer ap = new AudioPlayer(audio);
					ap.play();

				} catch (Exception ex) {
					Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error saying phrase.", ex);
				}
			}
		}else{
			try {
				String voice = "en_US/cmu-arctic_low%23rms";
				URL url = new URL("http://"+host+"/api/tts?voice="+voice);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");

				// set request parameters
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(text.getBytes());
				os.flush();
				os.close();

				InputStream in = new BufferedInputStream(conn.getInputStream());
				AudioInputStream ais = AudioSystem.getAudioInputStream(in);
				AudioPlayer player = new AudioPlayer(ais);
				player.play();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public Collection<Voice> getAvailableVoices() {
		return Voice.getAvailableVoices();
	}

	public MaryInterface getMarytts() {
		return marytts;
	}

	public List<AudioEffect> getAudioEffects() {
		return StreamSupport.stream(AudioEffects.getEffects().spliterator(), false).collect(Collectors.toList());
	}

	public void setVoice(String voice) {
		if(!mimic) marytts.setVoice(voice);
	}

}