package universite_paris8.iut.fabdelrahim.sae;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class pizzattackapplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                pizzattackapplication.class.getResource("ecran.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 1377, 856);
        stage.setTitle("pizzattack!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}