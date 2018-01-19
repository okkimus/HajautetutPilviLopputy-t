import javax.swing.*;
import java.net.URL;

/**
 * Authors:
 * Jan-Mikael Ihanus, jamiih@utu,fi, 516329
 * Mikko Metsäranta, misame@utu.fi, 515662
 *
 * course: HAJAUTETUT OHJELMISTOJÄRJESTELMÄT JA PILVIPALVELUT, SYKSY 2017
 */


/**
 * Luokka, joka hoitaa uuden säikeen luomisen kuvan hakua ja päivitystä varten.
 *
 */
public class newImageRequest {

    private newImageData nid;
    private JLabel imageLabel;

    public newImageRequest(newImageData nid, URL imageUrl, JLabel jLabel) {
        imageLabel = jLabel;

        try {

            new ImageRequestHandler(nid, imageUrl).start();

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    class ImageRequestHandler extends Thread {

        newImageData nid;
        URL imageUrl;

        public ImageRequestHandler(newImageData newimagedata, URL imgUrl) {
            nid = newimagedata;
            imageUrl = imgUrl;
        }

        public void run() {
            try {

                //ImageIcon imageIcon = new ImageIcon(new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&BBOX=-180,-90,180,90&SRS=EPSG:4326&WIDTH=953&HEIGHT=480&LAYERS=bluemarble&STYLES=&FORMAT=image/png&TRANSPARENT=true"));

                // Haetaan kuva annetulla URL:illa
                ImageIcon imageIcon = new ImageIcon(imageUrl);

                //nid.setNewImageData(imageIcon);

                // Asetetaan kuva käyttöliittymän JLabeliin.
                imageLabel.setIcon(imageIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
