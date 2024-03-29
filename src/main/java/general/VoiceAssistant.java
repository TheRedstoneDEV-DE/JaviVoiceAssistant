package general;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import com.openai.OpenAPIConnector;
import org.json.*;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import marytts.modules.synthesis.Voice;
import marytts.server.http.InfoRequestHandler;
import plugins.JPlugin;
import plugins.Manager;
import remoteProcessingClient.Client;
import tts.TextToSpeech;

import javax.sound.sampled.AudioSystem;

public class VoiceAssistant implements Runnable {
	Boolean remote;
	Client client;
	ServerSocket Ssocket = null;
	Model model;
	Recognizer recognizer = null;
	Boolean inRange = false;
	int AudibleDelay = 0;
	int sensibility = 20;
	public boolean isRunning = true;
	public TextToSpeech tts;
	Boolean active = false;
	public Boolean aiMode = false;
	Main m = Main.getMain();

	public VoiceAssistant(boolean isRemote, Client connection, boolean mimic, String host) {
		remote = isRemote;
		client = connection;
		tts = new TextToSpeech(isRemote, connection, mimic,host);
	}

	public VoiceAssistant() {
	}

	private Map<String, Class> commands = new HashMap<String, Class>() {
		{
			put("resume", voicecommands.VoiceCommandPlayback.class);
			put("pause", voicecommands.VoiceCommandPlayback.class);
			put("previous", voicecommands.VoiceCommandPlayback.class);
			put("next", voicecommands.VoiceCommandPlayback.class);
			put("start artificial intelligence", voicecommands.VoiceCommandAI.class);
			put("stop artificial intelligence", voicecommands.VoiceCommandAI.class);
			put("show overlay", voicecommands.VoiceCommandsOverlay.class);
			put("hide overlay", voicecommands.VoiceCommandsOverlay.class);
			put("set volume to", voicecommands.VoiceCommandVolume.class);
			put("open", voicecommands.VoiceCommandOpen.class);
			put("reconfigure", voicecommands.VoiceCommandReconf.class);
			put("reconfigure programs", voicecommands.VoiceCommandReconf.class);
		}
	};

	// initialize TTS system
	public void initTTS() {
		Voice.getAvailableVoices().stream().forEach(System.out::println);
		tts.setVoice("cmu-rms-hsmm");
	}

