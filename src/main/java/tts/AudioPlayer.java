package tts;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import marytts.util.data.audio.MonoAudioInputStream;
import marytts.util.data.audio.StereoAudioInputStream;

/**
 * A single Thread Audio Player Once used it has to be initialised again
 * 
 * @author GOXR3PLUS
 *
 */
public class AudioPlayer extends Thread {

	public static void setAudio(AudioInputStream audio) {
		try {
			AudioFormat format = audio.getFormat();
			DataLine.Info lineInfo = new DataLine.Info(Clip.class, format);

			Mixer.Info selectedMixer = null;

			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
				Mixer mixer = AudioSystem.getMixer(mixerInfo);
				if (mixer.isLineSupported(lineInfo)) {
					selectedMixer = mixerInfo;
					break;
				}
			}

			if (selectedMixer != null) {
				Clip clip = AudioSystem.getClip(selectedMixer);
				clip.open(audio);
				clip.start();
				do { Thread.sleep(100); } while (clip.isRunning());
				clip.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
