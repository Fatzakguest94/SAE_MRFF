package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

/**
 * Représente l'artillerie lourde de la pizzeria.
 * Cette tour possède une longue portée et génère de lourds dégâts de zone (AoE).
 * Note : La gestion de l'explosion est gérée à l'impact par l'entité du projectile Burger.
 */
public class LanceBurger extends Tour {

    public LanceBurger(int x, int y) {
        // super(x, y, portee, degats, vitesseTir, identite)
        // Portée de 180 pixels, 15 points de dégâts, cadence lente (60 tics de recharge)
        super(x, y, 180, 15, 60, "LanceBurger");
    }

    // L'ancienne méthode infligerDegats() a été supprimée.
    // C'est maintenant le projectile "Burger.java" qui applique son effet d'explosion
    // dans sa méthode "appliquerEffet(Environnement env)" lors de sa collision avec le zombie.
}