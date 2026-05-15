package universite_paris8.iut.rissamou.sae_td;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    private Terrain terrain = new Terrain();

    public TilePane map;
    @FXML

    private void creerTerrain() {
        int[][] grille = terrain.grille;

        int tailleTuile = 36;
        map.setPrefColumns(31);
        map.setPrefRows(19);

        map.setPrefTileWidth(tailleTuile);
        map.setPrefTileHeight(tailleTuile);

        Image solnoir = new Image(getClass().getResourceAsStream("solblanc.png"));
        Image sol = new Image(getClass().getResourceAsStream("tapisrose.png"));
        Image entrer = new Image(getClass().getResourceAsStream("entrer.jpg"));
        Image sortie = new Image(getClass().getResourceAsStream("door.png"));
        Image barrage5 = new Image(getClass().getResourceAsStream("barrage5.png"));
        Image barrage6 = new Image(getClass().getResourceAsStream("barrage6.png"));
        Image panneau8 = new Image(getClass().getResourceAsStream("panneau8.png"));

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                ImageView cases = new ImageView();
                cases.setFitHeight(tailleTuile);
                cases.setFitWidth(tailleTuile);
                int t = grille[i][j];
                switch (t) {
                    case 0:
                        cases.setImage(solnoir);
                        break;
                    case 1:
                        cases.setImage(sol);
                        break;
                    case 2:
                        cases.setImage(solnoir);
                        break;

                    case 3:
                        cases.setImage(entrer);
                        break;

                    case 4:
                        cases.setImage(sortie);
                        break;

                    case 5:
                        cases.setImage(barrage5);
                        break;

                    case 6:
                        cases.setImage(barrage6);
                        break;

                    case 8:
                        cases.setImage(panneau8);
                        break;

                    default:
                        cases.setImage(solnoir);
                        break;
                }
                map.getChildren().add(cases);
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        terrain = new Terrain();


        //map.setPrefColumns(21);

        System.out.println(terrain.grille.length);
        System.out.println(terrain.grille[0].length);
        creerTerrain();//terrain.grille, map);
    }

}
