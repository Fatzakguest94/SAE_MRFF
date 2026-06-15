package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.Tour;
import universite_paris8.iut.fabdelrahim.sae.vue.TerrainVue;
import universite_paris8.iut.fabdelrahim.sae.vue.EntiteVue;
import universite_paris8.iut.fabdelrahim.sae.vue.GestionImage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TilePane map;
    @FXML private Pane panneauJeu;
    @FXML private Label labelArgent;
    @FXML private Label labelVague;
    @FXML private ProgressBar pv;

    private TerrainVue terrainVue;
    private EntiteVue entiteVue;
    private Timeline gameLoop;
    private Environnement env;
    private String tourAcheteeEnCours = null; // Stocke l'action actuelle ("VENDRE", "AMELIORER" ou le type de tour)

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (map == null) return; // Sécurité FXML

        this.env = new Environnement();
        GestionImage.loadAssets();

        // Rendu de la carte
        this.terrainVue = new TerrainVue(env.getTerrain(), map);
        this.terrainVue.creerTerrain();

        // Taille fixe du panneau pour bloquer les décalages de tuiles
        this.panneauJeu.setPrefWidth(1152);
        this.panneauJeu.setPrefHeight(756);

        this.initJeu();
    }

    // Centralisation de la configuration de la partie
    private void initJeu() {
        this.entiteVue = new EntiteVue(panneauJeu, env);

        // Affichage du comptoir de départ
        if (this.env.getBase() != null) {
            this.entiteVue.afficherComptoir(this.env.getBase());
        }

        // Bindings
        if (this.labelArgent != null) {
            this.labelArgent.textProperty().bind(env.argentProperty().asString(" : %d"));
        }
        if (this.labelVague != null) {
            this.labelVague.textProperty().bind(env.numeroVagueProperty().asString("Vague : %d"));
        }
        if (this.env.getBase() != null && this.pv != null){
            this.pv.progressProperty().bind(this.env.getBase().hpProperty().divide(100.0));
        }

        this.initAnimation();
    }

    // Boucle principale du jeu (Game Loop)
    private void initAnimation() {
        this.gameLoop = new Timeline();
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017), // Environ 60 FPS
                ev -> {
                    this.env.unTourDeJeu();
                    this.entiteVue.mettreAJourAffichage(this.env);

                    // Condition de défaite
                    if (this.env.getBase() != null && this.env.getBase().estDetruit()) {
                        this.gameLoop.stop();
                        this.labelVague.textProperty().unbind();
                        this.labelVague.setText("GAME OVER !");
                    }
                }
        );
        this.gameLoop.getKeyFrames().add(kf);
    }

    @FXML
    public void lancerJeu(ActionEvent event) {
        if (this.gameLoop != null && this.gameLoop.getStatus() != Timeline.Status.RUNNING) {
            if (this.env.getNumeroVague() == 0) {
                this.env.preparerNouvelleVague();
            }
            this.gameLoop.play();
        }
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {
        if (this.gameLoop != null) {
            this.gameLoop.stop();
        }
    }

    @FXML
    public void arreterJeu(ActionEvent event) {
        if (this.gameLoop != null) {
            this.gameLoop.pause();
        }
    }

    @FXML
    public void reglage(ActionEvent event) throws IOException {
        if (gameLoop != null) gameLoop.stop();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/fabdelrahim/sae/reglage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void aide(ActionEvent event) {
        try {
            Button boutonClique = (Button) event.getSource();

            // Fermeture de la fenêtre d'aide
            if (boutonClique.getId() != null && boutonClique.getId().equals("nextBtn")) {
                Stage stageAide = (Stage) boutonClique.getScene().getWindow();
                if (stageAide != null) {
                    stageAide.close();
                }
                this.lancerJeu(null);
            }
            // Ouverture de la fenêtre d'aide
            else {
                if (gameLoop != null) gameLoop.pause();

                Stage stage = new Stage();
                stage.setTitle("Pizzattack - L'Histoire & Règles");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/fabdelrahim/sae/aide.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            System.err.println("Erreur de chargement de aide.fxml");
            e.printStackTrace();
        }
    }

    // Gestion de la boutique
    @FXML
    public void clicBoutonMitrailletteFrite(ActionEvent event) {
        this.tourAcheteeEnCours = "MitrailletteFrite";
    }

    @FXML
    public void clicBoutonLanceBurger(ActionEvent event) {
        this.tourAcheteeEnCours = "LanceBurger";
    }

    @FXML
    public void clicBoutonBacGlace(ActionEvent event) {
        this.tourAcheteeEnCours = "BacGlace";
    }

    @FXML
    public void clicBoutonBarbecue(ActionEvent event) {
        this.tourAcheteeEnCours = "Barbecue";
    }

    @FXML
    public void clicSurTerrain(MouseEvent event) {
        if (this.tourAcheteeEnCours == null) return;

        int xAjuste = ((int) event.getX() / 36) * 36;
        int yAjuste = ((int) event.getY() / 36) * 36;

        // Si on est en mode vente
        if (this.tourAcheteeEnCours.equals("VENDRE")) {
            this.env.vendreTour(xAjuste, yAjuste);
        }
        // MODIFICATION : Si on est en mode amélioration
        else if (this.tourAcheteeEnCours.equals("AMELIORER")) {
            this.env.ameliorerTour(xAjuste, yAjuste);
        }
        // Sinon on pose une tour classique
        else {
            this.env.ajouterTour(xAjuste, yAjuste, this.tourAcheteeEnCours);
        }

        this.tourAcheteeEnCours = null; // Réinitialise l'état après l'action
    }

    @FXML
    public void vendreT(ActionEvent event) {
        this.tourAcheteeEnCours = "VENDRE";
        System.out.println("Mode vente active : cliquez sur une tour pour la revendre.");
    }

    @FXML
    public void ameliorerT(ActionEvent event) { // Renommé proprement pour correspondre au bouton
        this.tourAcheteeEnCours = "AMELIORER";
        System.out.println("Mode amelioration active : cliquez sur une tour pour augmenter ses stats.");
    }
}