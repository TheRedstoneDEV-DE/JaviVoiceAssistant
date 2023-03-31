package progsetup;

import configuration.Manager;
import io.qt.core.QCoreApplication;
import io.qt.core.QRect;
import io.qt.core.QSize;
import io.qt.widgets.QDialog;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QTableWidget;
import io.qt.widgets.QTableWidgetItem;
import io.qt.widgets.QTextEdit;

public class Ui_Dialog {

    public QPushButton btn_add;
    public QPushButton btn_done;
    public QTextEdit progname;
    public QTextEdit progexec;
    public QTableWidget table;
    private String theme = "\n"
    		+ "/*-----QWidget-----*/\n"
    		+ "QWidget\n"
    		+ "{\n"
    		+ "	background-color: #17212b;\n"
    		+ "	color: #fff;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "/*-----QLabel-----*/\n"
    		+ "QLabel\n"
    		+ "{\n"
    		+ "	background-color: transparent;\n"
    		+ "	color: #fff;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "/*-----QLineEdit-----*/\n"
    		+ "QLineEdit\n"
    		+ "{\n"
    		+ " background-color: #243342;\n"
    		+ "	color : white;\n"
    		+ "	border: 1px solid darkgray;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "QLineEdit:hover \n"
    		+ "{\n"
    		+ "	background-color: #2d4053;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    	
    		+ "/*-----QCheckBox-----*/\n"
    		+ "QCheckBox\n"
    		+ "{\n"
    		+ "	background-color: transparent;\n"
    		+ "	color: #fff;\n"
    		+ "	border: none;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "QCheckBox::indicator\n"
    		+ "{\n"
    		+ "    color: #b1b1b1;\n"
    		+ "    background-color: #323232;\n"
    		+ "    border: 1px solid #00c3d1;\n"
    		+ "    width: 12px;\n"
    		+ "    height: 12px;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "QCheckBox::indicator:checked\n"
    		+ "{\n"
    		+ "    background-color: #0098a3;\n"
    		+ "    border: 1px solid #00c3d1;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "QCheckBox::indicator:unchecked:hover\n"
    		+ "{\n"
    		+ "	border: 1px solid #0098a3; \n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "QCheckBox::disabled\n"
    		+ "{\n"
    		+ "	color: #656565;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "QCheckBox::indicator:disabled\n"
    		+ "{\n"
    		+ "	background-color: #656565;\n"
    		+ "	color: #656565;\n"
    		+ "    border: 1px solid #656565;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "/*-----QRadioButton-----*/\n"
    		+ "QRadioButton \n"
    		+ "{\n"
    		+ "	color: lightgray;\n"
    		+ "	background-color: transparent;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "QRadioButton::indicator::unchecked:hover \n"
    		+ "{\n"
    		+ "	background-color: #fff;\n"
    		+ "	border: 2px solid #3a546e;\n"
    		+ "	border-radius: 6px;\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "QRadioButton::indicator::checked \n"
    		+ "{\n"
    		+ "	border: 2px solid #3a546e;\n"
    		+ "	border-radius: 6px;\n"
    		+ "	background-color: qlineargradient(spread:repeat, x1:1, y1:0, x2:1, y2:1, stop:0 rgba(23, 43, 68, 255),stop:1 rgba(33, 51, 75, 255)); \n"
    		+ "	width: 9px; \n"
    		+ "	height: 9px; \n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "/*-----QButton-----*/\n"
    		+ "QPushButton \n"
    		+ "{\n"
    		+ "	color: #fff;\n"
    		+ "	background-color: #243342;\n"
    		+ " border: 1px solid #656565;\n"
    		+ "\n"
    		+ "}\n"
    		+ "\n"
    		+ "\n"
    		+ "QPushButton::hover \n"
    		+ "{\n"
    		+ "	background-color: #2d4053;\n"
    		+ "	color: #fff;\n"
    		+ "\n"
    		+ "}\n"
    		+ "QPushButton::pressed \n"
    		+ "{\n"
    		+ "	background-color: black;\n"
    		+ "	color: #fff;\n"
    		+ "\n"
    		+ "}\n"
    		+ "QSlider::groove:horizontal {\n"
    		+ "    border: 1px solid #2d4053;\n"
    		+ "    height: 5px;\n"
    		+ "    background: #243342;\n"
    		+ "    margin: 0px;\n"
    		+ "}\n"
    		+ "QSlider::handle:horizontal {\n"
    		+ "    background: #243342;\n"
    		+ "    border: 1px solid #2d4053;\n"
    		+ "    width: 8px;\n"
    		+ "    height: 16px;\n"
    		+ "}"
    		+ "QSlider::sub-page:horizontal {\n"
    		+ "background-color: #0098a3;\n"
    		+ "border: 1px solid #777;\n"
    		+ "height: 8px;\n"
    		+ "}"
    		+ "\n"
    		+ "";
	Manager cfgman = new Manager();
    public Ui_Dialog() { super(); }

    public void setupUi(QDialog Dialog) {
		Dialog.setObjectName("Setup");
		Dialog.resize(new QSize(359, 431).expandedTo(Dialog.minimumSizeHint()));
		Dialog.setStyleSheet(theme);
		btn_add = new QPushButton(Dialog);
		btn_add.setObjectName("btn_add");
		btn_add.setGeometry(new QRect(250, 350, 101, 30));
		btn_add.setStyleSheet(theme);
		btn_add.setText("Add");
		btn_done = new QPushButton(Dialog);
		btn_done.setObjectName("btn_donw");
		btn_done.setGeometry(new QRect(250, 390, 101, 30));
		btn_done.setStyleSheet(theme);
		btn_done.setText("Done");
		progname = new QTextEdit(Dialog);
		progname.setObjectName("progname");
		progname.setGeometry(new QRect(40, 350, 91, 30));
		progname.setStyleSheet(theme);
		progexec = new QTextEdit(Dialog);
		progexec.setObjectName("progexec");
		progexec.setGeometry(new QRect(140, 350, 101, 30));
		progexec.setStyleSheet(theme);
		table = new QTableWidget(Dialog);
		table.setGeometry(new QRect(10, 10, 341, 331));
		table.setColumnCount(2);
		table.setColumnWidth(0, 160);
		table.setColumnWidth(1, 160);
		table.setRowCount(100);
		//retranslateUi(Dialog);

	} // setupUi

	void retranslateUi(QDialog Dialog) {
		Dialog.setWindowTitle(QCoreApplication.translate("Dialog", "Dialog", null));
		
	} // retranslateUi
}
