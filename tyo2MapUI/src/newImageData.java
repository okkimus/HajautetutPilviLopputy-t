import javax.swing.*;

/**
 * Authors:
 * Jan-Mikael Ihanus, jamiih@utu,fi, 516329
 * Mikko Metsäranta, misame@utu.fi, 515662
 * <p>
 * course: HAJAUTETUT OHJELMISTOJÄRJESTELMÄT JA PILVIPALVELUT, SYKSY 2017
 */

/**
 * Luokka, joka pitää sisällään kuvan
 */

public class newImageData {

    private ImageIcon imageIcon = null;

    public newImageData() {

    }

    public synchronized void setNewImageData(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public synchronized ImageIcon getImageIcon() {
        return imageIcon;
    }
}
