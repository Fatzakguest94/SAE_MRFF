package universite_paris8.iut.rissamou.sae_td;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

public class GestionImage {
    private static final Map<String, Image> sprites = new HashMap<>();

    public static void loadAssets() {
        sprites.put("Zombie", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/rissamou/sae_td/vue/zombie.png")));
    }

    public static Image getImage(String key) {
        return sprites.get(key);
    }
}