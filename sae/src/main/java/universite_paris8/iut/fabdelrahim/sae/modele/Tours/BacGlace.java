package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

import java.util.List;

public class BacGlace extends Tour {

    public BacGlace(int x, int y) {
        super(x, y, 50, 0, 12, "BacGlace");
    }

    @Override
    protected void infligerDegats(Enemie cible, List<Enemie> listeZombies) {
        cible.ralentissement();
    }
}