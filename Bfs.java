package universite_paris8.iut.fabdelrahim.sae.controller;

import universite_paris8.iut.fabdelrahim.sae.modele.Point;

import java.util.*;

public class Bfs {

    private static final int[][] direction = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };

    public static List<Point> bfs(int[][] grille, Point debut, Point fin) {

        int rows = grille.length;
        int cols = grille[0].length;

        boolean[][] visited = new boolean[rows][cols];
        Point[][] caseavant = new Point[rows][cols];

        Queue<Point> queue = new LinkedList<>();

        queue.add(debut);
        visited[debut.x][debut.y] = true;

        while (!queue.isEmpty()) {

            Point p = queue.poll();

            // arrivée
            if (p.x == fin.x && p.y == fin.y) {
                return reconstruireChemin(caseavant, fin);
            }

            for (int[] d : direction) {

                int nx = p.x + d[0];
                int ny = p.y + d[1];

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
                valeur == 3 ||
                valeur == 4;
    }

    private static List<Point> reconstruireChemin(Point[][] parent, Point end) {

        List<Point> chemin = new ArrayList<>();

        Point current = end;

        while (current != null) {

            chemin.add(current);

            current = parent[current.x][current.y];
        }

        Collections.reverse(chemin);

        return chemin;
    }
}
