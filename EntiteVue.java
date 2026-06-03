package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import universite_paris8.iut.fabdelrahim.sae.modele.Comptoir;
import universite_paris8.iut.fabdelrahim.sae.modele.Enemie;

import java.util.HashMap;
import java.util.Map;

public class EntiteVue {

    private Pane panneauJeu;
    private Map<Enemie, ImageView> dictionnaireImages;
    private ImageView imageComptoir;

    public EntiteVue(Pane terrain) {
        this.panneauJeu = terrain;
        this.dictionnaireImages = new HashMap<Enemie, ImageView>();
    }

    public void afficherEnnemie(Enemie e) {
        // Si l'ennemi a déjà une image affichée, on ne la recrée pas !
        if (this.dictionnaireImages.containsKey(e)) {
            return;
        }

        Image img = GestionImage.getImage(e.getIdentite());
        if (img == null) return;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setLayoutX(e.getX());
        imageView.setLayoutY(e.getY());

        this.panneauJeu.getChildren().add(imageView);
        this.dictionnaireImages.put(e, imageView);
    }

    public void afficherComptoir(Comptoir base) {
        Image img = GestionImage.getImage(base.getIdentite());
        if (img == null) return;

        this.imageComptoir = new ImageView(img);
        this.imageComptoir.setFitWidth(36);
        this.imageComptoir.setFitHeight(36);
        this.imageComptoir.setLayoutX(base.getX());
        this.imageComptoir.setLayoutY(base.getY());

        this.panneauJeu.getChildren().add(this.imageComptoir);
    }

    public void mettreAJourAffichage() {
        // On crée une liste des clés pour pouvoir faire un parcours simple sans bug de modification
        Object[] cles = this.dictionnaireImages.keySet().toArray();

        for (int i = 0; i < cles.length; i++) {
            Enemie zombie = (Enemie) cles[i];
            ImageView imageVisuelle = this.dictionnaireImages.get(zombie);

            // Si le zombie doit disparaître (mort ou arrivé)
            if (zombie.estMort() || zombie.estArrive()) {
                this.panneauJeu.getChildren().remove(imageVisuelle);
                this.dictionnaireImages.remove(zombie);
            } else {
                // Sinon, on le déplace visuellement
                imageVisuelle.setLayoutX(zombie.getX());
                imageVisuelle.setLayoutY(zombie.getY());
            }
        }
    }

    public void nettoyer() {
        // Enlève tous les zombies de l'écran
        for (ImageView iv : this.dictionnaireImages.values()) {
            this.panneauJeu.getChildren().remove(iv);
        }
        this.dictionnaireImages.clear();

        // Enlève le comptoir
        if (this.imageComptoir != null) {
            this.panneauJeu.getChildren().remove(this.imageComptoir);
            this.imageComptoir = null;
        }
    }
}
