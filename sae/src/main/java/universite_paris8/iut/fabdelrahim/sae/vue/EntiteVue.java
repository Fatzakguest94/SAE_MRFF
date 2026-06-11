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

        // 1. Gestionnaire d'affichage des ZOMBIES (Correction ici !)
        env.getZombies().addListener((ListChangeListener<Enemie>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Enemie nouveauZombie : change.getAddedSubList()) {
                        // On appelle ta méthode pour créer l'image du zombie sur la map
                        this.creerImageZombie(nouveauZombie);
                    }
                }

                if (change.wasRemoved()) {
                    for (Enemie zombieMort : change.getRemoved()) {
                        // On cherche l'image du zombie par son ID unique pour la supprimer
                        Node imgView = this.panneauJeu.lookup("#" + zombieMort.getIdUnique());
                        if (imgView != null) {
                            this.panneauJeu.getChildren().remove(imgView);
                        }
                    }
                }
            }
        });

        // 2. Gestionnaire d'affichage des TOURS (Pose et Vente)
        env.getTours().addListener((ListChangeListener<Tour>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tour nouvelleTour : change.getAddedSubList()) {
                        this.afficherTour(nouvelleTour);
                    }
                }

                if (change.wasRemoved()) {
                    for (Tour tourRetiree : change.getRemoved()) {
                        // On cherche l'image de la tour pour la supprimer (utile pour la vente)
                        Node imgView = this.panneauJeu.lookup("#" + tourRetiree.getIdUnique());
                        if (imgView != null) {
                            this.panneauJeu.getChildren().remove(imgView);
                        }
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