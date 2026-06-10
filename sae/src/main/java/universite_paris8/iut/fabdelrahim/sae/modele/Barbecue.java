package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.List;

public class Barbecue extends Tour {

    public Barbecue(int x, int y) {
        super(x, y, 20, 1, 12, "Barbecue");
    }

    @Override
    protected void infligerDegats(Enemie cible, List<Enemie> listeZombies) {
        cible.recevoirDegats(this.degats);
        cible.bruler();
    }
}