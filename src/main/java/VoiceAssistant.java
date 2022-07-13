
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

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
import tts.TextToSpeech;
import javax.sound.sampled.AudioSystem;

public class VoiceAssistant implements Runnable {
	static ServerSocket Ssocket = null;
	static Model model;
	static Recognizer recognizer = null;
	static Boolean inRange = false;
	static int AudibleDelay = 0;
	private static final DecimalFormat df = new DecimalFormat("0");

//  initialize TTS system
	public static void initTTS() {
		Voice.getAvailableVoices().stream().forEach(System.out::println);
		tts.setVoice("cmu-rms-hsmm");
		tts.speak("6", 1.0f, false, false);
	}

	static TextToSpeech tts = new TextToSpeech();
	static Boolean active = false;

//	convert number-words to number
	public static String toNumber(String word) {
		String[] words = new String[] { "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty",
				"ninety" };
		String[] num = new String[] { "10", "20", "30", "40", "50", "60", "70", "80", "90" };
		int Final = 20;
		for (int i = 0; i < 9; i++) {
			if (word.equalsIgnoreCase(words[i])) {
				Final = i;
				break;
			}
		}

		String ret;
		if (Final == 20) {
			ret = "NaN";
		} else {
			ret = num[Final];
		}
		return ret;
	}

//	just process the voice commands
	public static void processCommand(String voiceCommand) {
		try {
			final JSONObject obj = new JSONObject(voiceCommand);
			voiceCommand = obj.getString("text");
			if (voiceCommand.equalsIgnoreCase("hey computer")) {// activation command
				active = true;
				tts.speak("Hey", 2.0f, false, true);
			} else if (voiceCommand.startsWith("set volume to") && active && voiceCommand.endsWith("percent")) { // volume
				Main.oth.o.lastCmd = voiceCommand;
				active = false;
				Runtime.getRuntime().exec("amixer -D pulse sset Master "
						+ voiceCommand.replaceAll("set volume to", "").replaceAll("percent", "%").replaceAll(" ", ""));
				tts.speak("set master volume to " + toNumber(
						voiceCommand.replaceAll("set volume to", "").replaceAll("percent", "").replaceAll(" ", ""))
						+ " percent", 2.0f, false, true);
			}

			else if (voiceCommand.startsWith("open") && active) {
				Main.oth.o.lastCmd = voiceCommand;
				if (voiceCommand.equalsIgnoreCase("open <program>")) {
					Runtime.getRuntime().exec("screen -dmS <screen_name> <command>");
				}
				tts.speak("opened " + voiceCommand.replaceAll("OPEN ", ""), 2.0f, true, false);
				active = false;
			} else if (voiceCommand.equalsIgnoreCase("what is the cpu usage") && active) {// read the cpu usage
				tts.speak("it is", 2.0f, false, true);
				tts.speak(df.format(MeasureThread.getCpuUsage()).toString(), 2.0f, false, false);
				tts.speak("percent", 2.0f, false, false);
				Main.oth.o.lastCmd = voiceCommand;
				active = false;
			} else if (voiceCommand.equalsIgnoreCase("hide overlay") && active) {// hide the overlay
				if (Main.OverlayEnabled) {
					tts.speak("hiding overlay", 2.0f, true, true);
					active = false;
					Main.oth.o.hidden = true;
				} else {
					tts.speak("this module disabled by the configuration file", 2.0f, true, true);
				}
			} else if (voiceCommand.equalsIgnoreCase("show overlay") && active) {// show the overlay
				if (Main.OverlayEnabled) {
					active = false;
					tts.speak("showing overlay", 2.0f, true, true);
					Main.oth.o.hidden = false;
				} else {
					tts.speak("this module disabled by the configuration file", 2.0f, true, true);
				}
			} else if (voiceCommand.equalsIgnoreCase("resume") && active) {
				if (Main.MprisEnabled) {
					active = false;
					tts.speak("resuming", 2.0f, true, true);
					MPrisCTL.play();
				} else {
					tts.speak("this module disabled by the configuration file", 2.0f, true, true);
				}
			} else if (voiceCommand.equalsIgnoreCase("pause") && active) {
				if (Main.MprisEnabled) {
					active = false;
					tts.speak("pauseing", 2.0f, true, true);
					MPrisCTL.pause();
				} else {
					tts.speak("this module disabled by the configuration file", 2.0f, true, true);
				}
			} else if (voiceCommand.equalsIgnoreCase("previous") && active) {
				if (Main.MprisEnabled) {
					active = false;
					tts.speak("skipped one title back", 2.0f, true, true);
					MPrisCTL.prev();
				} else {
					tts.speak("this module disabled by the configuration file", 2.0f, true, true);
				}
			} else if (voiceCommand.equalsIgnoreCase("next") && active) {
				if (Main.MprisEnabled) {
					active = false;
					tts.speak("skipped one title", 2.0f, true, true);
					MPrisCTL.next();
				} else {
					tts.speak("this module disabled by the configuration file", 2.0f, true, true);
				}
			} else if (voiceCommand.startsWith("set rgb to") && active) {// set the rgb over serial
				if (Main.RGBEnabled) {
					String color = voiceCommand.replaceAll("set rgb to ", "");
					String[] words = new String[] { "red", "green", "blue", "cyan", "purple", "yellow", "auto",
							"custom", };
					int[][] values = new int[][] { new int[] { 255, 10, 10 }, new int[] { 10, 255, 10 },
							new int[] { 10, 10, 255 }, new int[] { 10, 255, 255 }, new int[] { 153, 51, 255 },
							new int[] { 255, 255, 102 }, };
					int Final = 20;
					for (int i = 0; i < 8; i++) {
						if (color.equalsIgnoreCase(words[i])) {
							Final = i;
							break;
						}
					}

					int[] ret;
					if (Final == 20) {
						ret = new int[] {};
					} else if (Final == 6) {
						Main.jdbind.auto = true;
					} else {
						Main.jdbind.auto = false;
						Main.jdbind.setColor(values[Final]);
					}

					tts.speak("set rgb color to " + color, 2.0f, true, true);
				} else {
					tts.speak("this module disabled by the configuration file", 2.0f, true, true);
				}
			}
			Main.oth.o.AIactive = active;
		} catch (Exception ex) {
			System.out.println("Failed at void processCommand!");
			ex.printStackTrace();
		}
	}

