package universite_paris8.iut.rissamou.sae_td.vue;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import universite_paris8.iut.rissamou.sae_td.modele.Comptoir;
import universite_paris8.iut.rissamou.sae_td.modele.Enemie;

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
    public void afficherComptoir(Comptoir base) {
        var image = GestionImage.getImage(base.getIdentite());
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(36);
        imageView.setFitHeight(36);

        imageView.setLayoutX(base.getX());
        imageView.setLayoutY(base.getY());

        paneauJeu.getChildren().add(imageView);
    }

    // Nouvelle méthode pour actualiser l'écran !
    public void mettreAJourAffichage() {
        for (Map.Entry<Enemie, ImageView> entree : dictionnaireImages.entrySet()) {
            Enemie enemie = entree.getKey();
            ImageView imageVisuelle = entree.getValue();

            // On aligne l'image sur les nouvelles coordonnées de l'objet
            imageVisuelle.setLayoutX(enemie.getX());
            imageVisuelle.setLayoutY(enemie.getY());
        }
    }

    // Méthode pour effacer un ennemi de l'écran
    public void retirerEnnemie(Enemie e) {
        ImageView imageAEnlever = dictionnaireImages.get(e);

        if (imageAEnlever != null) {
            paneauJeu.getChildren().remove(imageAEnlever); // On l'efface de l'écran
            dictionnaireImages.remove(e); // On l'efface de notre dictionnaire
        }
    }
}

