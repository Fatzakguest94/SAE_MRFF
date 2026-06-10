package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Environnement {

    // Réglages de la grille et équilibrage du jeu
    private static final int ArgentDepart = 100000;
    private static final int RecompenseParZombie = 10;
    private static final int TailleCase = 36; // En pixels (taille d'une tuile)

    // Gestion du temps (en nombre de "ticks")
    private static final int Delaiavantaparition = 10;
    private static final int DelaientreVague = 60;
    private static final int PAS_LOGIQUE = 5; // Facteur pour réguler la vitesse du jeu

    // Composants du moteur de jeu
    private final Terrain terrain;
    private final List<Point> chemin;
    private final Comptoir base;
    private final ObservableList<Tour> tours;
    private final ObservableList<Enemie> zombies; // Liste observable pour mettre à jour la vue automatiquement

    // Propriétés synchronisées avec l'interface
    private final IntegerProperty argent;
    private final IntegerProperty numeroVague;
    private final BooleanProperty vagueEnCours;

    // Compteurs internes pour la gestion des vagues et du temps
    private int temps;
    private int zombiesRestantsASpawner;
    private int delaiAvantProchainZombie;
    private int tempsAvantProchaineVague;

    public Environnement() {
        this.terrain = new Terrain();
        this.tours = FXCollections.observableArrayList();
        this.zombies = FXCollections.observableArrayList();

        this.argent = new SimpleIntegerProperty(ArgentDepart);
        this.numeroVague = new SimpleIntegerProperty(0);
        this.vagueEnCours = new SimpleBooleanProperty(false);
        this.temps = 0;

        // Calcul du chemin unique entre l'entrée et la sortie grâce au BFS
        Point debut = terrain.trouverEntree();
        Point arrivee = terrain.trouverSortie();
        this.chemin = Bfs.bfs(terrain.grille, debut, arrivee);

        if (chemin.isEmpty()) {
            System.err.println("[Environnement] Aucun chemin trouvé entre l'entrée et la sortie !");
        }

        // Placement du comptoir de la pizzeria sur la case de sortie (conversion en pixels)
        this.base = new Comptoir(
                (int) (arrivee.y * TailleCase),
                (int) (arrivee.x * TailleCase),
                "SuperComptoir"
        );
    }

    // Initialise les variables pour lancer une nouvelle vague
    public void preparerNouvelleVague() {
        numeroVague.set(getNumeroVague() + 1);
        vagueEnCours.set(true);

        // Formule de scaling : +10 zombies par vague
        zombiesRestantsASpawner = 10 * getNumeroVague();
        delaiAvantProchainZombie = 0;
        System.out.println("[Vague " + getNumeroVague() + "] Début — " + zombiesRestantsASpawner + " zombies à spawner.");
    }

    // Tente d'acheter et de placer une tour sur la carte
    public void ajouterTour(int pixelX, int pixelY, String type) {
        int cout = coutTour(type);

        if (!payerAchat(cout)) {
            System.out.println("[Tour] Pas assez de Tickets Resto pour : " + type + " (coût : " + cout + ")");
            return;
        }

        Tour nouvelleTour = creerTour(pixelX, pixelY, type);
        if (nouvelleTour != null) {
            tours.add(nouvelleTour);
            System.out.println("[Tour] " + type + " achetée et placée en (" + pixelX + ", " + pixelY + ").");
        }
    }


    private int coutTour(String type) {
        int cout;

        switch (type) {
            case "LanceBurger":
                cout = 150;
                break;
            case "MitrailletteFrite":
                cout = 100;
                break;
            case "BacGlace":
                cout = 200;
                break;
            case "Barbecue":
                cout = 250;
                break;
            default:
                cout = 100;
                break;
        }

        return cout;
    }

    // Utilisation du switch moderne pour instancier la bonne classe de Tour
    private Tour creerTour(int x, int y, String type) {
        return switch (type) {
            case "LanceBurger" -> new LanceBurger(x, y);
            case "MitrailletteFrite" -> new MitrailletteFrite(x, y);
            case "BacGlace" -> new BacGlace(x, y);
            case "Barbecue" -> new Barbecue(x, y);
            default -> new MitrailletteFrite(x, y);
        };
    }

    // Moteur principal appelé à chaque tick par l'AnimationTimer
    public void unTourDeJeu() {
        temps++;

        // Permet de ne pas exécuter la logique physique à chaque tick d'horloge
        if (temps % PAS_LOGIQUE != 0) {
            return;
        }

        gererSpawn();
        faireAttaquerLesTours();
        mettreAJourZombies();
        gererTransitionVague();
    }

    // Gère le rythme d'apparition des zombies
    private void gererSpawn() {
        if (zombiesRestantsASpawner <= 0) {
            return;
        }
        delaiAvantProchainZombie--;
        if (delaiAvantProchainZombie <= 0) {
            faireApparaitreZombie();
            zombiesRestantsASpawner--;
            delaiAvantProchainZombie = Delaiavantaparition; // Réinitialisation du cooldown
        }
    }

    // Fait tirer toutes les tours posées sur la carte
    private void faireAttaquerLesTours() {
        for (Tour t : tours) {
            t.attaquer(zombies); // La tour cherche sa cible dans la liste observable des zombies
        }
    }

    // Met à jour la position et l'état de santé des zombies
    private void mettreAJourZombies() {
        // Parcours inversé obligatoire pour éviter les bugs d'index lors des suppressions en plein milieu
        for (int i = zombies.size() - 1; i >= 0; i--) {
            Enemie z = zombies.get(i);
            z.avancer();

            // Le zombie a atteint le comptoir
            if (z.estArrive() && base != null) {
                base.recevoirDegats(z.getDegat());
            }

            //Le zombie est mort, on donne la récompense au joueur
            if (z.prendreRecompense()) {
                argent.set(getArgent() + RecompenseParZombie);
            }

            // Cas spécial du ZombieFamille : s'il meurt, on extrait ses enfants et on les ajoute à la liste
            if (z instanceof ZombieFamille) {
                ZombieFamille zf = (ZombieFamille) z; // Cast classique, propre et explicite pour le BUT 1
                if (zf.estMort()) {
                    List<Enemie> enfants = zf.genererEnfants(chemin, zf.getEtapeActuelle());
                    zombies.addAll(enfants);
                }
            }

            // Nettoyage de la liste : on retire les monstres morts ou arrivés au bout
            if (z.estMort() || z.estArrive()) {
                zombies.remove(i);
            }
        }
    }

    // Gère la fin d'une vague et le déclenchement automatique de la suivante
    private void gererTransitionVague() {
        // Si la vague est active mais qu'il n'y a plus aucun zombie en vie ni à spawner
        if (vagueEnCours.get() && zombiesRestantsASpawner == 0 && zombies.isEmpty()) {
            vagueEnCours.set(false);
            tempsAvantProchaineVague = DelaientreVague;
            System.out.println("[Vague " + getNumeroVague() + "] Terminée. Prochaine vague dans " + DelaientreVague + " ticks.");
        }

        // Compte à rebours avant le lancement automatique de la prochaine vague
        if (!vagueEnCours.get() && getNumeroVague() > 0) {
            tempsAvantProchaineVague--;
            if (tempsAvantProchaineVague <= 0) {
                preparerNouvelleVague();
            }
        }
    }

    // Vérifie et déduis l'argent si le solde est suffisant
    public boolean payerAchat(int montant) {
        if (getArgent() >= montant) {
            argent.set(getArgent() - montant);
            return true;
        }
        return false;
    }

    // Crée un zombie au point de départ (en pixels)
    private void faireApparaitreZombie() {
        Point depart = terrain.trouverEntree();
        int pixelX = (int) (depart.y * TailleCase);
        int pixelY = (int) (depart.x * TailleCase);

        Enemie zombie = creerZombieSelonVague(pixelX, pixelY);
        zombie.setChemin(chemin);
        zombies.add(zombie);
    }

    // Algorithme d'apparition des zombies selon le niveau de la vague actuelle
    private Enemie creerZombieSelonVague(int x, int y) {
        int vague = getNumeroVague();

        // Vague 5 et plus : Probabilités aléatoires
        if (vague >= 5) {
            double hasard = Math.random();
            if (hasard < 0.20) return new ZombieGros(x, y);
            if (hasard < 0.40) return new ZombieFamille(x, y);
            if (hasard < 0.70) return new ZombieRapide(x, y);
            return new ZombieNormal(x, y);
        }

        // Vague 3 et 4  Gros et Rapides
        if (vague >= 3) {
            if (zombiesRestantsASpawner % 4 == 0) return new ZombieGros(x, y);
            if (zombiesRestantsASpawner % 3 == 0) return new ZombieRapide(x, y);
            return new ZombieNormal(x, y);
        }

        // Vague 2 zombie rapide ajouter
        if (vague >= 2) {
            if (zombiesRestantsASpawner % 3 == 0) return new ZombieRapide(x, y);
            return new ZombieNormal(x, y);
        }

        // Vague 1 Uniquement des zombies basiques
        return new ZombieNormal(x, y);
    }

    // Getters / Setters et Properties pour les fenêtres JavaFX
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