	public void run() {

		try {
			model = new Model("vosk-model-small-en-us-0.15");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recognizer = new Recognizer(model, 120000,
				"[\"rgb auto red green blue cyan purple yellow the hey computer pause resume next previous set volume to percent open steam hide show overlay what is the cpu usage start conversation ten twenty thirty forty fifty sixty seventy eighty ninety\", \"[unk]\"]");
		startRec();
	}

	public static void restart() {
		System.out.println("restarting Recogniton...");
		startRec();
	}

	public static boolean isAudible(byte[] data) {
		if (!inRange) {
			inRange = (getRootMeanSquared(data) > Main.sensibility);
		}
		if (AudibleDelay == 0) {
			inRange = (getRootMeanSquared(data) > Main.sensibility);
		}
		if (AudibleDelay > 1000) {
			AudibleDelay = 0;
		} else {
			AudibleDelay++;
		}
		return inRange;
	}

	public static double getRootMeanSquared(byte[] audioData) {
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

	public static void startRec() {
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

			while (bytesRead <= 100000000) {
				numBytesRead = microphone.read(b, 0, CHUNK_SIZE);
				bytesRead += numBytesRead;
				if (isAudible(b)) {
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
				Thread.sleep(1);
			}
			System.out.println(recognizer.getFinalResult());
			microphone.close();

			restart();
		} catch (

		Exception e) {
			e.printStackTrace();
			restart();
		}
	}
}
