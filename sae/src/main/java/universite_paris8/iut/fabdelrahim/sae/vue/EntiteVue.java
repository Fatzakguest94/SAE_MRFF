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

        // --- ÉCOUTE AUTOMATIQUE DE LA LISTE DE ZOMBIES (SANS MAP) ---
        env.getZombies().addListener((ListChangeListener<Enemie>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Enemie nouveauZombie : change.getAddedSubList()) {
                        creerImageZombie(nouveauZombie);
                    }
                }
                if (change.wasRemoved()) {
                    for (Enemie zombieRetire : change.getRemoved()) {
                        // On cherche le composant graphique directement par son ID dans le panneau !
                        Node imgView = this.panneauJeu.lookup("#" + zombieRetire.getIdUnique());
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

        // C'est ici la magie : on injecte l'ID unique du modèle dans le composant FX !
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
        // Si l'image existe déjà sur le terrain, on ne fait rien
        if (this.panneauJeu.lookup("#" + t.getIdUnique()) != null) return;

        Image img = GestionImage.getImage("SuperComptoir");
        if (img == null) return;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setLayoutX(t.getX());
        imageView.setLayoutY(t.getY());

        // Attribuer l'ID unique de la tour à l'image
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