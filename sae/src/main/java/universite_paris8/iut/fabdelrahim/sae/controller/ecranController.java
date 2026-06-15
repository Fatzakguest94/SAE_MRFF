package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import universite_paris8.iut.fabdelrahim.sae.pizzattackapplication;
import java.io.IOException;

public class ecranController {

    @FXML
    private Button playBtn;



    @FXML
    private void jouer() {
        try {
            GestionSon.getInstance().demarrer("/universite_paris8/iut/fabdelrahim/sae/musique/Pizzayolo.wav");

            FXMLLoader loader = new FXMLLoader(
                    pizzattackapplication.class.getResource("vue0.fxml")
            );
            Scene gameScene = new Scene(loader.load(), 1377, 856);
            Stage stage = (Stage) playBtn.getScene().getWindow();
            stage.setScene(gameScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}