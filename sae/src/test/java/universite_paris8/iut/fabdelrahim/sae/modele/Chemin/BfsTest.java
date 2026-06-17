package universite_paris8.iut.fabdelrahim.sae.modele.Chemin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BfsTest {

    private int[][] grilleStandard;

    @BeforeEach
    void setUp() {
        grilleStandard = new int[][]{
                {1,   1,   0,   1},
                {0,   1,   0,   1},
                {1,   1,   3,   1},
                {1,   0,   100, 4}
        };
    }

    @Test
    void testEstTraversable() {
        assertAll(
                () -> assertTrue(Bfs.estTraversable(1)),
                () -> assertTrue(Bfs.estTraversable(3)),
                () -> assertTrue(Bfs.estTraversable(4)),
                () -> assertTrue(Bfs.estTraversable(100))
        );

        assertAll(
                () -> assertFalse(Bfs.estTraversable(0)),
                () -> assertFalse(Bfs.estTraversable(2)),
                () -> assertFalse(Bfs.estTraversable(-1))
        );
    }

    @Test
    void testReconstruireChemin() {
        Point[][] caseavant = new Point[3][3];

        Point depart = new Point(0, 0);
        Point etape1 = new Point(0, 1);
        Point etape2 = new Point(1, 1);
        Point fin = new Point(2, 1);

        caseavant[2][1] = etape2;
        caseavant[1][1] = etape1;
        caseavant[0][1] = depart;
        caseavant[0][0] = null;

        List<Point> chemin = Bfs.reconstruireChemin(caseavant, fin);

        assertNotNull(chemin);
        assertEquals(4, chemin.size());

        assertAll(
                () -> assertEquals(depart, chemin.get(0)),
                () -> assertEquals(etape1, chemin.get(1)),
                () -> assertEquals(etape2, chemin.get(2)),
                () -> assertEquals(fin, chemin.get(3))
        );
    }

    @Test
    void testBfsCheminValideAvecContournement() {
        Point debut = new Point(0, 0);
        Point fin = new Point(2, 3);

        List<Point> chemin = Bfs.bfs(grilleStandard, debut, fin);

        assertFalse(chemin.isEmpty());
        assertEquals(6, chemin.size());
        assertEquals(debut, chemin.get(0));
        assertEquals(fin, chemin.get(chemin.size() - 1));
    }

    @Test
    void testBfsTraverséeTypesDeTerrains() {
        Point debut = new Point(2, 1);
        Point fin = new Point(3, 3);

        List<Point> chemin = Bfs.bfs(grilleStandard, debut, fin);

        assertFalse(chemin.isEmpty());
        assertEquals(4, chemin.size());
    }

    @Test
    void testBfsAucunCheminPossible() {
        int[][] grilleBloquee = {
                {1, 1, 1},
                {0, 0, 0},
                {1, 1, 1}
        };
        Point debut = new Point(0, 0);
        Point fin = new Point(2, 2);

        List<Point> chemin = Bfs.bfs(grilleBloquee, debut, fin);

        assertTrue(chemin.isEmpty());
    }

    @Test
    void testBfsDepartEgalFin() {
        Point debut = new Point(1, 1);
        Point fin = new Point(1, 1);

        List<Point> chemin = Bfs.bfs(grilleStandard, debut, fin);

        assertEquals(1, chemin.size());
        assertEquals(debut, chemin.get(0));
    }

    @Test
    void testBfsGestionDesBordsEtAngles() {
        int[][] miniGrille = {
                {1, 1},
                {1, 1}
        };
        Point debut = new Point(0, 0);
        Point fin = new Point(1, 1);

        assertDoesNotThrow(() -> {
            List<Point> chemin = Bfs.bfs(miniGrille, debut, fin);
            assertEquals(3, chemin.size());
        });
    }
}