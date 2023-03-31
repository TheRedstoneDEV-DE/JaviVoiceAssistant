package progsetup;

import configuration.Manager;
import general.Main;
import io.qt.widgets.QApplication;
import io.qt.widgets.QDialog;
import io.qt.widgets.QTableWidgetItem;

public class Ui extends QDialog {
	Ui_Dialog diag = new Ui_Dialog();
	Manager cfgman = new Manager();

	public static void init() {
		if (!general.Main.getMain().QtInitialized) {
			QApplication.initialize(new String[] { "Test", "" });
			Ui mainw = new Ui();
			mainw.show();
			mainw.setWindowTitle("Setup");
			QApplication.exec();
		} else {
			general.Main.getMain().oth.o.progconf = true;
		}
	}

	public Ui() {
		// Place what you made in Designer onto the main window.
		diag.setupUi(this);
		diag.btn_done.clicked.connect(this, "onApply()");
		diag.btn_add.clicked.connect(this, "onAdd()");
		String[] prognames=cfgman.get("progNames").split(";");
		String[] progcommands=cfgman.get("progCommands").split(";");
		for (int i = 0; i<prognames.length; i++){
			diag.table.setItem(i, 0, new QTableWidgetItem(prognames[i]));
			diag.table.setItem(i, 1, new QTableWidgetItem(progcommands[i]));
		}
	}

	private void onApply() {
		for (int i = 0; i<100; i++){
			if(diag.table.item(i, 0)==null || diag.table.item(i, 0).text() == ""){
				break;
			}else if (i==0){
				cfgman.set("progNames", diag.table.item(i, 0).text());
				cfgman.set("progCommands", diag.table.item(i, 1).text());
			}else{
				cfgman.set("progNames", diag.table.item(i, 0).text()+";"+cfgman.get("progNames"));
				cfgman.set("progCommands", diag.table.item(i, 1).text()+";"+cfgman.get("progCommands"));
			}
		}
		this.close();
		Main.getMain().restart();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onAdd() {
		String name = diag.progname.toPlainText();
		String command = diag.progexec.toPlainText();
		int got = 404;
		for (int i = 0; i<100; i++){
			if(diag.table.item(i, 0)==null || diag.table.item(i, 0).text() == ""){
				got=i;
				break;
			}
		}
		if (got!=404){
			diag.table.setItem(got, 0, new QTableWidgetItem(name));
			diag.table.setItem(got, 1, new QTableWidgetItem(command));
		}
	}
}