	// just process the voice commands
	public void processCommand(String voiceCommand) {
		try {
			final JSONObject obj = new JSONObject(voiceCommand);
			voiceCommand = obj.getString("text");
			if (voiceCommand.equalsIgnoreCase("hey javi")) {// activation command
				active = true;
				tts.speak("Hey");
			} else if (active && !aiMode) {
				active = false;
				try {
					Boolean commandKnown = false;
					for (Map.Entry<String, Class> command : commands.entrySet()) {
						if (voiceCommand.startsWith(command.getKey())) {
							Class<? extends CmdMask> cmd = command.getValue();
							Constructor<? extends CmdMask> constructor = cmd.getConstructor();
							CmdMask result = constructor.newInstance();
							result.execute(voiceCommand, tts, m);
							commandKnown = true;
							if (Main.getMain().man.get("overlay-module-activated").equalsIgnoreCase("yes")) {
								m.oth.o.setWord(voiceCommand);
							}
							break;
						}
					}
					if (!commandKnown) {
						for (Map.Entry<String, JPlugin> command : Manager.plugins.entrySet()) {
							String key = command.getKey();
							if (key.contains(";")) {
								String[] commandList = key.split(";");
								for (String command_pl : commandList) {
									if (voiceCommand.startsWith(command_pl)) {
										try {
											command.getValue().onVoiceCommand(voiceCommand, tts, m);
											break;
										} catch (Exception e) {

										}
									}
								}
							} else {
								if (voiceCommand.startsWith(key)) {
									try {
										command.getValue().onVoiceCommand(voiceCommand, tts, m);
										break;
									} catch (Exception e) {

									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(active && aiMode){
				if(voiceCommand.startsWith("stop artificial intelligence")){
					tts.speak("switched to normal mode, to continue say any word");
					aiMode = false;
					isRunning = false;
				}else {
					Stream<String> result = new OpenAPIConnector().infer(voiceCommand,"http://"+m.man.get("llm-host")+"/v1/chat/completions",m.man.get("llm-model"),m.man.get("llm-api-key"), m.man.get("llm-max-tokens")).lines();
					result.forEach(line -> {
						System.out.println(line);
						if (line!="") {
							tts.speak(line);
						}
					});
					active = false;
				}
			}
			if (Main.getMain().man.get("overlay-module-activated").equalsIgnoreCase("yes")) {
				m.oth.o.AIactive = active;
			}
		} catch (Exception ex) {
			System.out.println("Failed at void processCommand!");
			ex.printStackTrace();
		}
	}
	public String send(String URL_GET) throws IOException, InterruptedException{
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.timeout(Duration.ofSeconds(60))
				.uri(URI.create(URL_GET))
				.build();
		return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
	}
	@Override
	public void run() {
		if (remote) {
			startRec();
		} else {
			try {
				model = new Model("model");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			recognizer = new Recognizer(model, 120000, "[\"" + Main.getMain().vocab + "\", \"\"]");
			startRec();
		}
	}

	public void restart() {
		try {
		//model = new Model("model");
		System.out.println("restarting Recogniton...");
		recognizer = new Recognizer(model, 120000, "[\"" + Main.getMain().vocab + "\", \"\"]");
		startRec();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public void restartWithFullDict(){
		try {
			//model = new Model("big_model");
			recognizer = new Recognizer(model, 120000);
			startRec();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isAudible(byte[] data) {
		if (!inRange) {
			inRange = (getRootMeanSquared(data) > sensibility);
		}
		if (AudibleDelay == 0) {
			inRange = (getRootMeanSquared(data) > sensibility);
			if (inRange) {
				AudibleDelay = 0;
			}
		}
		if (AudibleDelay > 2400) {
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
		if (!remote) {
			initTTS();
			LibVosk.setLogLevel(LogLevel.DEBUG);
		}
		isRunning = true;
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 60000, 16, 2, 4, 44100, false);
		TargetDataLine microphone;
		try {
			DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, format);
			Mixer.Info selectedMixer = null;
			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
				Mixer mixer = AudioSystem.getMixer(mixerInfo);
				String audiodev = "";
				if (System.getenv("AUDIODEV") != null) {
					audiodev = System.getenv("AUDIODEV");
				}
				if (mixer.isLineSupported(lineInfo) && mixer.getMixerInfo().getName().contains(audiodev)) {
					selectedMixer = mixerInfo;
					System.out.println("selected Input-Device: " + mixerInfo.getName());
					break;
				}
			}
			if (selectedMixer != null) {
				Mixer m = AudioSystem.getMixer(selectedMixer);
				microphone = (TargetDataLine) m.getLine(lineInfo);
				microphone.open(format);
				microphone.start();

				int numBytesRead = 0;
				int CHUNK_SIZE = 512;
				byte[] b = new byte[512];
				while (isRunning) {
					numBytesRead = microphone.read(b, 0, CHUNK_SIZE);
					if (isAudible(b)) {
						if (remote) {
							client.sendAudio(b);
						} else {
							if (recognizer.acceptWaveForm(b, numBytesRead)) {
								String res = recognizer.getResult();
								if (res.contains("[unk] ")) {
									res = res.replaceAll("[unk] ", "");
								} else if (res.contains("[unk]")) {
									res = res.replaceAll("[unk]", "");
								}
								if (res != null && res != "{\n" + "  \"text\" : \"\"\n" + "}"
										&& res != "{\n" + "  \"text\" : \"the\"\n" + "}"
										&& !res.equalsIgnoreCase("{\n" + "  \"text\" : \"\"\n" + "}")) {
									System.out.println("Recognizer1 >> Got command: " + res);
									processCommand(res);
									recognizer.reset();
								}
							}
						}
					}
				}
				microphone.drain();
				microphone.close();
				m.close();
				System.out.println("VA_RECOG_THR >> Exited!");
				if (aiMode) {
					restartWithFullDict();
				}else{
					restart();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
