package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.List;

public class Tour {
    private int x; // Position X en pixels
    private int y; // Position Y en pixels
    private int portee; // Rayon d'attaque en pixels
    private int degats;
    private int cooldown; // Temps restant avant le prochain tir
    private int vitesseTir; // Délai d'attente imposé entre deux tirs
    private static int compteur = 0;
    private String idUnique;

    public Tour(int x, int y) {
        this.x = x;
        this.y = y;
        this.portee = 120;     // Portée de la tour (ex: environ 3-4 cases autour)
        this.degats = 5;       // Dégâts infligés par tir
        this.cooldown = 0;
        this.vitesseTir = 5;   // Attaque tous les 5 cycles de logique
        this.idUnique = "tour_" + compteur++;
    }

    // Méthode appelée à chaque tour de jeu pour chercher un zombie à attaquer
    public void attaquer(List<Enemie> listeZombies) {
        // Si la tour recharge, on diminue le compteur et on s'arrête là
        if (this.cooldown > 0) {
            this.cooldown--;
            return;
        }

        // On parcourt la liste des zombies pour en trouver un à portée
        for (int i = 0; i < listeZombies.size(); i++) {
            Enemie zombie = listeZombies.get(i);

            if (!zombie.estMort()) {
                // Calcul de la distance géométrique entre la tour et le zombie (Théorème de Pythagore)
                double deltaX = this.x - zombie.getX();
                double deltaY = this.y - zombie.getY();
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                // Si le zombie est assez proche, on l'attaque !
                if (distance <= this.portee) {
                    zombie.recevoirDegats(this.degats);
                    this.cooldown = this.vitesseTir; // La tour doit maintenant recharger
                    System.out.println("Une tour tire sur un zombie ! HP restants : " + zombie.getHp());
                    break; // On sort de la boucle pour ne cibler qu'un seul zombie à la fois
                }
            }
        }
    }

    // Getters simples pour la vue
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public String getIdUnique() {return this.idUnique;}
}