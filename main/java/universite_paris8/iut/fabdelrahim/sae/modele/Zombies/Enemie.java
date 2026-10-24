package universite_paris8.iut.fabdelrahim.sae.modele.Zombies;

import universite_paris8.iut.fabdelrahim.sae.modele.Chemin.Point;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.util.List;

public class Enemie {

    private static final int TailleCase = 36;
    private static int compteur = 0;

    private final String idUnique;
    private final String identite;

    private double vitesse;
    private final int degat;
    private int hp;

    private DoubleProperty x;
    private DoubleProperty y;

    private List<Point> chemin;
    private int etapeActuelle;

    private boolean recompenseDonnee;

    private int brulure = 0;
    private boolean ralenti = false;

    public Enemie(int x, int y, double vitesse, int hp, int degat, String identite) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.vitesse = vitesse;
        this.hp = hp;
        this.degat = degat;
        this.identite = identite;

        this.etapeActuelle = 0;
        this.recompenseDonnee = false;

        this.idUnique = identite.toLowerCase() + "_" + compteur++;
    }

    public void avancer() {
        if (chemin == null || etapeActuelle >= chemin.size()) return;

        Point cible = chemin.get(etapeActuelle);

        double cibleX = cible.y * TailleCase;
        double cibleY = cible.x * TailleCase;

        double vitesseEffective = vitesse;

        if (ralenti) {
            vitesseEffective = vitesseEffective / 2;
        }

        this.x.set(approcherValeur(getX(), cibleX, vitesseEffective));
        this.y.set(approcherValeur(getY(), cibleY, vitesseEffective));

        if (getX() == cibleX && getY() == cibleY) {
            etapeActuelle++;
        }

        if (brulure > 0) {
            recevoirDegats(2);
            brulure--;
        }
    }

    private double approcherValeur(double valeur, double cible, double pas) {
        if (valeur < cible) return Math.min(valeur + pas, cible);
        if (valeur > cible) return Math.max(valeur - pas, cible);
        return valeur;
    }

    public void bruler() {
        this.brulure += 5;
    }

    public void ralentissement() {
        if (!this.ralenti) {
            this.ralenti = true;
        }
    }

    public void recevoirDegats(int degatsSubis) {
        hp = Math.max(0, hp - degatsSubis);
    }

    public boolean estMort() {
        return hp <= 0;
    }

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

    public boolean estArrive() {
        return chemin != null && etapeActuelle >= chemin.size();
    }

    public double getX() { return this.x.get(); }
    public double getY() { return this.y.get(); }
    public void setX(double x) { this.x.set(x); }
    public void setY(double y) { this.y.set(y); }
    public DoubleProperty xProperty() { return this.x; }
    public DoubleProperty yProperty() { return this.y; }

    public int getHp() { return hp; }
    public int getDegat() { return degat; }
    public double getVitesse() { return vitesse; }
    public String getIdentite() { return identite; }
    public String getIdUnique() { return idUnique; }
    public int getEtapeActuelle() { return etapeActuelle; }
    public void setEtapeActuelle(int etapeActuelle) {this.etapeActuelle = etapeActuelle;}
    public List<Point> getChemin() { return chemin; }
}