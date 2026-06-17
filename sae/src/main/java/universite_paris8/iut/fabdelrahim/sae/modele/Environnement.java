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
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.*;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.*;

public class Environnement {

    private static final int ArgentDepart = 150;
    private static final int RecompenseParZombie = 10;
    private static final int TailleCase = 36;

    private static final int Delaiavantaparition = 10;
    private static final int DelaientreVague = 60;
    private static final int PAS_LOGIQUE = 3;
    private static final int NombreVaguesMax = 10;

    private final Terrain terrain;
    private final List<Point> chemin;
    private final Comptoir base;
    private final ObservableList<Tour> tours;
    private final ObservableList<Enemie> zombies;
    private final ObservableList<Projectile> projectiles;

    private final IntegerProperty argent;
    private final IntegerProperty numeroVague;
    private final BooleanProperty vagueEnCours;

    private final IntegerProperty tempsbonus;

    private int temps;
    private int zombiesRestantsASpawner;
    private int delaiAvantProchainZombie;
    private int tempsAvantProchaineVague;
    private int compteurToursSurChemin = 0;

    private Objet objdrop = null;

    public Environnement() {
        this.terrain = new Terrain();
        this.tours = FXCollections.observableArrayList();
        this.zombies = FXCollections.observableArrayList();
        this.projectiles = FXCollections.observableArrayList();

        this.argent = new SimpleIntegerProperty(ArgentDepart);
        this.numeroVague = new SimpleIntegerProperty(0);
        this.vagueEnCours = new SimpleBooleanProperty(false);
        this.tempsbonus = new SimpleIntegerProperty(0);
        this.temps = 0;

        Point debut = terrain.trouverEntree();
        Point arrivee = terrain.trouverSortie();
        this.chemin = Bfs.bfs(terrain.grille, debut, arrivee);

        if (chemin.isEmpty()) {
            System.err.println("Aucun chemin valide trouvé !");
        }

        this.base = new Comptoir(
                (int) (arrivee.y * TailleCase),
                (int) (arrivee.x * TailleCase),
                "SuperComptoir"
        );
    }

    public void preparerNouvelleVague() {
        numeroVague.set(getNumeroVague() + 1);
        vagueEnCours.set(true);
        if (getNumeroVague() < NombreVaguesMax) {
            zombiesRestantsASpawner = 10 * getNumeroVague();
            delaiAvantProchainZombie = 0;
        } else {
            zombiesRestantsASpawner = 10 * (getNumeroVague() / 2);
        }
    }

    public void ajouterTour(int pixelX, int pixelY, String type) {
        int ligne = pixelY / TailleCase;
        int colonne = pixelX / TailleCase;
        int idTuile = terrain.grille[ligne][colonne];

        for (Tour t : tours) {
            if (t.getX() == pixelX && t.getY() == pixelY) {
                return;
            }
        }

        boolean estTourSpeciale = type.equals("BacGlace") || type.equals("Barbecue");

        if (estTourSpeciale) {
            // Les tours de type zone/ralentissement se placent sur le chemin (id 1 ou 100), max 6 au total
            if (idTuile == 1 || idTuile == 100) {
                if (compteurToursSurChemin >= 6) return;
            } else if (idTuile != 0) {
                return;
            }
        } else {
            if (idTuile != 0) return;
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
            case "LanceBurger": return 200;
            case "MitrailletteFrite": return 100;
            case "BacGlace": return 150;
            case "Barbecue": return 250;
            default: return 100;
        }
    }

    private Tour creerTour(int x, int y, String type) {
        switch (type) {
            case "LanceBurger": return new LanceBurger(x, y);
            case "MitrailletteFrite": return new MitrailletteFrite(x, y);
            case "BacGlace": return new BacGlace(x, y);
            case "Barbecue": return new Barbecue(x, y);
            default: return new MitrailletteFrite(x, y);
        }
    }

    public void unTourDeJeu() {
        temps++;
        // Permet d'alléger les calculs en limitant la mise à jour logique à une frame sur trois
        if (temps % PAS_LOGIQUE != 0) return;

        gererAparition();
        faireAttaquerLesTours();
        mettreAJourProjectiles();
        mettreAJourZombies();
        gererTransitionVague();

        if (objdrop != null) {
            objdrop.agir();
        }
    }

    private void gererAparition() {
        if (zombiesRestantsASpawner <= 0) return;

        delaiAvantProchainZombie--;
        if (delaiAvantProchainZombie <= 0) {
            faireApparaitreZombie();
            zombiesRestantsASpawner--;
            delaiAvantProchainZombie = Delaiavantaparition + 8;
        }
    }

