package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import universite_paris8.iut.fabdelrahim.sae.modele.Comptoir;
import universite_paris8.iut.fabdelrahim.sae.modele.Enemie;
import universite_paris8.iut.fabdelrahim.sae.modele.Tour;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;

public class EntiteVue {

    private Pane panneauJeu;
    private ImageView imageComptoir;

    public EntiteVue(Pane terrain, Environnement env) {
        this.panneauJeu = terrain;

        // 1. ÉCOUTE AUTOMATIQUE DE LA LISTE DE ZOMBIES (Déjà fait)
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

        // 2. RÉSOLUTION DU BUG : ÉCOUTE AUTOMATIQUE DE LA LISTE DE TOURS !
        env.getTours().addListener((ListChangeListener<Tour>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tour nouvelleTour : change.getAddedSubList()) {
                        // La vue détecte l'achat et dessine la tour TOUTE SEULE !
                        this.afficherTour(nouvelleTour);
                    }
                }
            }
        });
    }

    private void creerImageZombie(Enemie e) {
        // On demande à GestionImage de nous donner l'image brute
        // en fonction du TYPE de zombie (ex: "ZombieRapide")
        Image img = GestionImage.getImage(e.getIdentite());
        if (img == null) return;

        //On crée le composant visuel (le Node) qui va contenir cette image
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setLayoutX(e.getX());
        imageView.setLayoutY(e.getY());

        //on injecte l'ID unique du modèle dans le composant fx
        imageView.setId(e.getIdUnique());

        this.panneauJeu.getChildren().add(imageView);
    }

    // Déplace les images en les cherchant dynamiquement par leur ID unique
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

        // MODIFICATION ICI : On demande l'image correspondant à l'identité de la tour
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