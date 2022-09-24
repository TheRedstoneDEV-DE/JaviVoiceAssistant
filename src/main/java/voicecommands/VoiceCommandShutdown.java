package voicecommands;

import general.CmdMask;
import general.Main;
import general.VoiceAssistant;
import tts.TextToSpeech;

public class VoiceCommandShutdown extends CmdMask{
	public void execute(String command, TextToSpeech tts, Main main) {
		tts.speak("shutting down");
		main.shutdown();
	}
}
