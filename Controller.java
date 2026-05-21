package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import universite_paris8.iut.fabdelrahim.sae.modele.Terrain;
import universite_paris8.iut.fabdelrahim.sae.vue.TerrainVue;

public class Controller  implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private TilePane map;

    private universite_paris8.iut.fabdelrahim.sae.vue.TerrainVue TerrainVue;

    private Timeline gameLoop;
    @FXML
    private ImageView zombie;
    @FXML
    private ImageView zombie1;
    private int temps;
    @FXML
    private Pane panneauJeu;

    private Terrain terrain;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Image img = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/zombie.jpg"));
        zombie.setImage(img);
        zombie1.setImage(img);

        TerrainVue = new TerrainVue();
        TerrainVue.map = map;
        // création du terrain
        TerrainVue.creerTerrain();
        terrain= new Terrain();

        System.out.println(terrain.grille.length);
        System.out.println(terrain.grille[0].length);


        initAnimation();
    }

    @FXML
    public void reactionBouton(ActionEvent event) {
        gameLoop.play();
        System.out.println("lancement du parcours");
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
                        zombie.setLayoutX(zombie.getLayoutX()+1);
                        zombie.setLayoutY(zombie.getLayoutY()+1);

                        zombie1.setLayoutX(zombie1.getLayoutX()+1);
                        zombie1.setLayoutY(zombie1.getLayoutY()+1);



                    }
                    temps++;
                })
        );
        gameLoop.getKeyFrames().add(kf);
    }


}
