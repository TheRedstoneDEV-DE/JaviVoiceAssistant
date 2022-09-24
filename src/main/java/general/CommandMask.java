package general;

import tts.TextToSpeech;

public interface CommandMask {
	public void execute(String command, TextToSpeech tts, Main main);
}
