package universite_paris8.iut.fabdelrahim.sae.modele;


import java.util.List;

public class Enemie {

    // Coordonnées en double pour permettre des déplacements précis avec la vitesse
    private double x;
    private double y;
    private double vitesse;
    private int hp;
    private int degat;
    private String identite;

    private List<Point> chemin;
    private int etapeActuelle;
    private boolean recompenseDonnee;
    private static int compteur = 0; // Pour l'id unique demandé par le prof
    private String idUnique;

    public Enemie(int x, int y, double vitesse, String identite) {
        this.x = x;
        this.y = y;
        this.vitesse = vitesse;
        this.identite = identite;
        this.etapeActuelle = 0;
        this.recompenseDonnee = false;

        // Génération automatique de l'ID unique pour la Vue
        this.idUnique = "zombie_" + compteur++;

        //CONFIGURATION DES HP ET DÉGÂTS SELON LE TYPE DE ZOMBIE
        if (identite.equals("ZombieRapide")) {
            this.hp = 15;      // Moins de vie car il va vite
            this.degat = 3;
        }
        else if (identite.equals("ZombieGros")) {
            this.hp = 70;      // Beaucoup de vie car il est gros
            this.degat = 10;    // Fait plus de dégâts s'il arrive au comptoir
        }
        else if (identite.equals("ZombieFamille")) {
            this.hp = 35;
            this.degat = 5;
        }
        else { // "ZombieNormal" (comportement par défaut)
            this.hp = 20;
            this.degat = 2;
        }
    }


    public void recevoirDegats(int degatprit) {
        this.hp -= degatprit;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }


    public boolean prendreRecompense() {
        if (this.estMort() && !this.recompenseDonnee) {
            this.recompenseDonnee = true;
            return true; // Donne la récompense
        }
        return false; // Déjà donnée ou pas encore mort
    }

    public void avancer() {
        // Si pas de chemin ou si on est arrivé au bout, on s'arrête
        if (this.chemin == null || this.etapeActuelle >= this.chemin.size()) {
            return;
        }

        // Récupération de la case cible actuelle
        Point caseCible = this.chemin.get(this.etapeActuelle);


        int cibleX = caseCible.y * 36; // Utilise .y (colonne) pour l'axe X global
        int sizeY = caseCible.x * 36;  // Utilise .x (ligne) pour l'axe Y global

        // Déplacement fluide vers la cible sur l'axe X
        if (this.x < cibleX) {
            this.x += this.vitesse;
            if (this.x > cibleX) this.x = cibleX;
        } else if (this.x > cibleX) {
            this.x -= this.vitesse;
            if (this.x < cibleX) this.x = cibleX;
        }

        // Déplacement fluide vers la cible sur l'axe Y
        if (this.y < sizeY) {
            this.y += this.vitesse;
            if (this.y > sizeY) this.y = sizeY;
        } else if (this.y > sizeY) {
            this.y -= this.vitesse;
            if (this.y < sizeY) this.y = sizeY;
        }

        // On passe à la case suivante dès qu'on a atteint le pixel de destination
        if (this.x == cibleX && this.y == sizeY) {
            this.etapeActuelle++;
        }
    }

    public boolean estMort() {
        return this.hp <= 0;
    }

    public boolean estArrive() {
        return this.chemin != null && this.etapeActuelle >= this.chemin.size();
    }

    public void setChemin(List<Point> chemin) {
        this.chemin = chemin;
        this.etapeActuelle = 0;
    }

    public int getX() { return (int) this.x; }
    public int getY() { return (int) this.y; }
    public int getHp() { return this.hp; }
    public double getVitesse() { return this.vitesse; }
    public int getDegat() { return this.degat; }
    public String getIdentite() { return this.identite; }
    public String getIdUnique() {return this.idUnique; }

}