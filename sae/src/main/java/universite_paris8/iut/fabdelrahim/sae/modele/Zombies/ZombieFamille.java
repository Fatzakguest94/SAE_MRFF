package universite_paris8.iut.fabdelrahim.sae.modele.Zombies;

import universite_paris8.iut.fabdelrahim.sae.modele.Chemin.Point;

import java.util.ArrayList;
import java.util.List;

public class ZombieFamille extends Enemie {

    private static final int nbEnfants = 4;
    // Flag pour éviter les doubles triggers si la méthode est appelée plusieurs fois par cycle
    private boolean enfantsGeneres;

    public ZombieFamille(int x, int y) {
        super(x, y, 2, 40, 5, "ZombieFamille");
        this.enfantsGeneres = false;
    }


    //Méthode appelée à la mort du zombie pour diviser l'entité en 4 sous-zombies.
    //Les enfants récupèrent le chemin exact et l'état d'avancement (étape) du parent.

    public List<Enemie> genererEnfants(List<Point> chemin, int etapeParent) {
        List<Enemie> enfants = new ArrayList<>();

        // ne spawn rien si le zombie est vivant ou si il a deja eu les enfants a déjà eu lieu
        if (!this.estMort() || this.enfantsGeneres) {
            return enfants;
        }

        this.enfantsGeneres = true;

        // Pattern de dispersion (offset x, y) pour éviter la superposition parfaite des sprites à l'écran
        int[][] decalages = {
                { -8,  -8 },
                {  8,  -8 },
                { -8,   8 },
                {  8,   8 }
        };

        for (int i = 0; i < nbEnfants; i++) {
            ZombieNormal enfant = new ZombieNormal(
                    (int) this.getX() + decalages[i][0],
                    (int) this.getY() + decalages[i][1]
            );

            // évite que les enfants ne re-swawn au point de départ
            enfant.setChemin(chemin);
            enfant.setEtapeActuelle(etapeParent);

            enfants.add(enfant);
        }

        return enfants;
    }
}
