package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.List;

public class Enemie {
    // PASSAGE EN DOUBLE ICI
    private double x;
    private double y;
    private double vitesse;

    private int hp;
    private int degat;
    private String identite;
    private List<Point> chemin;
    private int etapeActuelle = 0;
    private boolean recompenseDonnee = false;


    public Enemie(double x, double y, double vitesse, String identite) {
        this.x = x;
        this.y = y;
        this.vitesse = vitesse;
        this.hp = 20;
        this.degat = 1;
        this.identite = identite;
    }


    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public int getHp() { return this.hp; }
    public String getIdentite() { return this.identite; }

    public void subirDegat(int montant) {
        this.hp -= montant;
        if (this.hp < 0) this.hp = 0;
    }

    public boolean estMort() { return this.hp <= 0; }

    public boolean prendreRecompense() {
        if (estMort() && !recompenseDonnee) {
            recompenseDonnee = true;
            return true;
        }
        return false;
    }

    public void setChemin(List<Point> chemin) {
        this.chemin = chemin;
        this.etapeActuelle = 0;
    }

    public void avancer() {
        if (chemin == null || etapeActuelle >= chemin.size() || estMort()) {
            return;
        }

        Point caseCible = chemin.get(etapeActuelle);
        double cibleX = caseCible.y * 36;
        double cibleY = caseCible.x * 36;

        // Déplacement horizontal
        if (this.x < cibleX) this.x += Math.min(vitesse, cibleX - this.x);
        else if (this.x > cibleX) this.x -= Math.min(vitesse, this.x - cibleX);

        // Déplacement vertical
        if (this.y < cibleY) this.y += Math.min(vitesse, cibleY - this.y);
        else if (this.y > cibleY) this.y -= Math.min(vitesse, this.y - cibleY);

        // Astuce BUT 1 pour les doubles : si l'écart est inférieur à 0.01, on considère qu'on est arrivé
        if (Math.abs(this.x - cibleX) < 0.01 && Math.abs(this.y - cibleY) < 0.01) {
            this.x = cibleX; // On recale parfaitement sur le pixel pile
            this.y = cibleY;
            etapeActuelle++; // Case suivante !
        }
    }
}
