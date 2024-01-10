module com.librarymanagmentsystem.librarymanagmentsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.librarymanagmentsystem.librarymanagmentsystem to javafx.fxml;
    exports com.librarymanagmentsystem.librarymanagmentsystem;
}