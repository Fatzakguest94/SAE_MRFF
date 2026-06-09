package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.List;

public class BacGlace extends Tour {
    public BacGlace(int x, int y) {
        super(x, y, 50, 0, 12, "Bacglacon");
    }

    @Override
    protected void infligerDegats(Enemie cible, List<Enemie> listeZombies) {

        cible.ralentissement();
        System.out.println("Gelée");
    }

}