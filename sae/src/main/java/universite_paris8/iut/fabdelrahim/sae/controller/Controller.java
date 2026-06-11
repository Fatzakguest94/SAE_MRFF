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
import universite_paris8.iut.fabdelrahim.sae.vue.TerrainVue;
import universite_paris8.iut.fabdelrahim.sae.vue.EntiteVue;
import universite_paris8.iut.fabdelrahim.sae.vue.GestionImage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Liens avec les composants du fichier FXML
    @FXML
    private TilePane map;
    @FXML
    private Pane panneauJeu;
    @FXML
    private Label labelArgent;
    @FXML
    private Label labelVague;
    @FXML
    private Label labelHpBase;


    // Variables pour la gestion des vues et du moteur de jeu
    private TerrainVue terrainVue;
    private EntiteVue entiteVue;
    private Timeline gameLoop;
    private Environnement env;
    private boolean achatTour = false;
    private String tourAcheteeEnCours = null; // Stocke la tour sélectionnée dans la boutique

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Sécurité pour éviter le plantage si le FXML n'est pas le principal
        if (map == null) {
            return;
        }

        // Initialisation du modèle et chargement des images du jeu
        this.env = new Environnement();
        GestionImage.loadAssets();

        // Création et affichage du terrain (les tuiles)
        this.terrainVue = new TerrainVue(env.getTerrain(), map);
        this.terrainVue.creerTerrain();

        this.panneauJeu.setPrefWidth(1152);   // 32 colonnes * 36 pixels
        this.panneauJeu.setPrefHeight(756);   // 21 lignes * 36 pixels

        // Data binding pour lier l'affichage aux variables du modèle (mise à jour automatique)
        if (this.labelArgent != null) {
            this.labelArgent.textProperty().bind(env.argentProperty().asString("Ticket Resto : %d"));
        }
        if (this.labelVague != null) {
            this.labelVague.textProperty().bind(env.numeroVagueProperty().asString("Vague : %d"));
        }

        // Lancement de la configuration des entités et de la boucle
        this.initJeu();
    }

    // Config de base des éléments dynamiques du plateau
    private void initJeu() {
        this.entiteVue = new EntiteVue(panneauJeu, env);

        // Affichage du comptoir/base s'il existe dans le modèle
        if (this.env.getBase() != null) {
            this.entiteVue.afficherComptoir(this.env.getBase());
        }

        // Liaison des points de vie de la base avec le label correspondant
        if (this.env.getBase() != null && this.labelHpBase != null) {
            this.labelHpBase.textProperty().bind(this.env.getBase().hpProperty().asString("Pizza : %d PV"));
        }

        // Création de la boucle de rafraîchissement
        this.initAnimation();
    }

    // Gestion du timer et des rafraîchissements (Game Loop)
    private void initAnimation() {
        this.gameLoop = new Timeline();
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);

        // Définition d'un tour de jeu toutes les ~16ms (environ 60 FPS)
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                ev -> {
                    // Calcul de la logique physique et déplacements dans le modèle
                    this.env.unTourDeJeu();

                    // Rafraîchissement visuel des zombies et projectiles
                    this.entiteVue.mettreAJourAffichage(this.env);

                    // Affichage des tours placées sur la carte
                    for (Tour t : this.env.getTours()) {
                        this.entiteVue.afficherTour(t);
                    }

                    // Condition de fin de partie si la base tombe à 0 PV
                    if (this.env.getBase() != null && this.env.getBase().estDetruit()) {
                        this.gameLoop.stop();
                        this.labelVague.textProperty().unbind(); // Désactivation du bind pour écrire le Game Over
                        this.labelVague.setText("GAME OVER !");
                    }
                }
        );
        this.gameLoop.getKeyFrames().add(kf);
    }

    // Action du bouton pour lancer ou reprendre la partie
    @FXML
    public void lancerJeu(ActionEvent event) {
        if (this.gameLoop != null && this.gameLoop.getStatus() != Timeline.Status.RUNNING) {
            // Si c'est le tout début, on génère les premiers monstres
            if (this.env.getNumeroVague() == 0) {
                this.env.preparerNouvelleVague();
            }
            this.gameLoop.play();
            System.out.println("Jeu lancé");
        }
    }

    // Remise à zéro complète du plateau et des données
    @FXML
    public void recommencerJeu(ActionEvent event) {
        if (this.gameLoop != null) {
            this.gameLoop.stop();
        }

        // Nettoyage complet du panneau de jeu (visuels des monstres/tours)
        this.entiteVue.viderTout();

        // Recréation d'un modèle tout neuf et réassignation des binds
        this.env = new Environnement();
        this.labelArgent.textProperty().bind(env.argentProperty().asString("Ticket Resto : %d"));
        this.labelVague.textProperty().bind(env.numeroVagueProperty().asString("Vague : %d"));

        // Relance de la structure du jeu
        this.initJeu();
        this.env.preparerNouvelleVague();
        this.gameLoop.play();
        System.out.println("Jeu recommencé");
    }

    // Met le jeu en pause
    @FXML
    public void arreterJeu(ActionEvent event) {
        if (this.gameLoop != null) {
            this.gameLoop.pause();
            System.out.println("Jeu mis en pause");
        }
    }

    // Active l'état d'achat pour placer un élément
    @FXML
    public void gestionargent(ActionEvent event) {
        this.achatTour = true;
        System.out.println("Mode placement activé ! Cliquez sur le terrain.");
    }

    // Ouvre la fenêtre des options / réglages
    @FXML
    public void reglage(ActionEvent event) throws IOException {
        if (gameLoop != null) gameLoop.stop();

        Stage stage = new Stage();
        // Utilisation de getClass() pour charger proprement le FXML localement
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/fabdelrahim/sae/reglage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void aide(ActionEvent event) {
        try {
            // Identification du composant graphique à l'origine de l'événement
            Button boutonClique = (Button) event.getSource();

            //Clic sur le bouton de validation de l'interface d'aide
            if (boutonClique.getId() != null && boutonClique.getId().equals("nextBtn")) {

                // Récupération de la fenêtre secondaire pour la fermer de manière isolée
                Stage stageAide = (Stage) boutonClique.getScene().getWindow();
                if (stageAide != null) {
                    stageAide.close();
                }

                // Relance de la boucle principale du jeu en réutilisant la méthode existante
                this.lancerJeu(null);
            }

            //Clic sur le bouton d'appel à l'aide depuis le plateau de jeu
            else {
                // Suspension temporaire du timer de jeu (game loop) pendant la lecture
                if (gameLoop != null) {
                    gameLoop.pause();
                }

                // Instanciation et configuration d'une nouvelle scène JavaFX
                Stage stage = new Stage();
                stage.setTitle("Pizzattack - L'Histoire & Règles");

                // Chargement du fichier de vue FXML dédié au guide du jeu
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/fabdelrahim/sae/aide.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                // Assignation et affichage de la nouvelle fenêtre à l'écran
                stage.setScene(scene);
                stage.show();
            }

        } catch (IOException e) {
            // Gestion de l'exception en cas de fichier FXML introuvable ou corrompu
            System.err.println("[Erreur] Impossible d'interagir avec aide.fxml");
            e.printStackTrace();
        }
    }

    //SELECTION DES TOURS DANS LA BOUTIQUE

    @FXML
    public void clicBoutonMitrailletteFrite(ActionEvent event) {
        this.tourAcheteeEnCours = "MitrailletteFrite";
        System.out.println("Mode placement : MitrailletteFrite sélectionnée !");
    }

    @FXML
    public void clicBoutonLanceBurger(ActionEvent event) {
        this.tourAcheteeEnCours = "LanceBurger";
        System.out.println("Mode placement : LanceBurger sélectionné !");
    }

    @FXML
    public void clicBoutonBacGlace(ActionEvent event) {
        this.tourAcheteeEnCours = "BacGlace";
        System.out.println("Mode placement : BacGlace sélectionnée !");
    }

    @FXML
    public void clicBoutonBarbecue(ActionEvent event) {
        this.tourAcheteeEnCours = "Barbecue";
        System.out.println("Mode placement : Barbecue sélectionnée !");
    }

    // Gestion du clic sur le terrain pour poser la tour sélectionnée
    @FXML
    public void clicSurTerrain(MouseEvent event) {
        // Si aucune tour n'est sélectionnée dans la boutique, on fait rien
        if (this.tourAcheteeEnCours == null) return;

        // Ajustement des coordonnées pour aligner la tour sur la grille (cases de 36x36)
        int xAjuste = ((int) event.getX() / 36) * 36;
        int yAjuste = ((int) event.getY() / 36) * 36;

        // Ajout de la tour dans le modèle (la vue intercepte l'ajout via écouteur)
        this.env.ajouterTour(xAjuste, yAjuste, this.tourAcheteeEnCours);

        // Remise à zéro de la sélection après le placement
        this.tourAcheteeEnCours = null;
    }


}