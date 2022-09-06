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
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;
import marytts.util.data.audio.MaryAudioUtils;

/**
 * @author GOXR3PLUS
 *
 */
public class TextToSpeech {

	private AudioPlayer tts;
	private MaryInterface marytts;

	public TextToSpeech() {
		try {
			marytts = new LocalMaryInterface();

		} catch (MaryConfigurationException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void speak(String text) {

		try (AudioInputStream audio = marytts.generateAudio(text)) {
			AudioPlayer ap = new AudioPlayer(audio);
			ap.play();

		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error saying phrase.", ex);
		}
	}

	public void setAudio(AudioInputStream audio) {
		try {
			AudioFormat format = audio.getFormat();
			DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, format);

			Mixer.Info selectedMixer = null;

			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
				Mixer mixer = AudioSystem.getMixer(mixerInfo);
				if (mixer.isLineSupported(lineInfo)) {
					selectedMixer = mixerInfo;
					break;
				}
			}

			if (selectedMixer != null) {
				Mixer m = AudioSystem.getMixer(selectedMixer);
				SourceDataLine sdl = (SourceDataLine) m.getLine(lineInfo);
				sdl.open(format);
				sdl.start();
				int nRead = 0;
				byte[] abData = new byte[512];
				sdl.drain();
				while ((nRead != -1)) {
					try {
						nRead = audio.read(abData, 0, abData.length);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					if (nRead >= 0) {
						System.out.println(sdl.available());
						System.out.println("start");
						if (sdl.available() >= 512) {
							sdl.write(abData, 0, nRead);
						} else {
							sdl.drain();
							sdl.write(abData, 0, nRead);
						}
						System.out.println("end");
					}
				}
				sdl.drain();
				sdl.close();
				audio.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
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