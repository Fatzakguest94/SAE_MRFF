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
    private Label labelArgent;
    @FXML
    private Label welcomeText;

    private TerrainVue terrainVue;
    private EntiteVue entiteVue;
    private Timeline gameLoop;
    private Environnement env;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Chargement des assets graphiques
        GestionImage.loadAssets();

        // Création et affichage du décor (la map de tuiles)
        this.terrainVue = new TerrainVue();
        this.terrainVue.map = map;
        this.terrainVue.creerTerrain();

        // Lancement initial de la configuration du jeu
        this.initJeu();
    }

    private void initJeu() {
        this.env = new Environnement();
        this.entiteVue = new EntiteVue(panneauJeu);

        // Affichage du comptoir au début
        if (this.env.getBase() != null) {
            this.entiteVue.afficherComptoir(this.env.getBase());
        }

        this.initAnimation();
        this.metAjourInterface();
    }

    private void initAnimation() {
        this.gameLoop = new Timeline();
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017), // Correspond à environ 60 rafraîchissements par seconde
                ev -> {
                    // 1. Met à jour les calculs dans la logique (Modèle)
                    this.env.unTourDeJeu();

                    // 2. Met à jour les positions des images existantes (Vue)
                    this.entiteVue.mettreAJourAffichage();

                    // 3. Demande à la vue d'afficher les nouveaux zombies s'il y en a
                    for (int i = 0; i < this.env.getZombies().size(); i++) {
                        Enemie z = this.env.getZombies().get(i);
                        this.entiteVue.afficherEnnemie(z);
                    }

                    // 4. Met à jour les textes affichés à l'écran
                    this.metAjourInterface();
                }
        );
        this.gameLoop.getKeyFrames().add(kf);
    }

    public void metAjourInterface() {
        if (this.labelArgent != null) {
            this.labelArgent.setText("Ticket Resto : " + this.env.getArgent());
        }

        if (this.welcomeText != null) {
            if (this.env.getBase() != null && this.env.getBase().estDetruit()) {
                this.welcomeText.setText("GAME OVER !");
                this.gameLoop.stop();
            } else {
                this.welcomeText.setText("Vague : " + this.env.getNumeroVague());
            }
        }
    }

    @FXML
    public void lancerJeu(ActionEvent event) {
        if (this.gameLoop != null && this.gameLoop.getStatus() != Timeline.Status.RUNNING) {
            // Si on est avant la première vague, on la génère
            if (this.env.getNumeroVague() == 0) {
                this.env.preparerNouvelleVague();
            }
            this.gameLoop.play();
            System.out.println("Jeu lancé");
        }
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {
        if (this.gameLoop != null) {
            this.gameLoop.stop();
        }
        this.entiteVue.nettoyer(); // On efface tout l'ancien visuel
        this.initJeu();            // On remet un modèle tout neuf
        this.env.preparerNouvelleVague();
        this.gameLoop.play();
        System.out.println("Jeu recommencé");
    }

    @FXML
    public void arreterJeu(ActionEvent event) {
        if (this.gameLoop != null) {
            this.gameLoop.pause();
            System.out.println("Jeu mis en pause");
        }
    }

    @FXML
    public void gestionargent(ActionEvent event) {
        // Exemple simple : on achète une action/un objet pour 100 tickets resto
        if (this.env.payerAchat(100)) {
            this.metAjourInterface();
            System.out.println("Achat réussi");
        } else {
            System.out.println("Fonds insuffisants !");
        }
    }

    @FXML
    public void reglage(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource("/universite_paris8/iut/rissamou/sae_td/reglage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
