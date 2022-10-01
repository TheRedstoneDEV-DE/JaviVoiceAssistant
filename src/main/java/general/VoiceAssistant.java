package general;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.json.*;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import marytts.modules.synthesis.Voice;
import marytts.tools.voiceimport.HMMVoiceMakeVoice;
import plugins.JPlugin;
import plugins.Manager;
import tts.TextToSpeech;
import voicecommands.VoiceCommandPlayback;

import javax.sound.sampled.AudioSystem;

public class VoiceAssistant implements Runnable {
	ServerSocket Ssocket = null;
	Model model;
	Recognizer recognizer = null;
	Boolean inRange = false;
	int AudibleDelay = 0;
	int sensibility = 20;
	public boolean isRunning = true;
	public TextToSpeech tts = new TextToSpeech();
	Boolean active = false;
	Main m = Main.getMain();
	private Map<String, Class> commands = new HashMap<String, Class>() {
		{
			put("resume", voicecommands.VoiceCommandPlayback.class);
			put("pause", voicecommands.VoiceCommandPlayback.class);
			put("previous", voicecommands.VoiceCommandPlayback.class);
			put("next", voicecommands.VoiceCommandPlayback.class);

			put("show overlay", voicecommands.VoiceCommandsOverlay.class);
			put("hide overlay", voicecommands.VoiceCommandsOverlay.class);
			put("set volume to", voicecommands.VoiceCommandVolume.class);
			put("open", voicecommands.VoiceCommandOpen.class);
			put("reconfigure", voicecommands.VoiceCommandReconf.class);
		}
	};
	private final DecimalFormat df = new DecimalFormat("0");

//  initialize TTS system
	public void initTTS() {
		Voice.getAvailableVoices().stream().forEach(System.out::println);
		tts.setVoice("cmu-rms-hsmm");
	}

	// just process the voice commands
	public void processCommand(String voiceCommand) {
		try {
			final JSONObject obj = new JSONObject(voiceCommand);
			voiceCommand = obj.getString("text");
			if (voiceCommand.equalsIgnoreCase("hey computer")) {// activation command
				active = true;
				tts.speak("Hey");
			} else if (active) {
				try {
					Boolean commandKnown = false;
					for (Map.Entry<String, Class> command : commands.entrySet()) {
						if (voiceCommand.startsWith(command.getKey())) {
							Class<? extends CmdMask> cmd = command.getValue();
							Constructor<? extends CmdMask> constructor = cmd.getConstructor();
							CmdMask result = constructor.newInstance();
							result.execute(voiceCommand, tts, m);
							commandKnown = true;
							m.oth.o.setWord(voiceCommand);
							break;
						}
					}
					if (!commandKnown) {
						for (JPlugin plugin : Manager.plugins) {
							try {
								if (plugin.onUnknownVoiceCommand(voiceCommand, tts, m)) {
									break;
								}
							} catch (Exception e) {
								System.out.println("Error on running a VoiceCommand of a Plugin:");
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				active = false;
			}
			Main.getMain().oth.o.AIactive = active;
		} catch (Exception ex) {
			System.out.println("Failed at void processCommand!");
			ex.printStackTrace();
		}
	}

	public void run() {

		try {
			model = new Model("model");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recognizer = new Recognizer(model, 120000, "[\"" + Main.getMain().vocab + "\", \"[unk]\"]");
		startRec();
	}

	public void restart() {
		System.out.println("restarting Recogniton...");
		startRec();
	}

	public boolean isAudible(byte[] data) {
		if (!inRange) {
			inRange = (getRootMeanSquared(data) > sensibility);
		}
		if (AudibleDelay == 0) {
			inRange = (getRootMeanSquared(data) > sensibility);
		}
		if (AudibleDelay > 1200) {
			AudibleDelay = 0;
		} else {
			AudibleDelay++;
		}
		return inRange;
	}

	public double getRootMeanSquared(byte[] audioData) {
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

	public void startRec() {
		initTTS();
		LibVosk.setLogLevel(LogLevel.DEBUG);
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 60000, 16, 2, 4, 44100, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		TargetDataLine microphone;
		try {
			microphone = (TargetDataLine) AudioSystem.getLine(info);
			microphone.open(format);
			microphone.start();

			int numBytesRead = 0;
			int CHUNK_SIZE = 1024;
			int bytesRead = 0;
			byte[] b = new byte[4096];
			Boolean isAudible = false;
			int counter = 0;
			while (isRunning) {
				numBytesRead = microphone.read(b, 0, CHUNK_SIZE);
				if (isAudible(b))  {
					if (recognizer.acceptWaveForm(b, numBytesRead)) {
						String res = recognizer.getResult();
						if (res != null && res != "{\n" + "  \"text\" : \"\"\n" + "}"
								&& res != "{\n" + "  \"text\" : \"the\"\n" + "}") {
							System.out.println("Recognizer >> Got command: " + res);
							processCommand(res);
							recognizer.reset();
						}
					}
				}
			}
			microphone.close();
			System.out.println("VA_RECOG_THR >> Exited!");
		} catch (

		Exception e) {
			e.printStackTrace();
			restart();
		}
	}
}
