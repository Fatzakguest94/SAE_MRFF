package universite_paris8.iut.fabdelrahim.sae.modele.Projectiles;

import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public abstract class Projectile {
    protected double x;
    protected double y;
    protected double vitesse;
    protected int degats;
    protected Enemie cible; //Le projectile doit retenir qui il chasse !
    protected String identite;
    protected boolean aTouche; // Pour savoir quand le supprimer
    private static int compteurProj = 0;
    private String idUnique;


    public Projectile(double x, double y, double vitesse, int degats, Enemie cible, String identite) {
        this.x = x;
        this.y = y;
        this.vitesse = vitesse;
        this.degats = degats;
        this.cible = cible;
        this.identite = identite;
        this.aTouche = false;
        this.idUnique = "proj_" + compteurProj++;
    }

    // Méthode abstraite que chaque projectile précis devra coder
    // (ex: LanceBurger fera exploser une zone, Frites fera des dégâts simples)
    public abstract void appliquerEffet(Environnement env);

    public void avancer() {
        // Sécurité : Si on a déjà touché, on ne bouge plus
        if (this.aTouche || this.cible == null) {
            return;
        }

        // 1. On récupère les coordonnées exactes de la cible
        double cibleX = this.cible.getX();
        double cibleY = this.cible.getY();

        // 2. On calcule la différence sur les axes
        double dx = cibleX - this.x;
        double dy = cibleY - this.y;

        // 3. Théorème de Pythagore pour avoir la distance totale
        double distance = Math.sqrt((dx * dx) + (dy * dy));

        // 4. Détection de collision (A-t-on touché la cible ?)
        // Si la distance qui nous sépare est plus petite que notre vitesse,
        // cela veut dire que le prochain pas nous fera rentrer dans le zombie !
        if (distance <= this.vitesse) {
            this.x = cibleX;
            this.y = cibleY;
            this.aTouche = true; // L'impact a eu lieu !
            return;
        }

        // 5. Sinon, on avance d'un pas vers la cible (Normalisation du vecteur)
        // On divise par la distance totale, et on multiplie par notre vitesse
        this.x += (dx / distance) * this.vitesse;
        this.y += (dy / distance) * this.vitesse;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getVitesse() {
        return vitesse;
    }


    public int getDegats() {
        return degats;
    }
    public void setDegats(int nouveauxDegats) {this.degats = nouveauxDegats;}
    public Enemie getCible() {
        return cible;
    }
    public String getIdentite() {
        return identite;
    }
    public boolean isATouche() {
        return aTouche;
    }
    public String getIdUnique() {
        return this.idUnique;
    }
}