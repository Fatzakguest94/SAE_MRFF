package universite_paris8.iut.rissamou.sae_td.modele;

public class Comptoir {
    private int x;
    private int y;
    private int hp;
    private String identite;

    public Comptoir(int x, int y, String identite) {
        this.x = x;
        this.y = y;
        this.hp = 100;
        this.identite = identite;
    }

    public void recevoirDegats(int degats) {
        this.hp -= degats;
        if (this.hp < 0) {
            this.hp = 0; // Pour éviter d'avoir pv négatifs
        }
    }

    public int getHp() {
        return this.hp;
    }

    public String getIdentite() {
        return identite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean estDetruit() {
        return this.hp <= 0;
    }

}
