package universite_paris8.iut.rissamou.sae_td;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import universite_paris8.iut.rissamou.sae_td.Terrain;
import universite_paris8.iut.rissamou.sae_td.TerrainVue;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private TilePane map;

    private universite_paris8.iut.rissamou.sae_td.TerrainVue TerrainVue;

    private Timeline gameLoop;

    private int temps;
    @FXML
    private Pane panneauJeu;

    public Terrain terrain;

    private EntiteVue entiteVue; // Déclare la vue des entités
    private List<Enemie> listeEnnemis = new ArrayList<>();

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

        initAnimation();

    }

    @FXML
    public void reactionBouton(ActionEvent event) {
        faireApparaitreZombie();
        gameLoop.play();
        System.out.println("lancement du parcours");
    }

    public void faireApparaitreZombie() {
        // On récupère automatiquement les coordonnées depuis la grille !
        Point depart = terrain.trouverEntree();
        Point arrivee = terrain.trouverSortie();

        // 💡 Astuce : On place le zombie exactement sur le pixel de départ
        Enemie zombie = new Enemie(depart.y * 36, depart.x * 36, 18, "Zombie");

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
                    if (temps == 1000) { // J'ai augmenté un peu le temps pour te laisser voir l'animation
                        System.out.println("fini");
                        gameLoop.stop();
                    } else if (temps % 5 == 0) {

                        // NOUVEAU 4 : La vraie magie opère ici !
                        // On demande à CHAQUE ennemi de la liste d'avancer un peu
                        for (Enemie e : listeEnnemis) {
                            e.avancer();
                            //System.out.println("Position de l'ennemi en mémoire : X=" + e.getX() + ", Y=" + e.getY());
                        }

                        // Une fois qu'ils ont tous bougé dans le "modèle",
                        // on dit à la vue de mettre à jour les ImageView sur l'écran
                        entiteVue.mettreAJourAffichage();
                    }
                    temps++;
                })
        );
        gameLoop.getKeyFrames().add(kf);
    }


}