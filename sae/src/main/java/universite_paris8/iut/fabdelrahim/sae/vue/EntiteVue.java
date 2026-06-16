package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EntiteVue {

    public EntiteVue() {
        // Constructeur vide, les fabrications se font à la demande via les méthodes ci-dessous
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

    //Crée le visuel du projectile sur la carte
    public ImageView creerImageProjectile(String identite, String idUnique) {
        Image img = GestionImage.getImage(identite); // Ira chercher "Burger" ou "Frites"
        if (img == null) return null;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(16);  // Plus petit qu'un zombie (ex: 16x16 pixels)
        imageView.setFitHeight(16);
        imageView.setId(idUnique);

        return imageView;
    }

    public ImageView creerImageTour(String identite, String idUnique) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return null;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setId(idUnique);

        return imageView;
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