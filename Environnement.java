package universite_paris8.iut.fabdelrahim.sae.controller;

import universite_paris8.iut.fabdelrahim.sae.modele.Point;
import universite_paris8.iut.fabdelrahim.sae.modele.Terrain;
import java.util.ArrayList;
import java.util.List;

public class Environnement {

    private Terrain terrain;
    private List<Zombie> zombies;
    private List<Point> chemin;
    private int temps;

    public Environnement() {
        this.terrain = new Terrain();
        this.zombies = new ArrayList<>();
        this.temps = 0;
        this.initialiserChemin();
        this.initialiserZombies();
    }

    private void initialiserChemin() {
        // Calcul du chemin unique avec le bfs
        this.chemin = Bfs.bfs(
                terrain.grille,
                new Point(12, 0),   // entrée
                new Point(10, 31)   // sortie
        );

        if (this.chemin.isEmpty()) {
            System.out.println("Aucun chemin trouvé dans l'environnement !");
        }
    }

    private void initialiserZombies() {
        // On fait 4 zombies et on leur donne le chemin
        for (int i = 0; i < 4; i++) {
            Zombie z = new Zombie();
            z.setChemin(this.chemin);
            this.zombies.add(z);
        }
    }


    public void unTourDeJeu() {
        this.temps++;
    }


    public Terrain getTerrain() {
        return this.terrain;
    }

    public List<Zombie> getZombies() {
        return this.zombies;
    }

    public int getTemps() {
        return this.temps;
    }

    public List<Point> getChemin() {
        return this.chemin;
    }
}
