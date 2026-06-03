package universite_paris8.iut.rissamou.sae_td.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import universite_paris8.iut.rissamou.sae_td.modele.*;
import universite_paris8.iut.rissamou.sae_td.vue.EntiteVue;
import universite_paris8.iut.rissamou.sae_td.vue.GestionImage;
import universite_paris8.iut.rissamou.sae_td.vue.TerrainVue;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private TilePane map;

    private universite_paris8.iut.rissamou.sae_td.vue.TerrainVue TerrainVue;

    private Timeline gameLoop;

    private int temps;
    @FXML
    private Pane panneauJeu;

    public Terrain terrain;

    private EntiteVue entiteVue; // Déclare la vue des entités
    private List<Enemie> listeEnnemis = new ArrayList<>();
    private int zombiesAFaireApparaitre = 0;
    private int delaiAvantProchainZombie = 0;
    private boolean vagueEnCours = false; // Pour bloquer le bouton pendant la vague
    private Comptoir base;
    private int numeroVague = 0; // Pour compter à quelle vague on est
    private int tempsAvantProchaineVague = 0; // Le compte à rebours entre deux vagues

    @Override
    public void initialize(URL location, ResourceBundle resources){
        GestionImage.loadAssets();

        TerrainVue = new TerrainVue();
        TerrainVue.map = map;
        // création du terrain
        TerrainVue.creerTerrain();
        terrain= new Terrain();

        //System.out.println(terrain.grille.length);
        //System.out.println(terrain.grille[0].length);


        // On initialise la vue des entités en lui passant le Pane du FXML
        entiteVue = new EntiteVue(panneauJeu);
        faireApparaitreComptoir();

        initAnimation();

    }

    @FXML
    public void reactionBouton(ActionEvent event) {
        // Le bouton ne marche que si le jeu n'a pas encore commencé (vague 0)
        if (numeroVague == 0) {
            preparerNouvelleVague();
            gameLoop.play();
            System.out.println("Jeu lancé !");
        }
    }

    public void preparerNouvelleVague() {
        numeroVague++; // On passe à la vague suivante
        vagueEnCours = true;

        // La difficulté augmente : 5 zombies à la vague 1, 10 à la vague 2, etc.
        zombiesAFaireApparaitre = 5 * numeroVague;
        delaiAvantProchainZombie = 0;

        System.out.println("DÉBUT DE LA VAGUE " + numeroVague + " ! (" + zombiesAFaireApparaitre + " zombies)");
    }

    public void faireApparaitreComptoir(){
        Point arrivee = terrain.trouverSortie();

        // On multiplie par 36 pour convertir la case de la grille en pixels !
        base = new Comptoir(arrivee.y * 36, arrivee.x * 36, "Comptoir");

        // N'oublie pas de demander à la vue de l'afficher juste après :
        entiteVue.afficherComptoir(base);
    }

    public void faireApparaitreZombie() {
        // On récupère automatiquement les coordonnées depuis la grille !
        Point depart = terrain.trouverEntree();
        Point arrivee = terrain.trouverSortie();

        //place le zombie exactement sur le pixel de départ
        Enemie zombie = new Enemie(depart.y * 36, depart.x * 36, 4, "Zombie");

        // On calcule le chemin et on le donne au zombie
        List<Point> cheminZombie = Bfs.bfs(terrain.grille, depart, arrivee);
        zombie.setChemin(cheminZombie);
        System.out.println("Nombre de cases trouvées par le BFS : " + cheminZombie.size());
        listeEnnemis.add(zombie);
        entiteVue.afficherEnnemie(zombie);
    }

    private void initAnimation() {
        gameLoop = new Timeline();
        temps = 0;
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    if (temps % 5 == 0) {

                        // 1. GESTION DES APPARITIONS
                        if (zombiesAFaireApparaitre > 0) {
                            delaiAvantProchainZombie--;
                            if (delaiAvantProchainZombie <= 0) {
                                faireApparaitreZombie();
                                zombiesAFaireApparaitre--;
                                delaiAvantProchainZombie = 10;
                            }
                        }

                        // 2. DÉPLACEMENTS ET VÉRIFICATION DES MORTS
                        List<Enemie> poubelle = new ArrayList<>(); // La liste de ceux qu'on va supprimer

                        for (Enemie e : listeEnnemis) {
                            e.avancer();

                            // Si l'ennemi n'a plus de PV ou s'il a atteint la sortie
                            if (e.estMort() || e.estArrive()) {
                                poubelle.add(e); // On le met dans la poubelle
                            }
                        }

                        // 3. ON VIDE LA POUBELLE
                        for (Enemie e : poubelle) {
                            listeEnnemis.remove(e); // On l'enlève de la logique
                            entiteVue.retirerEnnemie(e); // On l'enlève de l'écran
                            System.out.println("zombie a fais son  taff");
                        }

                        // 4. MISE À JOUR DE L'ÉCRAN
                        entiteVue.mettreAJourAffichage();

                        // 5. CONDITION DE FIN DE VAGUE ET TRANSITION
                        if (vagueEnCours && zombiesAFaireApparaitre == 0 && listeEnnemis.isEmpty()) {
                            vagueEnCours = false;
                            System.out.println("🎉 Vague " + numeroVague + " terminée ! Prochaine vague dans 5 secondes...");

                            // On règle le compte à rebours pour la prochaine vague (environ 5 secondes de pause)
                            tempsAvantProchaineVague = 60; // 60 "tours" de boucle
                        }

                        // 6. COMPTE À REBOURS ENTRE LES VAGUES
                        // Si aucune vague n'est en cours, le chronomètre tourne
                        if (!vagueEnCours && numeroVague > 0) {
                            tempsAvantProchaineVague--;

                            if (tempsAvantProchaineVague <= 0) {
                                preparerNouvelleVague(); // La suite démarre toute seule !
                            }
                        }
                    }
                    temps++;
                })
        );
        gameLoop.getKeyFrames().add(kf);
    }


}