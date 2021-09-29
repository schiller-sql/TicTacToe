module com.desktop.app.desktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens application to javafx.fxml;
    opens controller.scene to javafx.fxml;
    exports application;
    exports controller.scene;
}