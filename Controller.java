package universite_paris8.iut.fzekraoui.saedev.controller;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Controller implements Initializable {
	// permet de definir l'animation
	private Timeline gameLoop;
	
	private int temps;

	
    @FXML
    private Circle leCercle;

	@FXML
	private Circle leCercle1;

	@FXML 
	void reactionBouton(javafx.event.ActionEvent  event) {

		System.out.println("lancement du parcours");
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initAnimation();
		// demarre l'animation
		gameLoop.play();
		
	}


	private void initAnimation() {
		gameLoop = new Timeline();
		temps=0;
		gameLoop.setCycleCount(Timeline.INDEFINITE);

		KeyFrame kf = new KeyFrame(
				// on définit le FPS (nbre de frame par seconde)
				Duration.seconds(0.017),
				// on définit ce qui se passe à chaque frame 
				// c'est un eventHandler d'ou le lambda
				(ev ->{
					if(temps==100){
					System.out.println("fini");
					gameLoop.stop();
					}
					else if (temps%5==0){
						System.out.println("un tour");
						leCercle.setLayoutX(leCercle.getLayoutX()+5);
						leCercle.setLayoutY(leCercle.getLayoutY()+5);

						leCercle1.setLayoutX(leCercle1.getLayoutX()+20);
						leCercle1.setLayoutY(leCercle1.getLayoutY()+5);


					}
					temps++;
				})
				);
		gameLoop.getKeyFrames().add(kf);
	}
}
