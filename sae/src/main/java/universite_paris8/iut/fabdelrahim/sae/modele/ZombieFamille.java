package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.ArrayList;
import java.util.List;

public class ZombieFamille extends Enemie {

    // Nombre de sous-zombies (enfants) à spawn au moment de la mort du parent
    private static final int NB_ENFANTS = 4;

    // Flag pour éviter les doubles triggers si la méthode est appelée plusieurs fois par cycle
    private boolean enfantsGeneres;

    public ZombieFamille(int x, int y) {
        // Init du boss/parent : vitesse=2.0, hp=35, degat=5
        super(x, y, 2.0, 35, 5, "ZombieFamille");
        this.enfantsGeneres = false;
    }


     //Méthode appelée à la mort du zombie pour diviser l'entité en 4 sous-zombies.
     //Les enfants récupèrent le chemin exact et l'état d'avancement (étape) du parent.

    public List<Enemie> genererEnfants(List<Point> chemin, int etapeParent) {
        List<Enemie> enfants = new ArrayList<>();

        // ne spawn rien si le zombie est vivant ou si le split a déjà eu lieu
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

        for (int i = 0; i < NB_ENFANTS; i++) {
            ZombieNormal enfant = new ZombieNormal(
                    this.getX() + decalages[i][0],
                    this.getY() + decalages[i][1]
            );

            // Transfert de la navigation : évite que les enfants ne re-swawn au point de départ (bug du chemin initial)
            enfant.setChemin(chemin);
            enfant.setEtapeActuelle(etapeParent);

            enfants.add(enfant);
        }

        return enfants;
    }

    public boolean isEnfantsGeneres() {
        return this.enfantsGeneres;
    }
}