    private void faireAttaquerLesTours() {
        for (Tour t : tours) {
            t.attaquer(this);
        }
    }

    private void mettreAJourProjectiles() {
        // Parcours inversé indispensable pour éviter les erreurs de modification de liste pendant la suppression
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile p = projectiles.get(i);
            p.avancer();

            if (p.isATouche() || p.getCible() == null || p.getCible().estMort()) {
                if (p.isATouche()) {
                    p.appliquerEffet(this);
                }
                projectiles.remove(i);
            }
        }
    }

    private void mettreAJourZombies() {
        // Boucle à rebours pour nettoyer la liste et injecter des enfants à la volée sans casser l'index
        for (int i = zombies.size() - 1; i >= 0; i--) {
            Enemie z = zombies.get(i);
            z.avancer();

            if (z.estArrive() && base != null) {
                base.recevoirDegats(z.getDegat());
            }

            if (z.prendreRecompense()) {
                argent.set(getArgent() + RecompenseParZombie);
            }

            if (z instanceof ZombieFamille zf) {
                if (zf.estMort()) {
                    // Les sous-unités apparaissent sur la case exacte du parent pour suivre la suite du chemin
                    List<Enemie> enfants = zf.genererEnfants(chemin, zf.getEtapeActuelle());
                    zombies.addAll(enfants);
                }
            }

            if (z.estMort() && this.objdrop == null && Objet.lacher()) {
                this.objdrop = new Objet(z.getX(), z.getY(), this);
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

    // Algorithme de sélection probabiliste et cyclique des types d'ennemis selon la difficulté
    private Enemie creerZombieSelonVague(int x, int y) {
        int vague = getNumeroVague();
        double hasard = Math.random();

        if (vague == NombreVaguesMax) {
            if (zombiesRestantsASpawner == 25) return new Boss(x, y);
            if (hasard < 0.20) return new ZombieGros(x, y);
            if (hasard < 0.40) return new ZombieFamille(x, y);
            if (hasard < 0.50) return new ZombieRapide(x, y);
            if (hasard < 0.55) return new ZombieNormal(x, y);
        }

        if (vague >= 5) {
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

    public void vendreTour(int pixelX, int pixelY) {
        Tour tourATrouver = null;
        for (Tour t : tours) {
            if (t.getX() == pixelX && t.getY() == pixelY) {
                tourATrouver = t;
                break;
            }
        }

        if (tourATrouver != null) {
            String type = tourATrouver.getIdentite();
            int prixDeBase = coutTour(type);
            int remboursement = prixDeBase / 2;

            argent.set(getArgent() + remboursement);

            int ligne = pixelY / TailleCase;
            int colonne = pixelX / TailleCase;
            int idTuile = terrain.grille[ligne][colonne];
            if (idTuile == 1 || idTuile == 100) {
                compteurToursSurChemin--;
            }

            tours.remove(tourATrouver);
            System.out.println("Tour vendue ! Recrédité de : " + remboursement + " Tickets.");
        }
    }

    public void ameliorerTour(int pixelX, int pixelY) {
        Tour tourATrouver = null;
        for (Tour t : tours) {
            if (t.getX() == pixelX && t.getY() == pixelY) {
                tourATrouver = t;
                break;
            }
        }

        if (tourATrouver != null) {
            int cout = tourATrouver.getPrixAmelioration();

            if (payerAchat(cout)) {
                tourATrouver.ameliorer();
                System.out.println("Tour améliore au niveau " + tourATrouver.getNiveau() + " ! Cost: " + cout);
            } else {
                System.out.println("Pas assez d'argent pour améliorer cette tour !");
            }
        }
    }

    public boolean toutesVaguesTerminees() {
        return getNumeroVague() >= NombreVaguesMax && !vagueEnCours.get() && zombies.isEmpty();
    }

    public ObservableList<Enemie> getZombies() { return zombies; }
    public ObservableList<Tour> getTours() { return tours; }
    public ObservableList<Projectile> getProjectiles() { return this.projectiles; }
    public Terrain getTerrain() { return terrain; }
    public Comptoir getBase() { return base; }

    public int getArgent() { return argent.get(); }
    public IntegerProperty argentProperty() { return argent; }

    public int getNumeroVague() { return numeroVague.get(); }
    public IntegerProperty numeroVagueProperty() { return numeroVague; }
    public BooleanProperty vagueEnCoursProperty() { return vagueEnCours; }

    public Objet getObjdrop() { return this.objdrop; }

    public IntegerProperty tempsbonusProperty() { return this.tempsbonus; }
    public int getTempsbonus() { return this.tempsbonus.get(); }
    public void setTempsbonus(int temps) { this.tempsbonus.set(temps); }
}
