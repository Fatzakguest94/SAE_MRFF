package universite_paris8.iut.fzekraoui.saedev.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;
import universite_paris8.iut.fzekraoui.saedev.modele.Point;
import universite_paris8.iut.fzekraoui.saedev.modele.Terrain;
import universite_paris8.iut.fzekraoui.saedev.modele.TerrainVue;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import java.io.IOException;


public class Controller implements Initializable {

    @FXML
    private TilePane map;

    @FXML
    private Pane menu1;

    private Timeline gameLoop;

    private TerrainVue TerrainVue;

    @FXML
    private ImageView zombie;

    @FXML
    private ImageView zombie1;

    @FXML
    private ImageView affame;

    @FXML
    private Pane panneauJeu;


    private int temps;

    private int money = 200; // UNE seule déclaration, argent de départ

    @FXML
    private Label labelMoney;

    private Terrain terrain;

    private Zombie zombieModel;
    private Zombie zombieModel1;

    private List<Point> chemin;

    private void updateLabelMoney() {
        labelMoney.setText(String.valueOf(this.money));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        zombie.setVisible(false);
        zombie1.setVisible(false);

        updateLabelMoney(); // afficher l'argent de départ

        Image img = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fzekraoui/saedev/vue/zombie.png"));
        zombie.setImage(img);
        zombie1.setImage(img);

        // TERRAIN
        TerrainVue = new TerrainVue();
        TerrainVue.map = map;
        TerrainVue.creerTerrain();
        terrain = new Terrain();

        System.out.println("Grille: " + terrain.grille.length + " x " + terrain.grille[0].length);

        // BFS (CHEMIN ZOMBIE)
        List<Point> chemin = Bfs.bfs(
                terrain.grille,
                new Point(12, 0),
                new Point(10, 31)
        );

        if (chemin.isEmpty()) {
            System.out.println("Aucun chemin trouvé !");
            return;
        }

        // ZOMBIES (MODELE MVC)
        zombieModel = new Zombie();
        zombieModel.setChemin(chemin);

        zombieModel1 = new Zombie();
        zombieModel1.setChemin(chemin);

        initAnimation();
    }

    @FXML
    public void reactionBouton(ActionEvent event) {
        zombie.setVisible(true);
        zombie1.setVisible(true);
        gameLoop.play();
        System.out.println("lancement du parcours");
    }

    public void dragdrop(ActionEvent event) {
        Image img = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fzekraoui/saedev/vue/panneau8.png"));
        affame.setImage(img);
        System.out.println("Payer");
    }

    public void reglage(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource("/universite_paris8/iut/fzekraoui/saedev/reglage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    // +50 quand un zombie meurt (une seule fois par zombie)
    public void money(Zombie zombie) {
        if (zombie.prendreRecompense()) {
            this.money += 50;
            updateLabelMoney();
            System.out.println("Money: " + this.money);
        }
    }

    // Acheter une tour : à appeler depuis gestionargent()
    public void gestionargent(ActionEvent event) {
        if (this.money >= 100) {
            this.money = this.money - 100;
            updateLabelMoney();
            System.out.println("acheter" + this.money);
        } else {
            System.out.println("manque d'argent");
        }
    }

    // temporaire
    public void rerun(ActionEvent event) {
        gameLoop.stop();
    }

    private void initAnimation() {
        gameLoop = new Timeline();
        temps = 0;
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                ev -> {
                    if (temps % 5 == 0) {
                        System.out.println("un tour");

                        // Mouvement des zombies
                        zombieModel.update(zombie, 36);
                        zombieModel1.update(zombie1, 36);

                        // Vérifier récompense après chaque update
                        money(zombieModel);
                        money(zombieModel1);
                    }

                    temps++;
                }
        );

        gameLoop.getKeyFrames().add(kf);
    }
}
