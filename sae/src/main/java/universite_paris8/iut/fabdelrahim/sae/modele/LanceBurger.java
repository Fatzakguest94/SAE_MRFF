package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.List;

public class LanceBurger extends Tour {

    private int rayonExplosion = 50; // Rayon des dégâts de zone (en pixels)

    public LanceBurger(int x, int y) {
        // super(x, y, portee, degats, vitesseTir, identite)
        super(x, y, 180, 15, 60, "LanceBurger"); // Portée augmentée à 180 !
    }

    //LE POLYMORPHISME : On réécrit l'attaque pour faire des dégâts de zone
    @Override
    protected void infligerDegats(Enemie cible, List<Enemie> listeZombies) {
        System.out.println("Le Burger explose !");

        // On regarde la position de l'impact (le zombie ciblé)
        double impactX = cible.getX();
        double impactY = cible.getY();

        // On inflige des dégâts à TOUS les zombies proches de l'impact
        for (Enemie z : listeZombies) {
            if (!z.estMort()) {
                double dX = impactX - z.getX();
                double dY = impactY - z.getY();
                double distanceDeLExplosion = Math.sqrt(dX * dX + dY * dY);

                // Si le zombie est dans le rayon de la bombe, il prend
                if (distanceDeLExplosion <= this.rayonExplosion) {
                    z.recevoirDegats(this.degats);
                }
            }
        }
    }
}