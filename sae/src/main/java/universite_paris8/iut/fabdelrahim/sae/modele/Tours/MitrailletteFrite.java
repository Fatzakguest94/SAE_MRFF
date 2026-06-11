package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

/**
 * Représente la tour de défense de base à cadence rapide.
 * Arrose les cibles uniques à l'aide de projectiles Frites à haute vitesse.
 */
public class MitrailletteFrite extends Tour {

    public MitrailletteFrite(int x, int y) {
        // super(x, y, portee, degats, vitesseTir, identite)
        // Portée de 150 pixels, 4 points de dégâts par frite, cadence très élevée (4 tics de recharge)
        super(x, y, 150, 4, 4, "MitrailletteFrite");
    }
}