package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.Tour;

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

    public Node creerImageTour(Tour tour) {
        Image img = GestionImage.getImage(tour.getIdentite());
        if (img == null) return null;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);

        Label labelNiveau = new Label();
        labelNiveau.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        labelNiveau.setTextFill(Color.WHITE);
        labelNiveau.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 3; -fx-padding: 1 4 1 4;");

        labelNiveau.textProperty().bind(tour.niveauProperty().asString("Nv. %d"));

        VBox conteneurTour = new VBox(2);
        conteneurTour.setAlignment(Pos.CENTER);
        conteneurTour.setId(tour.getIdUnique());
        conteneurTour.getChildren().addAll(labelNiveau, imageView);

        conteneurTour.setTranslateY(-12);

        return conteneurTour;
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