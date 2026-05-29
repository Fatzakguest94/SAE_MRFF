package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;
import universite_paris8.iut.fabdelrahim.sae.vue.TerrainVue;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
   
    @FXML
    private TilePane map;
    @FXML
    private Pane panneauJeu;

    // Les 4 vues des zombies
    @FXML
    private ImageView zombie;
    @FXML
    private ImageView zombie1;
    @FXML
    private ImageView zombie2;
    @FXML
    private ImageView zombie3;

    private TerrainVue terrainVue;
    private Timeline gameLoop;


    private Environnement env;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation de l'environnement 
        this.env = new Environnement();


        zombie.setVisible(false);
        zombie1.setVisible(false);
        zombie2.setVisible(false);
        zombie3.setVisible(false);


        Image zombienormal = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/zombie.png"));
        Image zombiegros = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/zombiegros.png"));
        Image zombierapide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/zombierapide.png"));
        Image zombiefamille = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/zombiefamille.png"));

        zombie.setImage(zombienormal);
        zombie1.setImage(zombiegros);
        zombie2.setImage(zombierapide);
        zombie3.setImage(zombiefamille);


        terrainVue = new TerrainVue();
        terrainVue.map = map;
        terrainVue.creerTerrain();

        System.out.println("Grille: " + env.getTerrain().grille.length + " x " + env.getTerrain().grille[0].length);

        if (env.getChemin().isEmpty()) {
            System.out.println("Aucun chemin trouvé !");
            return;
        }

        //Initialisation animation
        initAnimation();
    }

    @FXML
    public void reactionBouton(ActionEvent event) {
        zombie.setVisible(true);
        zombie1.setVisible(true);
        zombie2.setVisible(true);
        zombie3.setVisible(true);
        gameLoop.play();
        System.out.println("lancement du parcours");
    }

    private void initAnimation() {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                ev -> {
                    // On fait progresser le temps dans le modèle
                    env.unTourDeJeu();
                    int tempsActuel = env.getTemps();

                    // Synchronisation de la vue avec le modèle selon les modulos
                    if (tempsActuel % 35 == 0) {
                        System.out.println("un tour");
                        env.getZombies().get(0).update(zombie, 36);
                    }

                    if (tempsActuel % 50 == 0) {
                        env.getZombies().get(1).update(zombie1, 36);
                    }

                    if (tempsActuel % 25 == 0) {
                        env.getZombies().get(2).update(zombie2, 36);
                    }

                    if (tempsActuel % 45 == 0) {
                        env.getZombies().get(3).update(zombie3, 36);
                    }
                }
        );

        gameLoop.getKeyFrames().add(kf);
    }
}
