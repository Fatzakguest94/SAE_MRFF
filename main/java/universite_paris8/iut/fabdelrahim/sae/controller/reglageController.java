package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import java.io.IOException;

public class reglageController {

    @FXML
    private Slider volume;

    @FXML
    public void initialize() {

        double vol = GestionSon.getInstance().getVolumeActuel();
        volume.setValue(vol);


        volume.valueProperty().addListener((obs, oldVal, newVal) -> {
            GestionSon.getInstance().setVolume(newVal.doubleValue());
        });
    }

    @FXML
    public void quitte(ActionEvent event) throws IOException {
        GestionSon.getInstance().reprendre();

        if (GestionJeu.getInstance().getController() != null) {
            GestionJeu.getInstance().getController().lancerJeu(null);
        }

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {
        GestionSon.getInstance().reprendre();
        GestionJeu.getInstance().getController().recommencerJeu(null);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}