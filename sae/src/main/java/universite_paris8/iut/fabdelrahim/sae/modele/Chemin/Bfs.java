package universite_paris8.iut.fabdelrahim.sae.modele.Chemin;

import java.util.*;

public class Bfs {

    private static final int[][] direction = {
            {1, 0}, //bas
            {-1, 0},//haut
            {0, 1},//droite
            {0, -1}//gauche
    };

    public static List<Point> bfs(int[][] grille, Point debut, Point fin) {
        //la je reprend juste les valeur de la grille colonnes et lignes
        int rows = grille.length;
        int cols = grille[0].length;
        //ok la je fait un tableau visted et caseavant
        boolean[][] visited = new boolean[rows][cols];
        Point[][] caseavant = new Point[rows][cols];
        // sa c'est pour faire une liste genre un ordre
        Queue<Point> queue = new LinkedList<>();

        queue.add(debut);
        visited[debut.x][debut.y] = true;

        while (!queue.isEmpty()) {
            //ici c'est pour enlever les cordonner que je vient de mettre
            Point p = queue.poll();

            // arrivée
            if (p.x == fin.x && p.y == fin.y) {
                return reconstruireChemin(caseavant, fin);
            }

            for (int[] d : direction) {

                int nx = p.x + d[0];
                int ny = p.y + d[1];
                // ici je verfie que je suis bien dans le terrain et que je peut traverser ce point et je l'ajoute a la queu
                if (nx >= 0 &&
                        ny >= 0 &&
                        nx < rows &&
                        ny < cols &&
                        !visited[nx][ny] &&
                        estTraversable(grille[nx][ny])) {

                    queue.add(new Point(nx, ny));

                    visited[nx][ny] = true;

                    caseavant[nx][ny] = p;
                }
            }
        }

        return new ArrayList<>();
    }

    private static boolean estTraversable(int valeur) {

        return valeur == 1 ||
                valeur == 100 ||
                valeur == 3 ||
                valeur == 4;
    }

    private static List<Point> reconstruireChemin(Point[][] parent, Point end) {

        List<Point> chemin = new ArrayList<>();

        Point current = end;
        //on parcourt tout le chemain a l'envers
        while (current != null) {

            chemin.add(current);

            current = parent[current.x][current.y];
        }
        //on inverse le chemin pour commencer du debut
        Collections.reverse(chemin);

        return chemin;
    }
}