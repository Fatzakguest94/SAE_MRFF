package universite_paris8.iut.fabdelrahim.sae;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class pizzattackapplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(pizzattackapplication.class.getResource("vue0.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1377, 856);
        stage.setTitle("pizzattack!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}	package universite_paris8.iut.fzekraoui.saedev;

	import javafx.application.Application;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.TextField;
	import javafx.scene.layout.BorderPane;
	import javafx.stage.Stage;
	import universite_paris8.iut.fzekraoui.saedev.controller.Controller;

	public class MainApp extends Application {

		@Override
		public void start(Stage primaryStage) {
			try {
				BorderPane root = FXMLLoader.load(getClass().getResource("/universite_paris8/iut/fzekraoui/saedev/vue.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/universite_paris8/iut/fzekraoui/saedev/Style.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();




			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static void main(String[] args) {
			launch(args);
		}
	}
