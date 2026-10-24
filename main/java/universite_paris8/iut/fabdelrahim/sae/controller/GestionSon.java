package universite_paris8.iut.fabdelrahim.sae.controller;

import javax.sound.sampled.*;

public class GestionSon {

    private static GestionSon instance;
    private Clip clip;
    private double volumeActuel = 50.0;

    private GestionSon() {}

    public static GestionSon getInstance() {
        if (instance == null) instance = new GestionSon();
        return instance;
    }

    public void demarrer(String chemin) {
        try {
            if (clip != null && clip.isRunning()) return;
            AudioInputStream audio = AudioSystem.getAudioInputStream(
                    new java.io.BufferedInputStream(
                            GestionSon.class.getResourceAsStream(chemin)
                    )
            );
            clip = AudioSystem.getClip();
            clip.open(audio);

            setVolume(this.volumeActuel);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) clip.stop();
    }

    public void reprendre() {
        if (clip != null && !clip.isRunning()) {
            setVolume(this.volumeActuel);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void setVolume(double valeur) {
        this.volumeActuel = valeur;

        if (clip == null) return;

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = valeur == 0 ? -80.0f : (float) (Math.log10(valeur / 100.0) * 20);
            gain.setValue(dB);
        }
    }
    public double getVolumeActuel() {
        return this.volumeActuel;
    }
}