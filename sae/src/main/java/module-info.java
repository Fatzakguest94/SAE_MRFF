module universite_paris8.iut.fabdelrahim.sae {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens universite_paris8.iut.fabdelrahim.sae to javafx.fxml;
    exports universite_paris8.iut.fabdelrahim.sae;
    exports universite_paris8.iut.fabdelrahim.sae.controller;
    opens universite_paris8.iut.fabdelrahim.sae.controller to javafx.fxml;
    exports universite_paris8.iut.fabdelrahim.sae.modele;
    opens universite_paris8.iut.fabdelrahim.sae.modele to javafx.fxml;
    exports universite_paris8.iut.fabdelrahim.sae.vue;
    opens universite_paris8.iut.fabdelrahim.sae.vue to javafx.fxml;
    exports universite_paris8.iut.fabdelrahim.sae.modele.Tours;
    opens universite_paris8.iut.fabdelrahim.sae.modele.Tours to javafx.fxml;
    exports universite_paris8.iut.fabdelrahim.sae.modele.Zombies;
    opens universite_paris8.iut.fabdelrahim.sae.modele.Zombies to javafx.fxml;
    exports universite_paris8.iut.fabdelrahim.sae.modele.Chemin;
    opens universite_paris8.iut.fabdelrahim.sae.modele.Chemin to javafx.fxml;
}