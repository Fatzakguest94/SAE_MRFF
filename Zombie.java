package universite_paris8.iut.mlatiaoui.saedevmoha;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Zombie {
    private IntegerProperty x;
    private IntegerProperty y;
    private int vitesse;

    public Zombie(int x, int y,int vitesse){
        this.x=new SimpleIntegerProperty(0);
        this.y=new SimpleIntegerProperty(0);
        this.vitesse=vitesse;
    }

    //l prop
    public IntegerProperty px(){
        return x;
    }

    public IntegerProperty py(){
        return y;
    }

    public int getX(){
        return x.getValue();
    }

    public int getY(){
        return y.getValue();
    }

    public void setX(int n){
        x.setValue(n);
    }

    public void setY(int n){
        y.setValue(n);
    }

    //test mvt que de la gauche vers la droite tp1, puis demande s'il peut avancer pour etre sur que sa depasse pas la limite du terrain
    public void seDeplace(TerrainProvisoire terrain){
        int prX=this.getX()+(this.vitesse);
        //int prY=this.getY()+(this.vitesse);

        if (terrain.limite(prX,this.getY())){
            this.setX(prX);
        }else {
            System.out.println("Un des zombies est arrivé jusqu'au bout de la salle");
        }
    }


}
 
 
 
 
 
package universite_paris8.iut.mlatiaoui.saedevmoha;

public class TerrainProvisoire {
    public int hauteur=500;
    public int largeur=700;

    public boolean limite(int prX, int prY){
        return(prX>=0 && prX <= largeur && prY>=0 && prY<= hauteur);
    }
}





























package universite_paris8.iut.fzekraoui.saedev.controller;

import java.util.List;
import javafx.scene.image.ImageView;
import universite_paris8.iut.fzekraoui.saedev.modele.Point;

public class Zombie {
    private List<Point> chemin;
    private int indexChemin = 0;
    private int pv;
    private boolean recompenseDonnee = false; // flag pour ne payer qu'une fois

    public Zombie() {
    }

    public void setChemin(List<Point> chemin) {
        this.chemin = chemin;
        this.indexChemin = 0;
    }

    public void update(ImageView view, int tileSize) {
        if (this.chemin != null && !this.chemin.isEmpty()) {
            if (this.indexChemin < this.chemin.size()) {
                Point p = (Point) this.chemin.get(this.indexChemin);
                view.setLayoutX((double) (p.y * tileSize));
                view.setLayoutY((double) (p.x * tileSize));
                ++this.indexChemin;
            }
        }
    }

    public void mort() {
        this.pv = 0;
    }

    public boolean vie() {
        return this.pv > 0;
    }

    public boolean estMort() {
        return !this.vie();
    }

    public boolean prendreRecompense() {
            if (estMort() && this.recompenseDonnee != false) {
            this.recompenseDonnee = true;
            return true;
        }
        return false;
    }
}
