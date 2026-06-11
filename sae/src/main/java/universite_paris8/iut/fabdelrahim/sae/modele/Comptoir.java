package universite_paris8.iut.fabdelrahim.sae.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Comptoir {
    private int x;
    private int y;
    private String identite;

    //PROPERTY
    private IntegerProperty hp;

    public Comptoir(int x, int y, String identite) {
        this.x = x;
        this.y = y;
        this.identite = identite;
        // Initialisation de la Property à 100 PV
        this.hp = new SimpleIntegerProperty(100);
    }

    public void recevoirDegats(int degats) {
        // On utilise .set() et .get() pour modifier la valeur d'une Property
        int nouveauxHp = this.hp.get() - degats;
        if (nouveauxHp < 0) {
            nouveauxHp = 0;
        }
        this.hp.set(nouveauxHp);
    }

    //GETTER DE LA PROPERTY Pour le binding ou l'écoute
    public IntegerProperty hpProperty() {
        return this.hp;
    }

    public int getHp() {
        return this.hp.get();
    }

    public String getIdentite() {
        return this.identite;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean estDetruit() {
        return this.hp.get() <= 0;
    }
}