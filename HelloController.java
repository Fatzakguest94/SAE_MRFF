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

        int tailleTuile = 28;
        map.setPrefColumns(21);
        map.setPrefRows(15);
        Image parquet = new Image(getClass().getResourceAsStream("vmur.jpeg"));
        Image mur = new Image(getClass().getResourceAsStream("mur.png"));

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                ImageView cases = new ImageView();
                cases.setFitHeight(tailleTuile);
                cases.setFitWidth(tailleTuile);
                int t = grille[i][j];
                switch (t) {
                    case 0:
                        cases.setImage(mur);
                        break;
                    case 1:
                        cases.setImage(mur);
                    case 2:
                        cases.setImage(parquet);
                        break;

                    case 3:

                        break;

                    default:

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