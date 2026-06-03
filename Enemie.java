package universite_paris8.iut.rissamou.sae_td.modele;

import java.util.List;

public class Enemie {

    private int x;
    private int y;
    private double vitesse;
    private int hp;
    private int degat;
    private String identite;
    private List<Point> chemin;
    private int etapeActuelle = 0;

    public Enemie(int x, int y, double vitesse, String identite) {
        this.x = x;
        this.y = y;
        this.vitesse = vitesse;
        this.hp = 20;
        this.degat = 1;
        this.identite = identite;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public int getHp() {
        return hp;
    }
    public double getVitesse() {
        return vitesse;
    } 
    public int getDegat() {
        return degat;
    }

    public int DegatSubi(Enemie enemie,Enemie enemie2){

        int degsubi = enemie2.getDegat();

        this.hp = enemie.getHp() - degsubi;

        return this.hp;
    }
    public String getIdentite() {
        return identite;
    }

    public void setChemin(List<Point> chemin) {
        this.chemin = chemin;
        this.etapeActuelle = 0;
    }

    public void avancer() {
        // 1. Si on a fini le chemin, on arrête la méthode
        if (chemin == null || etapeActuelle >= chemin.size()) {
            return;
        }

        // 2. On calcule la position en pixels de la case visée
        Point caseCible = chemin.get(etapeActuelle);
        int cibleX = caseCible.y * 36;
        int cibleY = caseCible.x * 36;

        // 3. Déplacement horizontal (X)
        if (this.x < cibleX) {
            this.x += vitesse; // Va à droite
        } else if (this.x > cibleX) {
            this.x -= vitesse; // Va à gauche
        }

        // 4. Déplacement vertical (Y)
        if (this.y < cibleY) {
            this.y += vitesse; // Descend
        } else if (this.y > cibleY) {
            this.y -= vitesse; // Monte
        }

        // 5. On passe à la case suivante seulement si on est arrivé pile dessus
        if (this.x == cibleX && this.y == cibleY) {
            etapeActuelle++;
        }
    }
    public boolean spawn(Enemie e, int [][]grille){
        boolean spawn = true;
        if(grille[this.x][this.y] != 3) {
            spawn = false;
        }
        return spawn;
    }

    public boolean estMort() {
        // <= 0 est plus sûr que == 0 (au cas où une tour fait beaucoup de dégâts d'un coup)
        return this.hp <= 0;
    }

    public boolean estArrive() {
        // Il est arrivé si son chemin n'est pas vide ET qu'il a atteint la dernière étape
        return chemin != null && etapeActuelle >= chemin.size();
    }
}
