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
