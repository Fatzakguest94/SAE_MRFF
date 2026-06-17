package universite_paris8.iut.fabdelrahim.sae.modele;

public class MitrailletteFrite extends Tour {

    public MitrailletteFrite(int x, int y) {
        // super(x, y, portee, degats, vitesseTir, identite)
        super(x, y, 150, 4, 4, "MitrailletteFrite"); // Portée augmentée à 150
    }
}