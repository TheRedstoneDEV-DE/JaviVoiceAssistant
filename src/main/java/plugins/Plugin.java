package plugins;

import general.Main;
import tts.TextToSpeech;

public interface Plugin {
	public void onLoad();
	public Boolean onVoiceCommand(String command, TextToSpeech tts, Main main);
}
