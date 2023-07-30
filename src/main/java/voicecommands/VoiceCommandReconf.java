package voicecommands;

import general.CmdMask;
import general.Main;
import tts.TextToSpeech;

public class VoiceCommandReconf extends CmdMask {
	public void execute(String command, TextToSpeech tts, Main main) {
		if (command.equalsIgnoreCase("reconfigure")) {
			tts.speak("reconfiguring ui opened");
			firstTimeSetup.Ui.init(true, tts);
		}else {
			tts.speak("reconfiguring programs");
			progsetup.Ui.init();
		}
	}
}
