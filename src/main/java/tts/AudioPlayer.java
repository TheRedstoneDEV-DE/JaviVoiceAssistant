package tts;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.spi.MixerProvider;

public class AudioPlayer {
	AudioInputStream ais = null;

	public AudioPlayer(AudioInputStream audio) {
		ais = audio;
	}

	public void play() {
		try {
			AudioFormat format = ais.getFormat();
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
				byte[] abData = new byte[12000];
				while ((nRead != -1)) {
					try {
						nRead = ais.read(abData, 0, abData.length);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					if (nRead >= 0) {
						if (sdl.available() >= 12000) {
							sdl.write(abData, 0, nRead);
							Thread.sleep(366);
						}
					}
				}
				sdl.drain();
				sdl.close();
				ais.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
