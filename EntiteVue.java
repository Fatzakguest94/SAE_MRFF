package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import universite_paris8.iut.fabdelrahim.sae.modele.Enemie;
import java.util.HashMap;
import java.util.Map;

public class EntiteVue {
    private Pane panneauJeu;
    private Map<Enemie, ImageView> dictionnaireImages = new HashMap<>();

    public EntiteVue(Pane terrain) {
        this.panneauJeu = terrain;
    }

    public void afficherEnnemie(Enemie e) {
        var image = GestionImage.getImage(e.getIdentite());
        if (image == null) return;

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setLayoutX(e.getX());
        imageView.setLayoutY(e.getY());

        panneauJeu.getChildren().add(imageView);
        dictionnaireImages.put(e, imageView);
    }

    public void mettreAJourAffichage() {
        // On boucle sur le dictionnaire pour caler les images sur la position de la logique
        for (Map.Entry<Enemie, ImageView> entree : dictionnaireImages.entrySet()) {
            Enemie ennemiLogic = entree.getKey();
            ImageView imageVisuelle = entree.getValue();

            if (ennemiLogic.estMort()) {
                imageVisuelle.setVisible(false); // Cache le zombie s'il est mort
            } else {
                imageVisuelle.setLayoutX(ennemiLogic.getX());
                imageVisuelle.setLayoutY(ennemiLogic.getY());
            }
        }
    }

    public void nettoyer() {
        for (ImageView iv : dictionnaireImages.values()) {
            panneauJeu.getChildren().remove(iv);
        }
        dictionnaireImages.clear();
    }
}
