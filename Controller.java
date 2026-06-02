package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombie;
import universite_paris8.iut.fabdelrahim.sae.vue.TerrainVue;

import javafx.scene.control.Label;
import java.io.IOException;
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

    @FXML
    public Label labelArgent;

    private int argent = 200;

    private void metAjour() {
        labelArgent.setText(String.valueOf(this.argent));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
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
        metAjour();
    }

    @FXML
    public void lancerJeu(ActionEvent event) {
        zombie.setVisible(true);
        zombie1.setVisible(true);
        zombie2.setVisible(true);
        zombie3.setVisible(true);

        if (gameLoop.getStatus() != Timeline.Status.RUNNING) {
            gameLoop.play();
        }

        System.out.println("Lancement du jeu");
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {

        //arrete l'animation
        if (gameLoop != null) {
            gameLoop.stop();
        }

        //Reset modèle
        this.env = new Environnement();

        // 3. Reset visibilité zombies
        zombie.setVisible(true);
        zombie1.setVisible(true);
        zombie2.setVisible(true);
        zombie3.setVisible(true);

        // 4. Recréer animation proprement
        initAnimation();

        // 5. Relancer
        gameLoop.play();

        System.out.println("Jeu recommencé");
    }

    @FXML
    public void arreterJeu(ActionEvent event) {
        if (gameLoop != null) {
            gameLoop.pause();
        }

        System.out.println("Jeu arrêté");
    }

    // +50 quand un zombie meurt (une seule fois par zombie)
    public void argenT(Zombie zombie) {
        if (zombie.prendreRecompense()) {
            this.argent += 50;
            metAjour();
            System.out.println("Argent: " + this.argent);
        }
    }

    // Acheter une tour : à appeler depuis gestionargent()
    public void gestionargent(ActionEvent event) {

        if (this.argent >= 100) {
            this.argent = this.argent - 100;
            metAjour();
            System.out.println("acheter" + this.argent);
        } else {
            System.out.println("manque d'argent");
        }
    }

    public void reglage(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource("/universite_paris8/iut/fabdelrahim/sae/reglage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
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
