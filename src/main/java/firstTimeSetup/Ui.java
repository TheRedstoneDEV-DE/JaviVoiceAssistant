package firstTimeSetup;

import configuration.Manager;
import general.Main;
import io.qt.widgets.QApplication;
import io.qt.widgets.QDialog;
import tts.TextToSpeech;

public class Ui extends QDialog {
	Ui_Dialog diag = new Ui_Dialog();
	Manager cfgman = new Manager();
	static Boolean rec;
	static TextToSpeech tts;
	public static void init(boolean reconf, TextToSpeech tts) {
		rec = reconf;
		Ui.tts = tts;
		if (!general.Main.getMain().QtInitialized) {
			QApplication.initialize(new String[] { "Test", "" });
			Ui mainw = new Ui();
			mainw.show();
			mainw.setWindowTitle("Setup");
			QApplication.exec();
		}else {
			general.Main.getMain().oth.o.reconf=true;
		}
	}

	public Ui() {
		// Place what you made in Designer onto the main window.
		diag.setupUi(this);
		diag.pushButton.clicked.connect(this, "onApply()");
	}

	private void onApply() {
		if (diag.MP_en.isChecked()) {
			cfgman.set("mpris-module-activated", "yes");
		} else {
			cfgman.set("mpris-module-activated", "no");
		}
		cfgman.set("mpris-module-busname", diag.MP_Bus.text());
		cfgman.set("mpris-module-objectpath", diag.MP_objpath.text());

		if (diag.SYS_en.isChecked()) {
			cfgman.set("overlay-module-activated", "yes");
		} else {
			cfgman.set("overlay-module-activated", "no");
		}
		cfgman.set("system-status-module-cpu-temperature-file", diag.SYS_temp.text());

		if (diag.SYS_smi.isChecked()) {
			cfgman.set("system-status-module-nvgpu-activated", "yes");
		} else {
			cfgman.set("system-status-module-nvgpu-activated", "no");
		}
		/*
		if (diag.OV_en.isChecked()) {
			cfgman.set("overlay-module-activated", "yes");
		} else {
			cfgman.set("overlay-module-activated", "no");
		}
		 */
		if (diag.DC_en.isChecked()) {
			cfgman.set("discord-rpc-module-activated", "yes");
		} else {
			cfgman.set("discord-rpc-module-activated", "no");
		}

		if (diag.checkBox_6.isChecked()) {
			cfgman.set("use-local-recognition", "yes");
		} else {
			cfgman.set("use-local-recognition", "no");
		}

		cfgman.set("rms-threshold", Integer.toString(diag.MIC_rms.value()));
		cfgman.set("overlay-position", diag.OV_pos.text());
		if (!rec) {
			cfgman.set("progNames", "");
			cfgman.set("progCommands", "");
		}

		this.close();
		if (!rec) {
			Main.main(new String[]{"example.jar"});
		}else{
			tts.speak("configuration saved, to apply do a full restart of the application");
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
