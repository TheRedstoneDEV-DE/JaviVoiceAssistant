package plugins;

import general.Main;
import tts.TextToSpeech;

public interface Plugin {
	public void onLoad();
	public Boolean onUnknownVoiceCommand(String command, TextToSpeech tts, Main main);
}
