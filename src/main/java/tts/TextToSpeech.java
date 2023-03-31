package tts;

import java.io.IOException;
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

	public TextToSpeech(boolean isRemote, Client connection) {
		remote = isRemote;
		client=connection;
		if (!isRemote) {
			try {
				marytts = new LocalMaryInterface();

			} catch (MaryConfigurationException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void speak(String text) {
		if (remote) {
			client.send(("TTS_PACKET:"+text+"\n").getBytes());
		} else {

			try (AudioInputStream audio = marytts.generateAudio(text)) {
				AudioPlayer ap = new AudioPlayer(audio);
				ap.play();

			} catch (Exception ex) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error saying phrase.", ex);
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
		marytts.setVoice(voice);
	}

}