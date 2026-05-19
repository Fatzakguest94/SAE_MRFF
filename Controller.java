package universite_paris8.iut.fabdelrahim.sae;

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

public class Controller implements Initializable {
    @FXML
    private Label welcomeText;

    private Terrain terrain = new Terrain();

    public TilePane map;

    private Timeline gameLoop;
    @FXML
    private ImageView zombie;
    @FXML
    private ImageView zombie1;
    private int temps;
    @FXML
    private Pane panneaujeu;

    @FXML
    private void creerTerrain() {
        int[][] grille = terrain.grille;

        int tailleTuile = 36;
        map.setPrefColumns(31);
        map.setPrefRows(19);

        map.setPrefTileWidth(tailleTuile);
        map.setPrefTileHeight(tailleTuile);

        Image solnoir = new Image(getClass().getResourceAsStream("solblanc.png"));
        Image sol = new Image(getClass().getResourceAsStream("tapisrose.png"));
        Image entrer = new Image(getClass().getResourceAsStream("entrer.jpg"));
        Image sortie = new Image(getClass().getResourceAsStream("door.png"));
        Image barrage5 = new Image(getClass().getResourceAsStream("barrage5.png"));
        Image barrage6 = new Image(getClass().getResourceAsStream("barrage6.png"));
        Image panneau8 = new Image(getClass().getResourceAsStream("panneau8.png"));

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                ImageView cases = new ImageView();
                cases.setFitHeight(tailleTuile);
                cases.setFitWidth(tailleTuile);
                int t = grille[i][j];
                switch (t) {
                    case 0:
                        cases.setImage(solnoir);
                        break;
                    case 1:
                        cases.setImage(sol);
                        break;
                    case 2:
                        cases.setImage(solnoir);
                        break;

                    case 3:
                        cases.setImage(entrer);
                        break;

                    case 4:
                        cases.setImage(sortie);
                        break;

                    case 5:
                        cases.setImage(barrage5);
                        break;

                    case 6:
                        cases.setImage(barrage6);
                        break;

                    case 8:
                        cases.setImage(panneau8);
                        break;

                    default:
                        cases.setImage(solnoir);
                        break;
                }
                map.getChildren().add(cases);
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        terrain = new Terrain();
        Image img = new Image(getClass().getResourceAsStream("zombie.jpg"));
        zombie.setImage(img);
        zombie1.setImage(img);


        //map.setPrefColumns(21);

        System.out.println(terrain.grille.length);
        System.out.println(terrain.grille[0].length);
        creerTerrain();
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
