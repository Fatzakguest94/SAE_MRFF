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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Tour;
import universite_paris8.iut.fabdelrahim.sae.pizzattackapplication;
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

    private TerrainVue terrainVue;
    private EntiteVue entiteVue;
    private Timeline gameLoop;
    private Environnement env;
    private boolean achatTour = false;

    @FXML
    private Button playBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.env = new Environnement();
        GestionImage.loadAssets();

        this.terrainVue = new TerrainVue(env.getTerrain(), map);
        this.terrainVue.creerTerrain();

        //DATA BINDING
        if (this.labelArgent != null) {
            this.labelArgent.textProperty().bind(env.argentProperty().asString("Ticket Resto : %d"));
        }
        if (this.labelVague != null) {
            this.labelVague.textProperty().bind(env.numeroVagueProperty().asString("Vague : %d"));
        }

        this.initJeu();
    }

    private void initJeu() {
        // On passe 'env' pour que la vue puisse attacher son écouteur de liste
        this.entiteVue = new EntiteVue(panneauJeu, env);

        if (this.env.getBase() != null) {
            this.entiteVue.afficherComptoir(this.env.getBase());
        }

        this.initAnimation();
    }

    private void initAnimation() {
        this.gameLoop = new Timeline();
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                ev -> {
                    this.env.unTourDeJeu();

                    // MODIFICATION ICI : On transmet 'env' à la méthode
                    this.entiteVue.mettreAJourAffichage(this.env);

                    for (Tour t : this.env.getTours()) {
                        this.entiteVue.afficherTour(t);
                    }

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
            System.out.println("Jeu lancé");
        }
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {
        if (this.gameLoop != null) {
            this.gameLoop.stop();
        }

        this.entiteVue.viderTout(); // On efface le visuel

        // On réinitialise un tout nouvel environnement propre et on recrée les Binds
        this.env = new Environnement();
        this.labelArgent.textProperty().bind(env.argentProperty().asString("Ticket Resto : %d"));
        this.labelVague.textProperty().bind(env.numeroVagueProperty().asString("Vague : %d"));

        this.initJeu();
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
        this.achatTour = true;
        System.out.println("Mode placement activé ! Cliquez sur le terrain.");
    }

    @FXML
    public void clicSurTerrain(MouseEvent event) {
        if (!this.achatTour) return;

        int xAjuste = ((int) event.getX() / 36) * 36;
        int yAjuste = ((int) event.getY() / 36) * 36;

        this.env.ajouterTour(xAjuste, yAjuste);
        this.achatTour = false; // Désactive le mode placement après la pose
    }

    public void reglage(ActionEvent event) throws IOException {
        if (gameLoop != null) gameLoop.stop();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource("/universite_paris8/iut/fabdelrahim/sae/reglage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void reprendre(ActionEvent event) throws IOException {
        if (gameLoop != null) gameLoop.play();
    }

    public void aide(ActionEvent event) throws IOException {
        if (gameLoop != null) gameLoop.stop();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource("/universite_paris8/iut/fabdelrahim/sae/aide.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}