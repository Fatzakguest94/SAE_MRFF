package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import universite_paris8.iut.fabdelrahim.sae.modele.Comptoir;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.Tour;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;

public class EntiteVue {

    private Pane panneauJeu;
    private ImageView imageComptoir;

    public EntiteVue(Pane terrain, Environnement env) {
        this.panneauJeu = terrain;

        // Gestionnaire d'affichage des zombies (Ajout / Suppression)
        env.getZombies().addListener((ListChangeListener<Enemie>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Enemie nouveauZombie : change.getAddedSubList()) {
                        creerImageZombie(nouveauZombie);
                    }
                }
                if (change.wasRemoved()) {
                    for (Enemie zombieRetire : change.getRemoved()) {
                        Node imgView = this.panneauJeu.lookup("#" + zombieRetire.getIdUnique());
                        if (imgView != null) {
                            this.panneauJeu.getChildren().remove(imgView);
                        }
                    }
                }
            }
        });

        // Gestionnaire d'affichage des tours posées
        env.getTours().addListener((ListChangeListener<Tour>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tour nouvelleTour : change.getAddedSubList()) {
                        this.afficherTour(nouvelleTour);
                    }
                }
            }
        });
    }

    private void creerImageZombie(Enemie e) {
        Image img = GestionImage.getImage(e.getIdentite());
        if (img == null) return;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setLayoutX(e.getX());
        imageView.setLayoutY(e.getY());
        imageView.setId(e.getIdUnique()); // Clé de recherche pour la mise à jour

        this.panneauJeu.getChildren().add(imageView);
    }

    public void mettreAJourAffichage(Environnement env) {
        for (Enemie zombie : env.getZombies()) {
            Node imgView = this.panneauJeu.lookup("#" + zombie.getIdUnique());
            if (imgView != null) {
                imgView.setLayoutX(zombie.getX());
                imgView.setLayoutY(zombie.getY());
            }
        }
    }

    public void afficherTour(Tour t) {
        if (this.panneauJeu.lookup("#" + t.getIdUnique()) != null) return;

        Image img = GestionImage.getImage(t.getIdentite());
        if (img == null) return;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setLayoutX(t.getX());
        imageView.setLayoutY(t.getY());
        imageView.setId(t.getIdUnique());

        this.panneauJeu.getChildren().add(imageView);
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

    public void viderTout() {
        this.panneauJeu.getChildren().clear();
        this.imageComptoir = null;
    }
}