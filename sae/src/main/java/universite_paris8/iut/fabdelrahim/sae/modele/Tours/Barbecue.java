package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import java.util.List;

public class Barbecue extends Tour {

    public Barbecue(int x, int y) {
        super(x, y, 20, 1, 12, "Barbecue");
    }

    @Override
    protected void appliquerEffetImmediat(Enemie cible, List<Enemie> listeZombies) {
        System.out.println("Le Barbecue brûle les zombies à proximité !");

        // Effet de zone : brûle et blesse tous les zombies sur le barbecue
        for (Enemie z : listeZombies) {
            if (!z.estMort()) {
                // Utilisation de getX() et getY() pour extraire la valeur de la Property
                double dx = z.getX() - this.getX();
                double dy = z.getY() - this.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= this.portee) {
                    z.recevoirDegats(this.degats);
                    z.bruler(); // Applique ton effet de brûlure (dégâts par seconde si tu l'as codé)
                }
            }
        }
    }
}