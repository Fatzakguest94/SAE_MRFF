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
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;

public class EntiteVue {

    private Pane panneauJeu;
    private ImageView imageComptoir;

    public EntiteVue(Pane terrain, Environnement env) {
        this.panneauJeu = terrain;

        //Gestionnaire d'affichage des zombies (Ajout / Suppression)
        env.getZombies().addListener((ListChangeListener<Enemie>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Enemie nouveauZombie : change.getAddedSubList()) {
                        this.creerImageZombie(nouveauZombie);
                    }
                }
                if (change.wasRemoved()) {
                    for (Enemie zombieMort : change.getRemoved()) {
                        Node imgView = this.panneauJeu.lookup("#" + zombieMort.getIdUnique());
                        if (imgView != null) {
                            this.panneauJeu.getChildren().remove(imgView);
                        }
                    }
                }
            }
        });

        //Gestionnaire d'affichage des tours posées et vendues
        env.getTours().addListener((ListChangeListener<Tour>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tour nouvelleTour : change.getAddedSubList()) {
                        this.afficherTour(nouvelleTour);
                    }
                }
                if (change.wasRemoved()) {
                    for (Tour tourRetiree : change.getRemoved()) {
                        Node imgView = this.panneauJeu.lookup("#" + tourRetiree.getIdUnique());
                        if (imgView != null) {
                            this.panneauJeu.getChildren().remove(imgView);
                        }
                    }
                }
            }
        });

        //Gestionnaire d'affichage des projectiles
        env.getProjectiles().addListener((ListChangeListener<Projectile>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Projectile nouveauProj : change.getAddedSubList()) {
                        this.creerImageProjectile(nouveauProj);
                    }
                }
                if (change.wasRemoved()) {
                    for (Projectile projDetruit : change.getRemoved()) {
                        Node imgView = this.panneauJeu.lookup("#" + projDetruit.getIdUnique());
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
        imageView.setId(e.getIdUnique());

        this.panneauJeu.getChildren().add(imageView);
    }

    //Crée le visuel du projectile sur la carte
    private void creerImageProjectile(Projectile p) {
        Image img = GestionImage.getImage(p.getIdentite()); // Ira chercher "Burger" ou "Frites"
        if (img == null) return;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(16);  // Plus petit qu'un zombie (ex: 16x16 pixels)
        imageView.setFitHeight(16);
        imageView.setLayoutX(p.getX());
        imageView.setLayoutY(p.getY());
        imageView.setId(p.getIdUnique());

        this.panneauJeu.getChildren().add(imageView);
    }

    public void mettreAJourAffichage(Environnement env) {
        // Mise à jour de la position des zombies
        for (Enemie zombie : env.getZombies()) {
            Node imgView = this.panneauJeu.lookup("#" + zombie.getIdUnique());
            if (imgView != null) {
                imgView.setLayoutX(zombie.getX());
                imgView.setLayoutY(zombie.getY());
            }
        }

        // Mise à jour de la position des projectiles en plein vol
        for (Projectile p : env.getProjectiles()) {
            Node imgView = this.panneauJeu.lookup("#" + p.getIdUnique());
            if (imgView != null) {
                imgView.setLayoutX(p.getX());
                imgView.setLayoutY(p.getY());
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


}