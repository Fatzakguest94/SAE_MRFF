package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import universite_paris8.iut.fabdelrahim.sae.pizzattackapplication;
import java.io.IOException;

public class ecranController {

    @FXML
    private Button playBtn;

    @FXML
    private void handlePlay() {
        try {
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
    public void quitte (ActionEvent event ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource("/universite_paris8/iut/fabdelrahim/sae/ecran.fxml"));

    }


}