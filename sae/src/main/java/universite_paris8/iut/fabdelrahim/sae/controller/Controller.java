package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.Tour;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;
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
    @FXML private Label gameOverLabel;
    @FXML private Label winLabel;
    @FXML private Label vagueAnnonce;
    @FXML private Label compteARebours;

    private TerrainVue terrainVue;
    private EntiteVue entiteVue;
    private Timeline gameLoop;
    private Environnement env;
    private String tourAcheteeEnCours = null; // Stocke l'action actuelle ("VENDRE", "AMELIORER" ou le type de tour)
    private int derniereVagueAffichee = 0;

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
        this.entiteVue = new EntiteVue();

        this.initEcouteurs();

        // Affichage du comptoir de départ
        if (this.env.getBase() != null) {
            ImageView imageComptoir = this.entiteVue.creerImageComptoir(
                    this.env.getBase().getIdentite(),
                    this.env.getBase().getX(),
                    this.env.getBase().getY()
            );
            if (imageComptoir != null) {
                this.panneauJeu.getChildren().add(imageComptoir);
            }
        }

        // Bindings de l'UI
        if (this.labelArgent != null) {
            this.labelArgent.textProperty().bind(env.argentProperty().asString(" : %d"));
        }
        if (this.labelVague != null) {
            this.labelVague.textProperty().bind(env.numeroVagueProperty().asString("Vague : %d"));
        }
        if (this.env.getBase() != null && this.pv != null) {
            this.pv.progressProperty().bind(this.env.getBase().hpProperty().divide(100.0));
        }

        this.initAnimation();
    }

    // Configuration des écouteurs de listes et des liaisons de propriétés (Bindings)
    private void initEcouteurs() {
        // Gestionnaire d'affichage des zombies (Ajout / Suppression)
        this.env.getZombies().addListener((ListChangeListener<Enemie>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Enemie nouveauZombie : change.getAddedSubList()) {
                        ImageView iv = this.entiteVue.creerImageZombie(nouveauZombie.getIdentite(), nouveauZombie.getIdUnique());
                        if (iv != null) {
                            iv.layoutXProperty().bind(nouveauZombie.xProperty());
                            iv.layoutYProperty().bind(nouveauZombie.yProperty());
                            this.panneauJeu.getChildren().add(iv);
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (Enemie zombieMort : change.getRemoved()) {
                        Node imgView = this.panneauJeu.lookup("#" + zombieMort.getIdUnique());
                        if (imgView != null) {
                            this.panneauJeu.getChildren().remove(imgView);
                        }
                    }
                }
            }
        });

        // Gestionnaire d'affichage des tours posées, vendues et améliorées
        this.env.getTours().addListener((ListChangeListener<Tour>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tour nouvelleTour : change.getAddedSubList()) {
                        if (this.panneauJeu.lookup("#" + nouvelleTour.getIdUnique()) != null) continue;

                        // CORRIGÉ MVC : On extrait uniquement les primitives / propriétés observables pour la vue
                        Node iv = this.entiteVue.creerImageTour(
                                nouvelleTour.getIdentite(),
                                nouvelleTour.getIdUnique(),
                                nouvelleTour.niveauProperty()
                        );

                        if (iv != null) {
                            iv.layoutXProperty().bind(nouvelleTour.xProperty());
                            iv.layoutYProperty().bind(nouvelleTour.yProperty());
                            this.panneauJeu.getChildren().add(iv);
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (Tour tourRetiree : change.getRemoved()) {
                        Node imgView = this.panneauJeu.lookup("#" + tourRetiree.getIdUnique());
                        if (imgView != null) {
                            this.panneauJeu.getChildren().remove(imgView);
                        }
                    }
                }
            }
        });

        // Gestionnaire d'affichage des projectiles
        this.env.getProjectiles().addListener((ListChangeListener<Projectile>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Projectile nouveauProj : change.getAddedSubList()) {
                        ImageView iv = this.entiteVue.creerImageProjectile(nouveauProj.getIdentite(), nouveauProj.getIdUnique());
                        if (iv != null) {
                            iv.layoutXProperty().bind(nouveauProj.xProperty());
                            iv.layoutYProperty().bind(nouveauProj.yProperty());
                            this.panneauJeu.getChildren().add(iv);
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (Projectile projDetruit : change.getRemoved()) {
                        Node imgView = this.panneauJeu.lookup("#" + projDetruit.getIdUnique());
                        if (imgView != null) {
                            this.panneauJeu.getChildren().remove(imgView);
                        }
                    }
                }
            }
        });
    }

    // Boucle principale du jeu (Game Loop)
    private void initAnimation() {
        this.gameLoop = new Timeline();
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                ev -> {
                    this.env.unTourDeJeu();

                    // Game Over
                    if (this.env.getBase() != null && this.env.getBase().estDetruit()) {
                        this.gameLoop.stop();
                        this.labelVague.textProperty().unbind();
                        this.labelVague.setText("GAME OVER !");
                        if (this.gameOverLabel != null) this.gameOverLabel.setVisible(true);
                        flouter(true);
                    }

                    // Victoire
                    if (this.env.toutesVaguesTerminees()) {
                        this.gameLoop.stop();
                        if (this.winLabel != null) this.winLabel.setVisible(true);
                        flouter(true);
                    }

                    // Changement de vague
                    if (this.env.getNumeroVague() != derniereVagueAffichee) {
                        derniereVagueAffichee = this.env.getNumeroVague();
                        afficherAnnonceVague(derniereVagueAffichee);
                        flouter(true);
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

        this.env = new Environnement();

        if (this.panneauJeu != null) {
            this.panneauJeu.getChildren().clear();
        }

        if (this.map != null) {
            this.map.getChildren().clear();
            this.terrainVue = new TerrainVue(env.getTerrain(), map);
            this.terrainVue.creerTerrain();
        }

        this.initJeu();
        this.lancerJeu(null);

        System.out.println("La partie a été réinitialisée avec succès !");
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

            if (boutonClique.getId() != null && boutonClique.getId().equals("nextBtn")) {
                Stage stageAide = (Stage) boutonClique.getScene().getWindow();
                if (stageAide != null) {
                    stageAide.close();
                }
                this.lancerJeu(null);
            } else {
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

    // Boutique : Choix des tours
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
    public void vendreT(ActionEvent event) {
        this.tourAcheteeEnCours = "VENDRE";
        System.out.println("Mode vente activé : cliquez sur une tour pour la revendre.");
    }

    @FXML
    public void ameliorerT(ActionEvent event) {
        this.tourAcheteeEnCours = "AMELIORER";
        System.out.println("Mode amélioration activé : cliquez sur une tour pour augmenter ses stats.");
    }

    @FXML
    public void clicSurTerrain(MouseEvent event) {
        if (this.tourAcheteeEnCours == null) return;

        int xAjuste = ((int) event.getX() / 36) * 36;
        int yAjuste = ((int) event.getY() / 36) * 36;

        if (this.tourAcheteeEnCours.equals("VENDRE")) {
            this.env.vendreTour(xAjuste, yAjuste);
        } else if (this.tourAcheteeEnCours.equals("AMELIORER")) {
            this.env.ameliorerTour(xAjuste, yAjuste);
        } else {
            this.env.ajouterTour(xAjuste, yAjuste, this.tourAcheteeEnCours);
        }

        this.tourAcheteeEnCours = null; // Réinitialisation de l'action après exécution
    }

    private void afficherAnnonceVague(int numeroVague) {
        if (this.gameLoop != null) {
            this.gameLoop.pause();
        }

        vagueAnnonce.setText("VAGUE " + numeroVague);
        vagueAnnonce.setVisible(true);
        compteARebours.setVisible(true);

        Timeline compteDown = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> compteARebours.setText("3")),
                new KeyFrame(Duration.seconds(1), e -> compteARebours.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> compteARebours.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> compteARebours.setVisible(false))
        );

        Timeline disparition = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> {
                    vagueAnnonce.setVisible(false);
                    flouter(false);

                    if (this.gameLoop != null) {
                        this.gameLoop.play();
                    }
                })
        );

        compteDown.play();
        disparition.play();
    }

    private void flouter(boolean actif) {
        if (actif) {
            map.setEffect(new GaussianBlur(10));
            panneauJeu.setEffect(new GaussianBlur(10));
        } else {
            map.setEffect(null);
            panneauJeu.setEffect(null);
        }
    }
}