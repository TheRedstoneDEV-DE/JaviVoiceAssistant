package voicecommands;
import java.io.IOException;

import general.CmdMask;
import general.Main;
import tts.TextToSpeech;

public class VoiceCommandVolume extends CmdMask{
	public void execute(String command, TextToSpeech tts, Main main) {
		try {
			String exec = "amixer -D pulse sset Master " + toNumber(
					command.replaceAll("set volume to", "").replaceAll("percent", "").replaceAll(" ", "")) + "%";
			Runtime.getRuntime().exec(exec.split(" "));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tts.speak("set master volume to " +
				command.replaceAll("set volume to", "").replaceAll("percent", "").replaceAll(" ", "")
				+ " percent");
	}
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
}
