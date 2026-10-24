package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

public enum TypeTour {
    //prix  sur chemin ?  clé de l'image dans GestionImage
    LANCE_BURGER(200, false, "LanceBurger"),
    MITRAILLETTE_FRITE(100, false, "MitrailletteFrite"),
    BAC_GLACE(150, true, "BacGlace"),
    BARBECUE(250, true, "Barbecue");

    private final int prix;
    private final boolean surChemin;
    private final String cleImage;

    TypeTour(int prix, boolean surChemin, String cleImage) {
        this.prix = prix;
        this.surChemin = surChemin;
        this.cleImage = cleImage;
    }

    public int getPrix() { return prix; }
    public boolean estPoseSurChemin() { return surChemin; }
    public String getCleImage() { return cleImage; }
}