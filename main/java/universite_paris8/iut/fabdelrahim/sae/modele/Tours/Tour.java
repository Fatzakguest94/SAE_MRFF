package universite_paris8.iut.fabdelrahim.sae.modele.Tours;


import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.List;


public class Tour {
    protected IntegerProperty x;
    protected IntegerProperty y;
    protected int portee;
    protected int degats;
    protected int cooldown;
    protected int vitesseTir;
    protected TypeTour type;
    protected IntegerProperty niveau;

    private static int compteur = 0;
    private String idUnique;

    public Tour(int x, int y, int portee, int degats, int vitesseTir, TypeTour type) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.portee = portee;
        this.degats = degats;
        this.cooldown = 0;
        this.vitesseTir = vitesseTir;
        this.type = type;
        this.idUnique = "tour_" + compteur++;
        this.niveau = new SimpleIntegerProperty(1);
    }

    // Appliquer l'amélioration des stats (Fusion des deux versions)
    public void ameliorer() {
        this.niveau.set(this.getNiveau() + 1);
        this.degats = (int) (this.degats * 1.5);        
        this.portee = (int) (this.portee * 1.15);       
        this.vitesseTir = (int) (this.vitesseTir * 0.75); 
    }


    public int getPrixAmelioration() {
        // A chaque amélioration, le prix augmente de 100
        return this.type.getPrix() + 100 * getNiveau();
    }

    public void attaquer(Environnement env) {
        // Gestion du temps de recharge
        if (this.cooldown > 0) {
            this.cooldown--;
            return;
        }

        List<Enemie> listeZombies = env.getZombies();

        // Recherche du premier zombie à portée de tir
        for (int i = 0; i < listeZombies.size(); i++) {
            Enemie zombie = listeZombies.get(i);

            if (!zombie.estMort()) {
                // Calcul de la distance entre le centre de la tour (36x36) et le centre du zombie (36x36)
                double centreTourX = this.getX() + 18;
                double centreTourY = this.getY() + 18;
                double centreZombieX = zombie.getX() + 18;
                double centreZombieY = zombie.getY() + 18;

                double deltaX = centreTourX - centreZombieX;
                double deltaY = centreTourY - centreZombieY;
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                // Si la cible est dans le rayon d'action
                if (distance <= this.portee) {
                    // Déclenche l'action de la tour (tir ou effet direct)
                    this.gererActionTour(zombie, env);

                    // Réinitialisation du temps de recharge
                    this.cooldown = this.vitesseTir;

                    // La tour s'arrête pour ce cycle après avoir trouvé sa cible
                    break;
                }
            }
        }
    }

    protected void gererActionTour(Enemie cible, Environnement env) {
        // Par défaut : effet direct (pièges au sol). Les tours à projectiles redéfinissent cette méthode.
        this.appliquerEffetImmediat(cible, env.getZombies());
    }

    // Comportement vide par défaut pour les tours à projectiles classiques
    protected void appliquerEffetImmediat(Enemie cible, List<Enemie> listeZombies) {
    }

    //Getters et Setters

    public int getX() { return this.x.get(); }
    public int getY() { return this.y.get(); }
    public void setX(int x) { this.x.set(x); }
    public void setY(int y) { this.y.set(y); }
    public IntegerProperty xProperty() { return this.x; }
    public IntegerProperty yProperty() { return this.y; }

    public String getIdUnique() { return this.idUnique; }
    public TypeTour getType() { return this.type; }

    public int getNiveau() { return this.niveau.get(); }
    public IntegerProperty niveauProperty() { return this.niveau; }

    // Ajout indispensable pour l'objet Rouleau (bonus de dégâts)
    public int getDegats(){ return this.degats; }
    public void setDegats(int degats){ this.degats = degats; }
}
