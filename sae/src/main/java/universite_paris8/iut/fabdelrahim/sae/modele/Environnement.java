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
    private ObservableList<Tour> tours;

    //PROPRIÉTÉS OBSERVABLES
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
        this.argent = new SimpleIntegerProperty(100); // Argent de départ
        this.numeroVague = new SimpleIntegerProperty(0);
        this.vagueEnCours = new SimpleBooleanProperty(false);

        this.tours = FXCollections.observableArrayList();
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



    public void preparerNouvelleVague() {
        this.numeroVague.set(this.numeroVague.get() + 1);
        this.vagueEnCours.set(true);
        this.zombiesAFaireApparaitre = 10 * this.getNumeroVague();
        this.delaiAvantProchainZombie = 0;
    }

    public boolean payerAchat(int montant) {
        if (this.getArgent() >= montant) {
            this.argent.set(this.getArgent() - montant);
            return true;
        }
        return false;
    }
    public void rembourserVente(int montant) {
        this.argent.set(this.getArgent() + montant);
    }

    public void ajouterTour(int pixelX, int pixelY, String type) {
        int cout = 100; // Prix par défaut

        // 1. On définit les prix de chaque type de tour
        if (type.equals("LanceBurger")) {
            cout = 150;
        } else if (type.equals("MitrailletteFrite")) {
            cout = 100;
        } else if (type.equals("Tour3")) {
            cout = 200;
        } else if (type.equals("Tour4")) {
            cout = 250;
        }

        //Si le joueur a assez d'argent, on crée l'objet correspondant
        if (this.payerAchat(cout)) {
            switch (type) {
                case "LanceBurger":
                    this.tours.add(new LanceBurger(pixelX, pixelY));
                    break;
                case "MitrailletteFrite":
                    this.tours.add(new MitrailletteFrite(pixelX, pixelY));
                    break;
                case "Tour3":
                    // this.tours.add(new Tour3(pixelX, pixelY));
                    break;
                case "Tour4":
                    // this.tours.add(new Tour4(pixelX, pixelY));
                    break;
                default:
                    this.tours.add(new MitrailletteFrite(pixelX, pixelY));
                    break;
            }
            System.out.println(type + " achetée et placée !");
        } else {
            System.out.println("Pas assez de Tickets Resto pour acheter : " + type);
        }
    }
    public void faireApparaitreZombie() {
        Point depart = this.terrain.trouverEntree();

        // 1. On crée une variable pour stocker le type de zombie choisi
        String typeZombie = "ZombieNormal"; // Type par défaut pour la vague 1
        double vitesse = 4;

        int vagueActuelle = this.getNumeroVague();

        // 2. Logique d'apparition selon les vagues
        if (vagueActuelle >= 5) {
            // À partir de la vague 5 : Mélange de tous les zombies
            double hasard = Math.random(); // Génère un nombre entre 0.0 et 1.0
            if (hasard < 0.2) {
                typeZombie = "ZombieGros";
                vitesse = 2; // Très lent mais costaud
            } else if (hasard < 0.4) {
                typeZombie = "ZombieFamille";
                vitesse = 3;
            } else if (hasard < 0.7) {
                typeZombie = "ZombieRapide";
                vitesse = 8;
            } else {
                typeZombie = "ZombieNormal";
                vitesse = 4;
            }
        }
        else if (vagueActuelle >= 3) {
            // Vagues 3 et 4 : Introduction du ZombieGros
            if (this.zombiesAFaireApparaitre % 4 == 0) {
                typeZombie = "ZombieGros";
                vitesse = 1;
            } else if (this.zombiesAFaireApparaitre % 3 == 0) {
                typeZombie = "ZombieRapide";
                vitesse = 4;
            }
        }
        else if (vagueActuelle >= 2) {
            // Vague 2 : Introduction du ZombieRapide
            if (this.zombiesAFaireApparaitre % 3 == 0) {
                typeZombie = "ZombieRapide";
                vitesse = 4;
            }
        }
        // (Si vague 1 : aucune condition n'est vraie, il reste "ZombieNormal" à vitesse 2)

        //Création de l'entité avec les bonnes statistiques
        Enemie zombie = new Enemie((int)(depart.y * 36), (int)(depart.x * 36), vitesse, typeZombie);
        zombie.setChemin(this.chemin);

        //L'ajout à l'ObservableList réveille l'écouteur JavaFX automatiquement
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
                    // Le zombie inflige ses propres dégâts personnalisés (1, 2 ou 3 selon son type) !
                    this.base.recevoirDegats(z.getDegat());
                }

                if (z.prendreRecompense()) {
                    this.argent.set(this.getArgent() + 10); // Met à jour l'argent (le label suivra tout seul)
                }

                // Plus besoin de liste "poubelle". On remove directement de l'ObservableList.
                if (z.estMort() || z.estArrive()) {
                    this.zombies.remove(i);
                }
                if (z.estArrive() && this.base != null) {
                    this.base.recevoirDegats(z.getDegat()); // Le zombie inflige ses dégâts (1, 2 ou 3)
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


    // --- GETTERS DES PROPRIÉTÉS (Pour les liaisons / Bindings dans le contrôleur) ---
    public ObservableList<Enemie> getZombies() { return this.zombies; }

    public IntegerProperty argentProperty() { return this.argent; }
    public int getArgent() { return this.argent.get(); }

    public IntegerProperty numeroVagueProperty() { return this.numeroVague; }
    public int getNumeroVague() { return this.numeroVague.get(); }

    public BooleanProperty vagueEnCoursProperty() { return this.vagueEnCours; }
    public Terrain getTerrain() { return this.terrain; }
    public ObservableList<Tour> getTours() {return this.tours;}
    public Comptoir getBase() {
        return this.base;
    }
}
