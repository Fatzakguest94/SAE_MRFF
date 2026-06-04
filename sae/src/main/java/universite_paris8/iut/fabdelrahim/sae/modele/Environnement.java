package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Environnement {

    private Terrain terrain;
    private List<Point> chemin;
    private Comptoir base;
    private List<Tour> tours;

    // --- PROPRIÉTÉS OBSERVABLES (Demandées par le prof) ---
    private ObservableList<Enemie> zombies;
    private IntegerProperty argent;
    private IntegerProperty numeroVague;
    private BooleanProperty vagueEnCours;

    private int temps;
    private int zombiesAFaireApparaitre;
    private int delaiAvantProchainZombie;
    private int tempsAvantProchaineVague;

    public Environnement() {
        this.terrain = new Terrain();

        // Initialisation des observables
        this.zombies = FXCollections.observableArrayList();
        this.argent = new SimpleIntegerProperty(200); // Argent de départ
        this.numeroVague = new SimpleIntegerProperty(0);
        this.vagueEnCours = new SimpleBooleanProperty(false);

        this.tours = new ArrayList<>();
        this.temps = 0;
        this.initialiserCheminAndBase();
    }

    private void initialiserCheminAndBase() {
        Point debut = this.terrain.trouverEntree();
        Point arrivee = this.terrain.trouverSortie();
        this.chemin = Bfs.bfs(this.terrain.grille, debut, arrivee);

        if (this.chemin.isEmpty()) {
            System.out.println("Aucun chemin trouvé !");
        }
        this.base = new Comptoir((int)(arrivee.y * 36), (int)(arrivee.x * 36), "SuperComptoir");
    }

    // --- GETTERS DES PROPRIÉTÉS (Pour les liaisons / Bindings dans le contrôleur) ---
    public ObservableList<Enemie> getZombies() { return this.zombies; }

    public IntegerProperty argentProperty() { return this.argent; }
    public int getArgent() { return this.argent.get(); }

    public IntegerProperty numeroVagueProperty() { return this.numeroVague; }
    public int getNumeroVague() { return this.numeroVague.get(); }

    public BooleanProperty vagueEnCoursProperty() { return this.vagueEnCours; }

    public void preparerNouvelleVague() {
        this.numeroVague.set(this.numeroVague.get() + 1);
        this.vagueEnCours.set(true);
        this.zombiesAFaireApparaitre = 5 * this.getNumeroVague();
        this.delaiAvantProchainZombie = 0;
    }

    public boolean payerAchat(int montant) {
        if (this.getArgent() >= montant) {
            this.argent.set(this.getArgent() - montant);
            return true;
        }
        return false;
    }

    public void ajouterTour(int pixelX, int pixelY) {
        if (this.payerAchat(100)) {
            this.tours.add(new Tour(pixelX, pixelY));
            System.out.println("Tour achetée et placée !");
        } else {
            System.out.println("Pas assez de Tickets Resto...");
        }
    }

    public void faireApparaitreZombie() {
        Point depart = this.terrain.trouverEntree();
        String typeZombie = (this.zombiesAFaireApparaitre % 3 == 0) ? "ZombieRapide" : "ZombieNormal";
        double vitesse = typeZombie.equals("ZombieRapide") ? 4 : 2;

        Enemie zombie = new Enemie((int)(depart.y * 36), (int)(depart.x * 36), vitesse, typeZombie);
        zombie.setChemin(this.chemin);

        // L'ajout dans la liste déclenche automatiquement l'affichage dans la Vue
        this.zombies.add(zombie);
    }

    public void unTourDeJeu() {
        this.temps++;

        if (this.temps % 5 == 0) {
            // Gestion des apparitions
            if (this.zombiesAFaireApparaitre > 0) {
                this.delaiAvantProchainZombie--;
                if (this.delaiAvantProchainZombie <= 0) {
                    this.faireApparaitreZombie();
                    this.zombiesAFaireApparaitre--;
                    this.delaiAvantProchainZombie = 10;
                }
            }

            // Attaque des tours
            for (Tour t : this.tours) {
                t.attaquer(this.zombies);
            }

            // Déplacement et gestion des états des zombies
            // (On parcourt à l'envers pour pouvoir supprimer en toute sécurité pendant la boucle)
            for (int i = this.zombies.size() - 1; i >= 0; i--) {
                Enemie z = this.zombies.get(i);
                z.avancer();

                if (z.estArrive() && this.base != null) {
                    this.base.recevoirDegats(10);
                }

                if (z.prendreRecompense()) {
                    this.argent.set(this.getArgent() + 50); // Met à jour l'argent (le label suivra tout seul)
                }

                // Plus besoin de liste "poubelle". On remove directement de l'ObservableList.
                if (z.estMort() || z.estArrive()) {
                    this.zombies.remove(i);
                }
            }

            // Gestion de la transition des vagues
            if (this.vagueEnCours.get() && this.zombiesAFaireApparaitre == 0 && this.zombies.isEmpty()) {
                this.vagueEnCours.set(false);
                this.tempsAvantProchaineVague = 60;
            }

            if (!this.vagueEnCours.get() && this.getNumeroVague() > 0) {
                this.tempsAvantProchaineVague--;
                if (this.tempsAvantProchaineVague <= 0) {
                    this.preparerNouvelleVague();
                }
            }
        }
    }

    public Terrain getTerrain() { return this.terrain; }
    public List<Tour> getTours() { return this.tours; }
    public Comptoir getBase() {
        return this.base;
    }
}
