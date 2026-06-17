package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieNormal;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Burger;

import static org.junit.jupiter.api.Assertions.*;

class TourTest {

    private Tour tourBurger;
    private Tour tourFrite;
    private Tour tourGlace;
    private Environnement env;

    @BeforeEach
    void setUp() {
        tourBurger = new Tour(0, 0, 50, 20, 10, "LanceBurger");
        tourFrite = new Tour(10, 10, 30, 5, 2, "MitrailletteFrite");
        tourGlace = new Tour(0, 0, 40, 0, 15, "BacGlace");

        env = new Environnement();
        env.getZombies().clear();
        env.getProjectiles().clear();
    }

    @Test
    void testAmeliorer() {
        assertEquals(1, tourBurger.getNiveau());
        assertEquals(20, tourBurger.degats);
        assertEquals(10, tourBurger.vitesseTir);

        tourBurger.ameliorer();

        assertEquals(2, tourBurger.getNiveau());
        assertEquals(30, tourBurger.degats);
        assertEquals(7, tourBurger.vitesseTir);
    }

    @Test
    void testGetPrixAmelioration() {
        assertEquals(300, tourBurger.getPrixAmelioration());
        assertEquals(200, tourFrite.getPrixAmelioration());
        assertEquals(250, tourGlace.getPrixAmelioration());

        tourBurger.ameliorer();
        assertEquals(400, tourBurger.getPrixAmelioration());
    }

    @Test
    void testAttaquerCibleAPortee() {
        Enemie zombie = new ZombieNormal(20, 20);
        env.getZombies().add(zombie);

        assertEquals(0, tourBurger.cooldown);
        tourBurger.attaquer(env);

        assertEquals(10, tourBurger.cooldown);
        assertEquals(1, env.getProjectiles().size());
        assertInstanceOf(Burger.class, env.getProjectiles().get(0));
    }

    @Test
    void testAttaquerCibleHorsPortee() {
        Enemie zombie = new ZombieNormal(100, 100);
        env.getZombies().add(zombie);

        tourBurger.attaquer(env);

        assertEquals(0, tourBurger.cooldown);
        assertTrue(env.getProjectiles().isEmpty());
    }

    @Test
    void testAttaquerSousCooldown() {
        Enemie zombie = new ZombieNormal(20, 20);
        env.getZombies().add(zombie);

        tourBurger.attaquer(env);
        assertEquals(10, tourBurger.cooldown);
        int nbProjectilesApresTir1 = env.getProjectiles().size();

        tourBurger.attaquer(env);
        assertEquals(9, tourBurger.cooldown);
        assertEquals(nbProjectilesApresTir1, env.getProjectiles().size());
    }

    @Test
    void testAttaquerZombieMort() {
        Enemie zombieMort = new ZombieMort(20, 20);
        env.getZombies().add(zombieMort);

        tourBurger.attaquer(env);

        assertEquals(0, tourBurger.cooldown);
        assertTrue(env.getProjectiles().isEmpty());
    }

    @Test
    void testIdUniqueDifferent() {
        Tour autreTour = new Tour(0, 0, 50, 20, 10, "LanceBurger");
        assertNotEquals(tourBurger.getIdUnique(), autreTour.getIdUnique());
    }

    private static class ZombieMort extends ZombieNormal {
        public ZombieMort(int x, int y) {
            super(x, y);
        }

        @Override
        public boolean estMort() {
            return true;
        }
    }
}