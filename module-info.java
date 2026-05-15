module universite_paris8.iut.fzekraoui.saedev {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.desktop;

    opens universite_paris8.iut.fzekraoui.saedev to javafx.fxml;
    exports universite_paris8.iut.fzekraoui.saedev;
    exports universite_paris8.iut.fzekraoui.saedev.controller;
    opens universite_paris8.iut.fzekraoui.saedev.controller to javafx.fxml;
}