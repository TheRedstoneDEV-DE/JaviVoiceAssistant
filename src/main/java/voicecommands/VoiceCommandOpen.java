package voicecommands;

import java.io.IOException;

import general.CmdMask;
import general.Main;
import tts.TextToSpeech;

public class VoiceCommandOpen extends CmdMask{
	public void execute(String command, TextToSpeech tts, Main main) {
		String[] prognames=main.man.get("progNames").split(";");
		String[] progcommands=main.man.get("progCommands").split(";");
		int sel = 404;
		for (int i = 0; i<prognames.length; i++){
			if(command.contains(prognames[i])) {
				sel=i;
				break;
			}
		}
		if(sel == 404){
			tts.speak("requested program not found!");
		}else{
			tts.speak("opened "+prognames[sel]);
			try {
				String[] commandargs = progcommands[sel].split(" ");
				Process process = Runtime.getRuntime().exec(commandargs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
