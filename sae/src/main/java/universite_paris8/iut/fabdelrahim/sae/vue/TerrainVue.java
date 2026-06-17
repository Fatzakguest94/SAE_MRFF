package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.fabdelrahim.sae.modele.Terrain;


public class TerrainVue {

    private Terrain terrain;
    private TilePane map;

    public TerrainVue(Terrain t,TilePane map){
        this.terrain = t;
        this.map = map;
    }




    @FXML
    public void creerTerrain() {
        int[][] grille = terrain.grille;

        int tailleTuile = 36;
        map.setPrefColumns(32);
        map.setPrefRows(21);

        map.setPrefTileWidth(tailleTuile);
        map.setPrefTileHeight(tailleTuile);


        Image sol = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/tapisrose.png"));
        Image entrer = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/entrer.jpg"));
        Image sortie = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/table.png"));
        Image barrage5 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/barrage5.png"));
        Image barrage6 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/barrage6.png"));
        Image panneau8 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/panneau8.png"));
        Image solcasser = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/solcasser.png"));
        Image c1 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c1.png"));
        Image c2 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c2.png"));
        Image c3 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c3.png"));
        Image c4 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c4.png"));
        Image c5 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c5.png"));
        Image c6 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c6.png"));
        Image c7 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c7.png"));
        Image c8 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c8.png"));
        Image c9 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c9.png"));
        Image c10 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c10.png"));
        Image c11 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c11.png"));
        Image c12 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c12.png"));
        Image c13 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c13.png"));
        Image c14 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c14.png"));
        Image c15 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c15.png"));
        Image c16 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c16.png"));
        Image c17 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c17.png"));
        Image c18 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c18.png"));
        Image c50 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/c50.png"));
        Image panneau9 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/panneau9.png"));
        Image chaise1 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/chaise1.png"));
        Image chaise2 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/chaise2.png"));
        Image chaise3 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/chaise3.png"));
        Image chaise4 = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/chaise4.png"));
        Image table = new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/table.png"));

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                ImageView cases = new ImageView();
                cases.setFitHeight(tailleTuile);
                cases.setFitWidth(tailleTuile);
                int t = grille[i][j];
                switch (t) {
                    case 0:
                        cases.setImage(table);
                        break;
                    case 1:
                        cases.setImage(sol);
                        break;
                    case 2:
                        cases.setImage(sol);
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

                    case 9:
                        cases.setImage(solcasser);
                        break;

                    case 10:
                        cases.setImage(c1);
                        break;

                    case 11:
                        cases.setImage(c2);
                        break;

                    case 12:
                        cases.setImage(c3);
                        break;

                    case 13:
                        cases.setImage(c4);
                        break;

                    case 14:
                        cases.setImage(c5);
                        break;

                    case 15:
                        cases.setImage(c6);
                        break;
                    case 16:
                        cases.setImage(c7);
                        break;

                    case 17:
                        cases.setImage(c8);
                        break;

                    case 18:
                        cases.setImage(c9);
                        break;
                    case 19:
                        cases.setImage(c10);
                        break;
                    case 20:
                        cases.setImage(c11);
                        break;
                    case 21:
                        cases.setImage(c12);
                        break;
                    case 22:
                        cases.setImage(c13);
                        break;

                    case 23:
                        cases.setImage(c14);
                        break;

                    case 24:
                        cases.setImage(c15);
                        break;

                    case 25:
                        cases.setImage(c16);
                        break;

                    case 26:
                        cases.setImage(c17);
                        break;

                    case 27:
                        cases.setImage(c18);
                        break;

                    case 28:
                        cases.setImage(panneau9);
                        break;

                    case 50:
                        cases.setImage(c50);
                        break;

                    case 100:
                        cases.setImage(c50);
                        break;

                    case 31:
                        cases.setImage(chaise1);
                        break;

                    case 32:
                        cases.setImage(chaise2);
                        break;

                    case 33:
                        cases.setImage(chaise3);
                        break;

                    case 34:
                        cases.setImage(chaise4);
                        break;

                    default:
                        cases.setImage(c50);
                        break;
                }
                map.getChildren().add(cases);
            }
        }
    }
}
