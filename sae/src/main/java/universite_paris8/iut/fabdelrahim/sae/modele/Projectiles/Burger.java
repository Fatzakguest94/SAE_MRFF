package universite_paris8.iut.fabdelrahim.sae.modele.Projectiles;

import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public class Burger extends Projectile {

    public Burger(double startX, double startY, Enemie cible) {
        super(startX, startY, 5, 15, cible, "Burger");
    }

    @Override
    public void appliquerEffet(Environnement env) {
        System.out.println("Le burger explose à l'impact !");

        // Le centre de l'explosion, c'est là où le projectile s'est écrasé (utilisation des getters)
        double impactX = this.getX();
        double impactY = this.getY();

        // on parcourt TOUS les zombies du jeu pour voir qui est touché par l'explosion
        for (Enemie z : env.getZombies()) {

            if (!z.estMort()) {
                //Calcul de la distance entre l'explosion et ce zombie précis (Théorème de Pythagore)
                double dx = z.getX() - impactX;
                double dy = z.getY() - impactY;
                double distance = Math.sqrt((dx * dx) + (dy * dy));

                // Si le zombie est à moins de 50 pixels de l'explosion, il prend les dégâts
                if (distance <= 50) {
                    z.recevoirDegats(this.degats);
                }
            }
        }

        // On signale que le projectile a fini son travail pour qu'il soit effacé
        this.aTouche = true;
    }
}