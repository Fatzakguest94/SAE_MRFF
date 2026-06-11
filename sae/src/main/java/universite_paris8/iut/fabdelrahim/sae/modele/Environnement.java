package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.fabdelrahim.sae.modele.Chemin.Bfs;
import universite_paris8.iut.fabdelrahim.sae.modele.Chemin.Point;
import universite_paris8.iut.fabdelrahim.sae.modele.Chemin.Terrain;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.*;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.*;

public class Environnement {

    // Paramètres d'équilibrage
    private static final int ArgentDepart = 100000;
    private static final int RecompenseParZombie = 10;
    private static final int TailleCase = 36;

    // Gestion du temps et des vagues
    private static final int Delaiavantaparition = 10;
    private static final int DelaientreVague = 60;
    private static final int PAS_LOGIQUE = 5;

    private final Terrain terrain;
    private final List<Point> chemin;
    private final Comptoir base;
    private final ObservableList<Tour> tours;
    private final ObservableList<Enemie> zombies;

    private final IntegerProperty argent;
    private final IntegerProperty numeroVague;
    private final BooleanProperty vagueEnCours;

    private int temps;
    private int zombiesRestantsASpawner;
    private int delaiAvantProchainZombie;
    private int tempsAvantProchaineVague;
    private int compteurToursSurChemin = 0;

    public Environnement() {
        this.terrain = new Terrain();
        this.tours = FXCollections.observableArrayList();
        this.zombies = FXCollections.observableArrayList();

        this.argent = new SimpleIntegerProperty(ArgentDepart);
        this.numeroVague = new SimpleIntegerProperty(0);
        this.vagueEnCours = new SimpleBooleanProperty(false);
        this.temps = 0;

        // Trajet des monstres via l'algorithme BFS
        Point debut = terrain.trouverEntree();
        Point arrivee = terrain.trouverSortie();
        this.chemin = Bfs.bfs(terrain.grille, debut, arrivee);

        if (chemin.isEmpty()) {
            System.err.println("Aucun chemin valide trouvé !");
        }

        // Placement du QG de fin de parcours
        this.base = new Comptoir(
                (int) (arrivee.y * TailleCase),
                (int) (arrivee.x * TailleCase),
                "SuperComptoir"
        );
    }

    public void preparerNouvelleVague() {
        numeroVague.set(getNumeroVague() + 1);
        vagueEnCours.set(true);

        zombiesRestantsASpawner = 10 * getNumeroVague(); // +10 zombies par vague
        delaiAvantProchainZombie = 0;
    }

    public void ajouterTour(int pixelX, int pixelY, String type) {
        int ligne = pixelY / TailleCase;
        int colonne = pixelX / TailleCase;
        int idTuile = terrain.grille[ligne][colonne];

        // Anti-superposition
        for (Tour t : tours) {
            if (t.getX() == pixelX && t.getY() == pixelY) {
                return;
            }
        }

        // Vérification des règles de placement
        boolean estTourSpeciale = type.equals("BacGlace") || type.equals("Barbecue");

        if (estTourSpeciale) {
            if (idTuile == 1 || idTuile == 100) {
                if (compteurToursSurChemin >= 6) return; // Limite sur chemin
            } else if (idTuile != 0) {
                return; // Interdit hors sol ou chemin
            }
        } else {
            if (idTuile != 0) return; // Tours classiques uniquement sur les tables (0)
        }

        int cout = coutTour(type);
        if (!payerAchat(cout)) return;

        Tour nouvelleTour = creerTour(pixelX, pixelY, type);
        if (nouvelleTour != null) {
            tours.add(nouvelleTour);
            if (idTuile == 1 || idTuile == 100) {
                compteurToursSurChemin++;
            }
        }
    }

    private int coutTour(String type) {
        switch (type) {
            case "LanceBurger":
                return 150;
            case "MitrailletteFrite":
                return 100;
            case "BacGlace":
                return 200;
            case "Barbecue":
                return 250;
            default:
                return 100;
        }
    }

    private Tour creerTour(int x, int y, String type) {
        switch (type) {
            case "LanceBurger":
                return new LanceBurger(x, y);
            case "MitrailletteFrite":
                return new MitrailletteFrite(x, y);
            case "BacGlace":
                return new BacGlace(x, y);
            case "Barbecue":
                return new Barbecue(x, y);
            default:
                return new MitrailletteFrite(x, y);
        }
    }

