package universite_paris8.iut.fabdelrahim.sae.modele;


import java.util.ArrayList;
import java.util.List;

public class Environnement {

    private Terrain terrain;
    private List<Enemie> zombies;
    private List<Point> chemin;
    private Comptoir base;

    private int temps;
    private int argent;
    private int numeroVague;
    private int zombiesAFaireApparaitre;
    private int delaiAvantProchainZombie;
    private boolean vagueEnCours;
    private int tempsAvantProchaineVague;

    public Environnement() {
        this.terrain = new Terrain();
        this.zombies = new ArrayList<Enemie>();
        this.temps = 0;
        this.argent = 200; // Argent de départ
        this.numeroVague = 0;
        this.vagueEnCours = false;

        this.initialiserCheminAndBase();
    }

    private void initialiserCheminAndBase() {
        Point depart = this.terrain.trouverEntree();
        Point arrivee = this.terrain.trouverSortie();

        // Calcul du chemin avec le BFS
        this.chemin = Bfs.bfs(this.terrain.grille, depart, arrivee);

        if (this.chemin.isEmpty()) {
            System.out.println("Aucun chemin trouvé !");
        }

        // CORRECTION : arrivee.y (colonne) = X, arrivee.x (ligne) = Y
        this.base = new Comptoir((int)(arrivee.y * 36), (int)(arrivee.x * 36), "Comptoir");
    }

    public void preparerNouvelleVague() {
        this.numeroVague++;
        this.vagueEnCours = true;
        // Exemple simple : 5 zombies vague 1, 10 vague 2...
        this.zombiesAFaireApparaitre = 5 * this.numeroVague;
        this.delaiAvantProchainZombie = 0;
    }

    public void faireApparaitreZombie() {
        Point depart = this.terrain.trouverEntree();

        String typeZombie = "ZombieNormal";
        double vitesse = 2;

        if (this.zombiesAFaireApparaitre % 3 == 0) {
            typeZombie = "ZombieRapide";
            vitesse = 4;
        }


        Enemie zombie = new Enemie((int)(depart.y * 36), (int)(depart.x * 36), vitesse, typeZombie);
        zombie.setChemin(this.chemin);
        this.zombies.add(zombie);
    }

    public void unTourDeJeu() {
        this.temps++;

        // Ralentisseur de logique (comme ton temps % 5 == 0 de base)
        if (this.temps % 5 == 0) {

            // 1. Apparition des zombies un par un
            if (this.zombiesAFaireApparaitre > 0) {
                this.delaiAvantProchainZombie--;
                if (this.delaiAvantProchainZombie <= 0) {
                    this.faireApparaitreZombie();
                    this.zombiesAFaireApparaitre--;
                    this.delaiAvantProchainZombie = 10; // Temps d'attente avant le prochain
                }
            }

            // 2. Déplacement et vérification des dégâts / récompenses
            for (int i = 0; i < this.zombies.size(); i++) {
                Enemie z = this.zombies.get(i);
                z.avancer();

                // Si le zombie touche le comptoir
                if (z.estArrive() && this.base != null) {
                    this.base.recevoirDegats(10);
                }

                // Si le zombie vient de mourir, on récupère les sous
                if (z.prendreRecompense()) {
                    this.argent += 50;
                }
            }

            // 3. Nettoyage de la liste (on retire les morts ou arrivés)
            List<Enemie> poubelle = new ArrayList<Enemie>();
            for (int i = 0; i < this.zombies.size(); i++) {
                Enemie z = this.zombies.get(i);
                if (z.estMort() || z.estArrive()) {
                    poubelle.add(z);
                }
            }
            // On applique le retrait de la liste logique
            this.zombies.removeAll(poubelle);

            // 4. Gestion des transitions de vagues
            if (this.vagueEnCours && this.zombiesAFaireApparaitre == 0 && this.zombies.isEmpty()) {
                this.vagueEnCours = false;
                this.tempsAvantProchaineVague = 60; // Pause avant la suite
            }

            // 5. Lancement auto de la vague suivante après la pause
            if (!this.vagueEnCours && this.numeroVague > 0) {
                this.tempsAvantProchaineVague--;
                if (this.tempsAvantProchaineVague <= 0) {
                    this.preparerNouvelleVague();
                }
            }
        }
    }

    public boolean payerAchat(int montant) {
        if (this.argent >= montant) {
            this.argent -= montant;
            return true;
        }
        return false;
    }

    // Getters simples pour le Controller et la Vue
    public Terrain getTerrain() { return this.terrain; }
    public List<Enemie> getZombies() { return this.zombies; }
    public int getArgent() { return this.argent; }
    public int getNumeroVague() { return this.numeroVague; }
    public Comptoir getBase() { return this.base; }
}
