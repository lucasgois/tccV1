module com.github.lucasgois.tcc.deploy {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.github.lucasgois.tcc.deploy to javafx.fxml;
    exports com.github.lucasgois.tcc.deploy;
}