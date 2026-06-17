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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Objet;
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

    // Bonus (Objet Rouleau)
    @FXML private ProgressBar barrebonus;
    @FXML private VBox bonuseffet;

    // Attributs Techniques 
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

        //Bindings et Configuration de l'UI 

        // Argent, Vague, PV Base (Bindings simples)
        if (this.labelArgent != null) {
            this.labelArgent.textProperty().bind(env.argentProperty().asString(" : %d"));
        }
        if (this.labelVague != null) {
            this.labelVague.textProperty().bind(env.numeroVagueProperty().asString("Vague : %d"));
        }
        if (this.env.getBase() != null && this.pv != null) {
            this.pv.progressProperty().bind(this.env.getBase().hpProperty().divide(100.0));
        }

        // Configuration Jauge Bonus (Binding + Listener)
        if (this.env != null && this.barrebonus != null && this.bonuseffet != null) {
            // Liaison mathématique (Temps / Total)
            this.barrebonus.progressProperty().bind(this.env.tempsbonusProperty().divide(600.0));

            // État initial caché
            this.bonuseffet.setVisible(false);

            // Listener pour gérer la visibilité
            this.env.tempsbonusProperty().addListener((observable, ancienneValeur, nouvelleval) -> {
                if ((int) nouvelleval > 0) { // On affiche la boîte si le temps est sup à 0
                    this.bonuseffet.setVisible(true);
                } else {
                    this.bonuseffet.setVisible(false);
                }
            });
        }

        // Démarrage de la boucle de jeu
        this.initAnimation();
    }

    // Configuration des écouteurs de listes pour l'affichage dynamique 
    private void initEcouteurs() {
        // Gestionnaire d'affichage des zombies (Ajout / Suppression dans le panneau)
        this.env.getZombies().addListener((ListChangeListener<Enemie>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Enemie nouveauZombie : change.getAddedSubList()) {
                        // Création de l'image via la vue
                        ImageView iv = this.entiteVue.creerImageZombie(nouveauZombie.getIdentite(), nouveauZombie.getIdUnique());
                        if (iv != null) {
                            // Liaison des coordonnées View <-> Model (Property Binding)
                            iv.layoutXProperty().bind(nouveauZombie.xProperty());
                            iv.layoutYProperty().bind(nouveauZombie.yProperty());
                            // Ajout physique à l'écran
                            this.panneauJeu.getChildren().add(iv);
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (Enemie zombieMort : change.getRemoved()) {
                        // Recherche de l'image par son ID unique pour la supprimer
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
                        // Sécurité pour éviter les doublons d'affichage
                        if (this.panneauJeu.lookup("#" + nouvelleTour.getIdUnique()) != null) continue;
                        
                        // Création du Node (VBox avec image + label niveau)
                        // On extrait uniquement les primitives / propriétés observables pour la vue
                        Node iv = this.entiteVue.creerImageTour(
                                nouvelleTour.getIdentite(),
                                nouvelleTour.getIdUnique(),
                                nouvelleTour.niveauProperty() // MVC : On passe la propriété, pas la valeur fixe
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
                Duration.seconds(0.017), // Environ 60 FPS
                ev -> {
                    //Mise à jour Logique du Modèle
                    this.env.unTourDeJeu();

                    //Gestion spécifique de l'Objet au Sol (Rouleau)
                    Objet obj = this.env.getObjdrop();
                    // Si un objet logique existe, n'est pas encore affiché et n'est pas ramassé
                    if (obj != null && !obj.isDejaAffiche() && !obj.isRamasse()){
                        faireApparaitreObjet(obj); // On crée son ImageView
                        obj.setDejaAffiche(true);  // Sécurité pour ne pas recréer l'image au prochain tick
                    }

                    //Gestion des États de Jeu (Game Over, Win, Vagues)

                    // Condition de défaite
                    if (this.env.getBase() != null && this.env.getBase().estDetruit()) {
                        this.gameLoop.stop();
                        this.labelVague.textProperty().unbind(); // On coupe le lien
                        this.labelVague.setText("GAME OVER !");
                        if (this.gameOverLabel != null) this.gameOverLabel.setVisible(true);
                        flouter(true); // Effet visuel
                    }

                    // Condition de Victoire
                    if (this.env.toutesVaguesTerminees()) {
                        this.gameLoop.stop();
                        if (this.winLabel != null) this.winLabel.setVisible(true);
                        flouter(true);
                    }

                    // Détection de Changement de vague (Annonces)
                    if (this.env.getNumeroVague() != derniereVagueAffichee) {
                        derniereVagueAffichee = this.env.getNumeroVague();
                        afficherAnnonceVague(derniereVagueAffichee); // Pause + Décompte visuel
                        flouter(true);
                    }
                }
        );
        this.gameLoop.getKeyFrames().add(kf);
    }

    
     //Crée et gère l'ImageView physique de l'objet au sol.
     //Inclut la gestion du clic pour le ramassage.
      public void faireApparaitreObjet(Objet objet){
        // Récupération de l'image préchargée
        ImageView vueObjet = new ImageView(GestionImage.getImage("Rouleau"));

        // Taille visuelle
        vueObjet.setFitWidth(30);
        vueObjet.setFitHeight(30);

        // Positionnement physique initial (pas de binding ici car on le supprime au clic)
        vueObjet.setTranslateX(objet.getX());
        vueObjet.setTranslateY(objet.getY());

        // Gestionnaire de CLIC sur l'objet 
        vueObjet.setOnMouseClicked(event -> {
            objet.ramasser(); // Action logique sur le modèle
            this.panneauJeu.getChildren().remove(vueObjet); // Action physique sur la vue
        });

        // Ajout physique au panneau de jeu
        this.panneauJeu.getChildren().add(vueObjet);
    }

    //Actions UI (Boutons, Clics)

    @FXML
    public void lancerJeu(ActionEvent event) {
        if (this.gameLoop != null && this.gameLoop.getStatus() != Timeline.Status.RUNNING) {
            // Si la partie n'a pas commencé, on prépare la vague 1
            if (this.env.getNumeroVague() == 0) {
                this.env.preparerNouvelleVague();
            }
            this.gameLoop.play();
        }
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {
        //Arrêt net de la boucle
        if (this.gameLoop != null) {
            this.gameLoop.stop();
        }

        // Réinitialisation complète du Modèle
        this.env = new Environnement();

        //Nettoyage complet de la Vue
        if (this.panneauJeu != null) {
            this.panneauJeu.getChildren().clear(); // Supprime toutes les images
        }

        if (this.map != null) {
            this.map.getChildren().clear(); // Supprime les tuiles de carte
            // Reconstruction de la carte
            this.terrainVue = new TerrainVue(env.getTerrain(), map);
            this.terrainVue.creerTerrain();
        }

        //Reset des variables d'état du contrôleur
        this.tourAcheteeEnCours = null;
        this.derniereVagueAffichee = 0;

        //Reset UI d'état (cachées)
        if (this.gameOverLabel != null) this.gameOverLabel.setVisible(false);
        if (this.winLabel != null) this.winLabel.setVisible(false);
        if (this.vagueAnnonce != null) this.vagueAnnonce.setVisible(false);
        if (this.compteARebours != null) this.compteARebours.setVisible(false);
        flouter(false);

        //Relancement de la configuration initiale
        this.initJeu();
        this.lancerJeu(null); // Démarre automatiquement

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

            // Fermeture de la fenêtre d'aide (détection par l'ID du bouton dans aide.fxml)
            if (boutonClique.getId() != null && boutonClique.getId().equals("nextBtn")) {
                Stage stageAide = (Stage) boutonClique.getScene().getWindow();
                if (stageAide != null) {
                    stageAide.close();
                }
                this.lancerJeu(null); // Reprend le jeu
            }
            // Ouverture de la fenêtre d'aide
            else {
                if (gameLoop != null) gameLoop.pause(); // Pause obligatoire

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

    // Gestion de l'action sur le terrain (Clic de souris)
    @FXML
    public void clicSurTerrain(MouseEvent event) {
        if (this.tourAcheteeEnCours == null) return; // Si aucune tour choisie, on ne fait rien

        // Ajustement des coordonnées sur la grille (36x36) pour le Modèle
        int xAjuste = ((int) event.getX() / 36) * 36;
        int yAjuste = ((int) event.getY() / 36) * 36;

        // Action selon l'état actuel de tourAcheteeEnCours
        if (this.tourAcheteeEnCours.equals("VENDRE")) {
            this.env.vendreTour(xAjuste, yAjuste); // Appelle la logique dans le modèle
        } else if (this.tourAcheteeEnCours.equals("AMELIORER")) {
            this.env.ameliorerTour(xAjuste, yAjuste);
        } else {
            // Pose d'une tour classique
            this.env.ajouterTour(xAjuste, yAjuste, this.tourAcheteeEnCours);
        }

        // Réinitialisation de l'action de la boutique après exécution
        this.tourAcheteeEnCours = null;
    }

    

    
     // Affiche l'annonce textuelle et fait le décompte visuel avant la vague.
     //Met le jeu en pause.
    
    private void afficherAnnonceVague(int numeroVague) {
        // Pause Logique
        if (this.gameLoop != null) {
            this.gameLoop.pause();
        }

        // Configuration UI de l'annonce
        vagueAnnonce.setText("VAGUE " + numeroVague);
        vagueAnnonce.setVisible(true);
        compteARebours.setVisible(true);

        // Timeline pour le décompte 
        Timeline compteDown = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> compteARebours.setText("3")),
                new KeyFrame(Duration.seconds(1), e -> compteARebours.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> compteARebours.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> compteARebours.setVisible(false))
        );

        // Timeline pour la disparition et reprise
        Timeline disparition = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> {
                    vagueAnnonce.setVisible(false);
                    flouter(false); //On enlève le flou

                    // Reprise Logique
                    if (this.gameLoop != null) {
                        this.gameLoop.play();
                    }
                })
        );

        compteDown.play();
        disparition.play();
    }

    
     //Applique ou désactive un effet de flou sur le panneau de jeu.
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