    public void unTourDeJeu() {
        temps++;
        if (temps % PAS_LOGIQUE != 0) return; // Régulateur de vitesse

        gererAparition();
        faireAttaquerLesTours();
        mettreAJourZombies();
        gererTransitionVague();
    }

    private void gererAparition() {
        if (zombiesRestantsASpawner <= 0) return;

        delaiAvantProchainZombie--;
        if (delaiAvantProchainZombie <= 0) {
            faireApparaitreZombie();
            zombiesRestantsASpawner--;
            delaiAvantProchainZombie = Delaiavantaparition;
        }
    }

    private void faireAttaquerLesTours() {
        for (Tour t : tours) {
            t.attaquer(zombies);
        }
    }

    private void mettreAJourZombies() {
        // Parcours inversé pour sécuriser les suppressions en cours de boucle
        for (int i = zombies.size() - 1; i >= 0; i--) {
            Enemie z = zombies.get(i);
            z.avancer();

            if (z.estArrive() && base != null) {
                base.recevoirDegats(z.getDegat());
            }

            if (z.prendreRecompense()) {
                argent.set(getArgent() + RecompenseParZombie);
            }

            // Division du ZombieFamille à sa mort
            if (z instanceof ZombieFamille zf) {
                if (zf.estMort()) {
                    List<Enemie> enfants = zf.genererEnfants(chemin, zf.getEtapeActuelle());
                    zombies.addAll(enfants);
                }
            }

            if (z.estMort() || z.estArrive()) {
                zombies.remove(i);
            }
        }
    }

    private void gererTransitionVague() {
        if (vagueEnCours.get() && zombiesRestantsASpawner == 0 && zombies.isEmpty()) {
            vagueEnCours.set(false);
            tempsAvantProchaineVague = DelaientreVague;
        }

        if (!vagueEnCours.get() && getNumeroVague() > 0) {
            tempsAvantProchaineVague--;
            if (tempsAvantProchaineVague <= 0) {
                preparerNouvelleVague();
            }
        }
    }

    public boolean payerAchat(int montant) {
        if (getArgent() >= montant) {
            argent.set(getArgent() - montant);
            return true;
        }
        return false;
    }

    private void faireApparaitreZombie() {
        Point depart = terrain.trouverEntree();
        int pixelX = (int) (depart.y * TailleCase);
        int pixelY = (int) (depart.x * TailleCase);

        Enemie zombie = creerZombieSelonVague(pixelX, pixelY);
        zombie.setChemin(chemin);
        zombies.add(zombie);
    }

    // Logique d'apparition selon le niveau de difficulté de la vague
    private Enemie creerZombieSelonVague(int x, int y) {
        int vague = getNumeroVague();

        if (vague >= 5) {
            double hasard = Math.random();
            if (hasard < 0.20) return new ZombieGros(x, y);
            if (hasard < 0.40) return new ZombieFamille(x, y);
            if (hasard < 0.70) return new ZombieRapide(x, y);
            return new ZombieNormal(x, y);
        }

        if (vague >= 3) {
            if (zombiesRestantsASpawner % 4 == 0) return new ZombieGros(x, y);
            if (zombiesRestantsASpawner % 3 == 0) return new ZombieRapide(x, y);
            return new ZombieNormal(x, y);
        }

        if (vague >= 2) {
            if (zombiesRestantsASpawner % 3 == 0) return new ZombieRapide(x, y);
            return new ZombieNormal(x, y);
        }

        return new ZombieNormal(x, y);
    }

    // Getters / Setters standard
    public ObservableList<Enemie> getZombies() { return zombies; }
    public ObservableList<Tour> getTours() { return tours; }
    public Terrain getTerrain() { return terrain; }
    public Comptoir getBase() { return base; }
    public int getArgent() { return argent.get(); }
    public IntegerProperty argentProperty() { return argent; }
    public int getNumeroVague() { return numeroVague.get(); }
    public IntegerProperty numeroVagueProperty() { return numeroVague; }
    public BooleanProperty vagueEnCoursProperty() { return vagueEnCours; }
}