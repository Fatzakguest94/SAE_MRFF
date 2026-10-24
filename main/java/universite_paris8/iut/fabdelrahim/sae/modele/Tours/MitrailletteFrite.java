package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Frites;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public class MitrailletteFrite extends Tour {

    public MitrailletteFrite(int x, int y) {
        super(x, y, 150, 4, 4, TypeTour.MITRAILLETTE_FRITE);
    }

    @Override
    protected void gererActionTour(Enemie cible, Environnement env) {
        Frites f = new Frites(this.getX() + 10, this.getY() + 10, cible);
        f.setDegats(this.degats);
        env.getProjectiles().add(f);
    }
}