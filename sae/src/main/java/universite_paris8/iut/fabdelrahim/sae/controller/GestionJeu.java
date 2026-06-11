package universite_paris8.iut.fabdelrahim.sae.controller;

public class GestionJeu {

    private static GestionJeu instance;
    private Controller controller;

    private GestionJeu() {}

    public static GestionJeu getInstance() {
        if (instance == null) instance = new GestionJeu();
        return instance;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }
}