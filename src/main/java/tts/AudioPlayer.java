package tts;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat.Encoding;
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
			String audiodev = "";
			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
				Mixer mixer = AudioSystem.getMixer(mixerInfo);
				if (System.getenv("AUDIODEV") != null){
					audiodev = System.getenv("AUDIODEV");
				}
				//System.out.println("Found:"+mixer.getMixerInfo().getName());
				if (mixer.isLineSupported(lineInfo) && mixer.getMixerInfo().getName().startsWith(audiodev)) {
					selectedMixer = mixerInfo;					
				}
			}
			if (selectedMixer != null) {
				//System.out.println("selected Output-Device: " + selectedMixer.getName());
				Mixer m = AudioSystem.getMixer(selectedMixer);
				SourceDataLine sdl = (SourceDataLine) m.getLine(lineInfo);
				sdl.open(format);
				sdl.start();
				if (sdl.isControlSupported(FloatControl.Type.MASTER_GAIN) && System.getenv("OUTPUT_GAIN") != null) {
					FloatControl volume = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
				   volume.setValue(20.0f * (float) Math.log10( Float.parseFloat(System.getenv("OUTPUT_GAIN")) / 100.0 ));
				}
				int nRead = 0;
				byte[] abData = new byte[1200];
				while ((nRead != -1)) {
					try {
						nRead = ais.read(abData, 0, abData.length);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					if (nRead >= 0) {
						if (sdl.available() >= 1200) {
							sdl.write(abData, 0, nRead);
							float delay = (1200/(format.getSampleRate()* format.getChannels()*(format.getSampleSizeInBits()/8))*1000);
							Thread.sleep((long) Math.rint(delay));
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
