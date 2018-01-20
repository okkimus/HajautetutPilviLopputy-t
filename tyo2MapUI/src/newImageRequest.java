import javax.swing.*;
import java.net.URL;

/**
 * Authors:
 * Jan-Mikael Ihanus, jamiih@utu,fi, 516329
 * Mikko Metsäranta, misame@utu.fi, 515662
 * <p>
 * course: HAJAUTETUT OHJELMISTOJÄRJESTELMÄT JA PILVIPALVELUT, SYKSY 2017
 */


/**
 * Luokka, joka hoitaa uuden säikeen luomisen kuvan hakua ja päivitystä varten.
 *
 */

public class newImageRequest {

    private newImageData nid;
    private JLabel imageLabel;

    /**
     *
     * @param nid kuvan tietoluokan osoittaminen
     * @param imageUrl kuvaosoite, jolla uusi kuva haetaan nettisivuilta
     * @param jLabel itse karttakuva, joka päivitetään käyttöliittymään
     */

    public newImageRequest(newImageData nid, URL imageUrl, JLabel jLabel) {
        imageLabel = jLabel;

        try {

            new ImageRequestHandler(nid, imageUrl).start();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     *  Luokka luomaan kuvanhakuthread
     */

    class ImageRequestHandler extends Thread {

        newImageData nid;
        URL imageUrl;

        /**
         *
         * @param newimagedata kuvan tietoluokan osoittaminen
         * @param imgUrl kuvaosoite, jolla uusi kuva haetaan nettisivuilta
         */

        public ImageRequestHandler(newImageData newimagedata, URL imgUrl) {
            nid = newimagedata;
            imageUrl = imgUrl;
        }

        public void run() {
            try {

                // Haetaan kuva annetulla URL:illa
                ImageIcon imageIcon = new ImageIcon(imageUrl);

                // Asetetaan kuva käyttöliittymän JLabeliin.
                imageLabel.setIcon(imageIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
