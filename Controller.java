package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Enemie;
import universite_paris8.iut.fabdelrahim.sae.vue.TerrainVue;
import universite_paris8.iut.fabdelrahim.sae.vue.EntiteVue;
import universite_paris8.iut.fabdelrahim.sae.vue.GestionImage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TilePane map;
    @FXML
    private Pane panneauJeu;
    @FXML
    public Label labelArgent;

    private TerrainVue terrainVue;
    private EntiteVue entiteVue;
    private Timeline gameLoop;
    private Environnement env;
    private int argent = 200;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Chargement des images
        GestionImage.loadAssets();

        // Initialisation du Terrain
        terrainVue = new TerrainVue();
        terrainVue.map = map;
        terrainVue.creerTerrain();

        //Initialisation des entités et de la boucle de jeu
        initJeu();
    }

    private void initJeu() {
        this.env = new Environnement();
        this.entiteVue = new EntiteVue(panneauJeu);

        // On injecte les zombies du modèle dans la vue des entités
        for (Enemie z : env.getZombies()) {
            entiteVue.afficherEnnemie(z);
        }

        initAnimation();
        metAjour();
    }

    private void initAnimation() {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                ev -> {

                    env.unTourDeJeu();

                    // Vérification des récompenses de mort
                    for (Enemie z : env.getZombies()) {
                        if (z.prendreRecompense()) {
                            this.argent += 50;
                            metAjour();
                        }
                    }

                    // Rafraîche les positions des images sur l'écran
                    entiteVue.mettreAJourAffichage();
                }
        );
        gameLoop.getKeyFrames().add(kf);
    }

    public void metAjour() {
        labelArgent.setText("Ticket Resto : " + argent);
    }

    @FXML
    public void lancerJeu(ActionEvent event) {
        if (gameLoop != null && gameLoop.getStatus() != Timeline.Status.RUNNING) {
            gameLoop.play();
            System.out.println("Lancement du jeu");
        }
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        entiteVue.nettoyer(); // Retire les anciennes images 
        initJeu();            // Recrée l'environnement tout neuf
        gameLoop.play();
        System.out.println("Jeu recommencé");
    }

    @FXML
    public void arreterJeu(ActionEvent event) {
        if (gameLoop != null) {
            gameLoop.pause();
            System.out.println("Jeu arrêté");
        }
    }

    @FXML
    public void gestionargent(ActionEvent event) {
        if (this.argent >= 100) {
            this.argent -= 100;
            metAjour();
            System.out.println("Achat effectué, reste : " + this.argent);
        } else {
            System.out.println("Pas assez de Tickets Resto !");
        }
    }

    @FXML
    public void reglage(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource("/universite_paris8/iut/fabdelrahim/sae/reglage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
