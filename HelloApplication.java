package universite_paris8.iut.fzekraoui.saedev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
