package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.List;

public class Tour {
    protected int x;
    protected int y;
    protected int portee;
    protected int degats;
    protected int cooldown;
    protected int vitesseTir;
    protected String identite; // Pour savoir si c'est une Frite ou un Burger

    private static int compteur = 0;
    private String idUnique;

    public Tour(int x, int y, int portee, int degats, int vitesseTir, String identite) {
        this.x = x;
        this.y = y;
        this.portee = portee;
        this.degats = degats;
        this.cooldown = 0;
        this.vitesseTir = vitesseTir;
        this.identite = identite;
        this.idUnique = "tour_" + compteur++;
    }

    public void attaquer(List<Enemie> listeZombies) {
        // Si la tour recharge, on diminue le cooldown et on attend
        if (this.cooldown > 0) {
            this.cooldown--;
            return;
        }

        // On parcourt la liste des zombies pour trouver une cible
        for (int i = 0; i < listeZombies.size(); i++) {
            Enemie zombie = listeZombies.get(i);

            if (!zombie.estMort()) {
                //AJUSTEMENT GÉOMÉTRIQUE : TIR DEPUIS LE CENTRE DES CASES
                double centreTourX = this.x + 18;
                double centreTourY = this.y + 18;
                double centreZombieX = zombie.getX() + 18;
                double centreZombieY = zombie.getY() + 18;

                // Calcul de la distance réelle entre les deux centres
                double deltaX = centreTourX - centreZombieX;
                double deltaY = centreTourY - centreZombieY;
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                // Si le zombie est à portée
                if (distance <= this.portee) {
                    // On inflige les dégâts (méthode polymorphe redéfinie dans LanceBurger)
                    this.infligerDegats(zombie, listeZombies);

                    // On active le temps de recharge
                    this.cooldown = this.vitesseTir;

                    // On arrête la recherche : cette tour a fini son action pour ce tour-ci
                    break;
                }
            }
        }
    }

    // Par défaut, une tour normale ne touche qu'un seul zombie
    protected void infligerDegats(Enemie cible, List<Enemie> listeZombies) {
        cible.recevoirDegats(this.degats);
        System.out.println(this.identite + " tire ! HP restants : " + cible.getHp());
    }

    public int getDegats(){return this.degats;}

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public String getIdUnique() { return this.idUnique; }
    public String getIdentite() { return this.identite; }
}