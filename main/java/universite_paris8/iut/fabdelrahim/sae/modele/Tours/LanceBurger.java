package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Burger;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public class LanceBurger extends Tour {

    public LanceBurger(int x, int y) {
        super(x, y, 180, 15, 60, TypeTour.LANCE_BURGER);
    }

    @Override
    protected void gererActionTour(Enemie cible, Environnement env) {
        Burger b = new Burger(this.getX() + 10, this.getY() + 10, cible);
        b.setDegats(this.degats);
        env.getProjectiles().add(b);
    }
}