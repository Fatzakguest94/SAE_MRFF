package universite_paris8.iut.fabdelrahim.sae.modele.Projectiles;

import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public class Frites extends Projectile {
    public Frites(double startX, double startY, Enemie cible) {
        super(startX, startY, 9, 4, cible, "Frite"); // Vitesse 9, dégâts 4
    }

    @Override
    public void appliquerEffet(Environnement env) {
        if (this.cible != null && !this.cible.estMort()) {
            this.cible.recevoirDegats(this.degats);
        }
        this.aTouche = true; // Signale la fin de vie du projectile
    }
}