package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.List;

public class Barbecue extends Tour {
    public Barbecue(int x, int y) {
        super(x, y, 20, 2, 12, "Barbecue");
    }

    @Override
    protected void infligerDegats(Enemie cible, List<Enemie> listeZombies) {
        cible.recevoirDegats(this.degats); // Premier degat
        cible.bruler(); // Zombie en feu
        System.out.println("Sa brule");
    }
}