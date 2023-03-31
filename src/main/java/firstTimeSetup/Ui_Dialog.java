package firstTimeSetup;

import io.qt.core.QCoreApplication;
import io.qt.core.QRect;
import io.qt.core.Qt;
import io.qt.core.QSize;
import io.qt.widgets.QCheckBox;
import io.qt.widgets.QDialog;
import io.qt.widgets.QLabel;
import io.qt.widgets.QLineEdit;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QSlider;

public class Ui_Dialog {

    public QPushButton pushButton;
    public QCheckBox MP_en;
    public QLineEdit MP_Bus;
    public QLabel label;
    public QLabel label_2;
    public QLineEdit MP_objpath;
    public QCheckBox OV_en;
    public QCheckBox SYS_en;
    public QLabel label_3;
    public QLineEdit SYS_temp;
    public QCheckBox SYS_smi;
    public QCheckBox DC_en;
    public QCheckBox checkBox_6;
    public QSlider MIC_rms;
    public QLabel label_4;
    public QLineEdit OV_pos;
    public QLabel label_5;
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

    public Ui_Dialog() { super(); }

    public void setupUi(QDialog Dialog) {
		Dialog.setObjectName("Setup");
		Dialog.resize(new QSize(443, 500).expandedTo(Dialog.minimumSizeHint()));
		Dialog.setStyleSheet(theme);
		pushButton = new QPushButton(Dialog);
		pushButton.setObjectName("pushButton");
		pushButton.setGeometry(new QRect(340, 440, 101, 36));
		pushButton.setStyleSheet(theme);
		MP_en = new QCheckBox(Dialog);
		MP_en.setObjectName("MP_en");
		MP_en.setEnabled(true);
		MP_en.setGeometry(new QRect(10, 10, 131, 24));
		MP_en.setChecked(true);
		MP_en.setStyleSheet(theme);
		MP_Bus = new QLineEdit(Dialog);
		MP_Bus.setObjectName("MP_Bus");
		MP_Bus.setGeometry(new QRect(10, 60, 301, 34));
		MP_Bus.setStyleSheet(theme);
		label = new QLabel(Dialog);
		label.setObjectName("label");
		label.setGeometry(new QRect(10, 40, 131, 20));
		label.setStyleSheet(theme);
		label_2 = new QLabel(Dialog);
		label_2.setObjectName("label_2");
		label_2.setGeometry(new QRect(10, 100, 91, 20));
		MP_objpath = new QLineEdit(Dialog);
		MP_objpath.setObjectName("MP_objpath");
		MP_objpath.setGeometry(new QRect(10, 120, 301, 34));
		/*
		OV_en = new QCheckBox(Dialog);
		OV_en.setObjectName("OV_en");
		OV_en.setGeometry(new QRect(10, 300, 141, 24));
		OV_en.setChecked(true);
		OV_en.setDisabled(true);
		*/
		SYS_en = new QCheckBox(Dialog);
		SYS_en.setObjectName("SYS_en");
		SYS_en.setGeometry(new QRect(10, 170, 161, 24));
		SYS_en.setCheckable(true);
		SYS_en.setChecked(true);
		label_3 = new QLabel(Dialog);
		label_3.setObjectName("label_3");
		label_3.setGeometry(new QRect(10, 200, 101, 20));
		SYS_temp = new QLineEdit(Dialog);
		SYS_temp.setObjectName("SYS_temp");
		SYS_temp.setGeometry(new QRect(10, 220, 301, 34));
		label_5 = new QLabel(Dialog);
		label_5.setObjectName("label_5");
		label_5.setGeometry(new QRect(10, 260, 101, 20));
		OV_pos = new QLineEdit(Dialog);
		OV_pos.setObjectName("OV_pos");
		OV_pos.setGeometry(new QRect(10, 280, 301, 34));
		SYS_smi = new QCheckBox(Dialog);
		SYS_smi.setObjectName("SYS_smi");
		SYS_smi.setGeometry(new QRect(10, 310, 161, 24));
		DC_en = new QCheckBox(Dialog);
		DC_en.setObjectName("DC_en");
		DC_en.setGeometry(new QRect(10, 350, 171, 24));
		DC_en.setChecked(true);
		checkBox_6 = new QCheckBox(Dialog);
		checkBox_6.setObjectName("checkBox_6");
		checkBox_6.setEnabled(false);
		checkBox_6.setGeometry(new QRect(10, 370, 171, 24));
		checkBox_6.setCheckable(true);
		checkBox_6.setChecked(true);
		MIC_rms = new QSlider(Dialog);
		MIC_rms.setObjectName("MIC_rms");
		MIC_rms.setGeometry(new QRect(10, 440, 311, 20));
		MIC_rms.setValue(20);
		MIC_rms.setOrientation(Qt.Orientation.Horizontal);
		label_4 = new QLabel(Dialog);
		label_4.setObjectName("label_4");
		label_4.setGeometry(new QRect(110, 420, 131, 20));
		retranslateUi(Dialog);

	} // setupUi

	void retranslateUi(QDialog Dialog) {
		Dialog.setWindowTitle(QCoreApplication.translate("Dialog", "Dialog", null));
		pushButton.setText(QCoreApplication.translate("Dialog", "Save and Exit", null));
		MP_en.setText(QCoreApplication.translate("Dialog", "MPRIS enabled", null));
		MP_Bus.setText(QCoreApplication.translate("Dialog", "org.mpris.MediaPlayer2.plasma-browser-integration", null));
		label.setText(QCoreApplication.translate("Dialog", "Busname:", null));
		label_2.setText(QCoreApplication.translate("Dialog", "Obejectpath:", null));
		MP_objpath.setText(QCoreApplication.translate("Dialog", "/org/mpris/MediaPlayer2", null));
		//OV_en.setText(QCoreApplication.translate("Dialog", "PLACEHOLDER", null));
		SYS_en.setText(QCoreApplication.translate("Dialog", "Overlay enabled", null));
		label_3.setText(QCoreApplication.translate("Dialog", "CPU temp file:", null));
		SYS_temp.setText(QCoreApplication.translate("Dialog", "/sys/class/thermal/thermal_zone0/temp", null));
		SYS_smi.setText(QCoreApplication.translate("Dialog", "NVidia smi enabled", null));
		DC_en.setText(QCoreApplication.translate("Dialog", "Discord-RPC enabled", null));
		checkBox_6.setText(QCoreApplication.translate("Dialog", "Use local recognition", null));
		label_4.setText(QCoreApplication.translate("Dialog", "Mic RMS threshold:", null));
		label_5.setText(QCoreApplication.translate("Dialog", "Overlay postion:", null));
		OV_pos.setText(QCoreApplication.translate("Dialog", "0,0",null));
	} // retranslateUi
}
