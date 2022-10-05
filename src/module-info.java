module NTP_Proje {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;
	requires javafx.graphics;
	requires java.sql;
	requires javafx.base;
	requires javafx.media;
	requires java.security.jgss;
	requires java.xml;
	requires jdk.xml.dom;
	requires java.xml.crypto;
	
	opens application to javafx.base, javafx.media ,javafx.graphics, javafx.fxml;
}
