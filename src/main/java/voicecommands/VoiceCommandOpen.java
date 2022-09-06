package voicecommands;

import java.io.IOException;

import general.CmdMask;
import general.Main;
import tts.TextToSpeech;

public class VoiceCommandOpen extends CmdMask{
	public void execute(String command, TextToSpeech tts, Main main) {
		String[] apps = {
			"kate",
			"dolphin",
			"steam" //and so on
		};
		String selApp = "";
		for (String app : apps) {
			if(command.contains(app)) {
					selApp = app;
					break;
			}
		}
		tts.speak("opened "+selApp);
		try {
			Runtime.getRuntime().exec("screen -dmS "+selApp+"_screen "+selApp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
