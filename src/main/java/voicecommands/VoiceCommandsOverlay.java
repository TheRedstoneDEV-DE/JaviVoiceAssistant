package voicecommands;

import configuration.Manager;
import general.CmdMask;
import general.Main;
import tts.TextToSpeech;

public class VoiceCommandsOverlay extends CmdMask{
	Manager man = new Manager();
	public void execute(String command, TextToSpeech tts, Main main) {
		if (command.startsWith("hide")) {
			if (man.get("overlay-module-activated").equalsIgnoreCase("yes")) {				
				main.oth.o.hidden = true;
				tts.speak("hiding overlay");
			} else {
				tts.speak("this module disabled by the configuration file");
			}
		}else if(command.startsWith("show")) {
			if (man.get("overlay-module-activated").equalsIgnoreCase("yes")) {
				tts.speak("showing overlay");
				main.oth.o.hidden = false;
			} else {
				tts.speak("this module disabled by the configuration file");
			}
			
		}
	}
}
