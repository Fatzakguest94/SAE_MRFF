package universite_paris8.iut.fzekraoui.saedev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("/universite_paris8/iut/fzekraoui/saedev/vue.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("/vue/towerDefense.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			/*Button button = new Button("click me!");
			TextField TF = new TextField();
			button.setOnAction(e -> TF.appendText("click !!!!"));
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
