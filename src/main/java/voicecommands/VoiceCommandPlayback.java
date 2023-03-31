package voicecommands;

import general.CmdMask;
import configuration.Manager;
import general.Main;
import tts.TextToSpeech;

public class VoiceCommandPlayback extends CmdMask{
	Manager man = new Manager();
	public void execute(String command, TextToSpeech tts, Main main) {
		if (man.get("mpris-module-activated").equalsIgnoreCase("yes")) {
			if (command.equalsIgnoreCase("resume")) {
				tts.speak("resuming");
				main.mpc.play();
			} else if (command.equalsIgnoreCase("pause")) {
				tts.speak("pauseing");
				main.mpc.pause();
			} else if (command.equalsIgnoreCase("previous")) {
				tts.speak("skipped one title back");
				main.mpc.prev();
			} else if (command.equalsIgnoreCase("next")) {
				tts.speak("skipped one title");
				main.mpc.next();
			}
		} else {
		tts.speak("this module disabled by the configuration file");
		}
	}
}
