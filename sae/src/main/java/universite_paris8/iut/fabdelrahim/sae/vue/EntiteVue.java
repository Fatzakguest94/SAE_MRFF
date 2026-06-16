package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;

public class EntiteVue {

    public EntiteVue() {

    }

    public ImageView creerImageZombie(String identite, String idUnique) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return null;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setId(idUnique);

        return imageView;
    }

    public ImageView creerImageProjectile(String identite, String idUnique) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return null;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        imageView.setId(idUnique);

        return imageView;
    }


    public VBox creerImageTour(String identite, String idUnique, IntegerProperty niveauProperty) {
        try {
            URL fxmlUrl = getClass().getResource("/universite_paris8/iut/fabdelrahim/sae/barreniv.fxml");
            if (fxmlUrl == null) return null;

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            VBox conteneurTour = loader.load();

            Label labelNiveau = (Label) loader.getNamespace().get("labelNiveau");
            ImageView imageTour = (ImageView) loader.getNamespace().get("imageTour");

            Image img = GestionImage.getImage(identite);
            if (img != null && imageTour != null) {
                imageTour.setImage(img);
            }

            conteneurTour.setId(idUnique);

            if (labelNiveau != null && niveauProperty != null) {
                labelNiveau.textProperty().bind(niveauProperty.asString("Nv. %d"));
            }

            return conteneurTour;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageView creerImageComptoir(String identite, int x, int y) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return null;

        ImageView imageComptoir = new ImageView(img);
        imageComptoir.setFitWidth(36);
        imageComptoir.setFitHeight(36);
        imageComptoir.setLayoutX(x);
        imageComptoir.setLayoutY(y);

        return imageComptoir;
    }
}