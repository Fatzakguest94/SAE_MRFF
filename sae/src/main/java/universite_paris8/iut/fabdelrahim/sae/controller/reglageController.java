package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import java.io.IOException;

public class reglageController {

    @FXML
    public void quitte(ActionEvent event) throws IOException {
        GestionSon.getInstance().reprendre();
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