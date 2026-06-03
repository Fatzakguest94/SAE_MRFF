package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.ArrayList;
import java.util.List;

public class Environnement {

    private Terrain terrain;
    private List<Enemie> zombies;
    private List<Point> chemin;
    private int temps;

    public Environnement() {
        this.terrain = new Terrain();
        this.zombies = new ArrayList<>();
        this.temps = 0;
        this.initialiserChemin();
        this.initialiserZombies(); // C'est ici que la méthode est appelée au lancement !
    }

    private void initialiserChemin() {
        // Calcul le seul chemin du bfs
        this.chemin = Bfs.bfs(
                terrain.grille,
                new Point(12, 0),   // entrée
                new Point(10, 31)   // sortie
        );

        if (this.chemin.isEmpty()) {
            System.out.println("Aucun chemin trouvé dans l'environnement !");
        }
    }

    // TU METS LA MÉTHODE ICI :
    private void initialiserZombies() {
        // La ligne de départ reste la ligne 12
        double startY = 12 * 36;

        
        
        Enemie normal = new Enemie(0, startY, 1.0, "ZombieNormal");       // Part direct, vitesse normale
        Enemie gros   = new Enemie(-45, startY, 0.7, "ZombieGros");      // Pop juste après, avance lentement
        Enemie rapide = new Enemie(-90, startY, 1.8, "ZombieRapide");    // Pop encore après, avance très vite
        Enemie famille= new Enemie(-135, startY, 0.8, "ZombieFamille");  // Ferme la marche

        // Ajout à la liste
        this.zombies.add(normal);
        this.zombies.add(gros);
        this.zombies.add(rapide);
        this.zombies.add(famille);

        // donne le chemin
        for (Enemie z : this.zombies) {
            z.setChemin(this.chemin);
        }
    }

    public void unTourDeJeu() {
        this.temps++;
        // À chaque tour, on demande à TOUS les zombies de la liste d'avancer
        for (Enemie z : zombies) {
            z.avancer();
        }
    }

    // Getters pour que le Controller et la Vue puissent récupérer les infos
    public Terrain getTerrain() {
        return this.terrain;
    }

    public List<Enemie> getZombies() {
        return this.zombies;
    }

    public int getTemps() {
        return this.temps;
    }

    public List<Point> getChemin() {
        return this.chemin;
    }
}
