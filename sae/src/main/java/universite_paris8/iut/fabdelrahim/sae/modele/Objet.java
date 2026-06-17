package universite_paris8.iut.fabdelrahim.sae.modele;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.Tour;

public class Objet {

    private double x;
    private double y;
    private Environnement env;
    private boolean ramasse;
    private int tempseffet;
    private boolean dejaaffiche;

    public Objet(double x, double y, Environnement env){
        this.x=x;
        this.y=y;
        this.env=env;
        this.ramasse=false;
        this.dejaaffiche= false;
        this.tempseffet=0;
    }

    // Drop
    public static boolean lacher(){
        return Math.random()<=1.0; //5% de chance
    }

    // Utilisateur clique sur l'item
    public void ramasser(){
        this.ramasse=true;
        this.env.setTempsbonus(600);
        for (Tour tour : this.env.getTours()){
            tour.setDegats((int)(tour.getDegats() * 1.2));//+20% dégâts
        }
    }

    // Le temps diminue
    public void agir(){
        if (this.ramasse && this.env.getTempsbonus()>0){
            this.env.setTempsbonus(this.env.getTempsbonus() - 1);//Diminue de 1
            if (this.env.getTempsbonus()==0){ // temps fini !
                for (Tour tour: this.env.getTours()){
                    tour.setDegats((int)(tour.getDegats()/1.2)); // on enlève l'effet
                }
            }
        }
    }

    public boolean isRamasse() { return this.ramasse; }
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public boolean isDejaAffiche() { return this.dejaaffiche; }
    public void setDejaAffiche(boolean dejaAffiche) { this.dejaaffiche=dejaAffiche; }
}