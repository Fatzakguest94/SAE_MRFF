package universite_paris8.iut.rissamou.sae_td;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.HashMap;
import java.util.Map;

public class EntiteVue {
    private Pane paneauJeu;
    // Le dictionnaire magique qui relie un ennemi à son image à l'écran
    private Map<Enemie, ImageView> dictionnaireImages = new HashMap<>();

    public EntiteVue(Pane terrain) {
        this.paneauJeu = terrain;
    }

    public void afficherEnnemie(Enemie e) {
        var image = GestionImage.getImage(e.getIdentite());
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(36);
        imageView.setFitHeight(36);

        imageView.setLayoutX(e.getX());
        imageView.setLayoutY(e.getY());

        paneauJeu.getChildren().add(imageView);

        // On mémorise le lien !
        dictionnaireImages.put(e, imageView);
    }

    // Nouvelle méthode pour actualiser l'écran !
    public void mettreAJourAffichage() {
        for (Map.Entry<Enemie, ImageView> entree : dictionnaireImages.entrySet()) {
            Enemie ennemiLogic = entree.getKey();
            ImageView imageVisuelle = entree.getValue();

            // On aligne l'image sur les nouvelles coordonnées de l'objet
            imageVisuelle.setLayoutX(ennemiLogic.getX());
            imageVisuelle.setLayoutY(ennemiLogic.getY());
        }
    }
}

