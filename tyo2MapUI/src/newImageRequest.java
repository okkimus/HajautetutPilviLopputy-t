import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;

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
    private ImageRequestHandler irh;

    /**
     *
     * @param jLabel itse karttakuva, joka päivitetään käyttöliittymään
     */

    public newImageRequest(JLabel jLabel) {
        imageLabel = jLabel;
        irh = new ImageRequestHandler();

    }

    /**
     *
     * @param imageUrl kuvaosoite, jolla uusi kuva haetaan nettisivuilta
     */
    public void updateMap(URL imageUrl) {
        try {
            if (irh.isAlive()) {
                irh.addUrl(imageUrl);
            } else {
                irh = new ImageRequestHandler();
                irh.addUrl(imageUrl);
                irh.start();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setImage(ImageIcon img) {
        // Asetetaan kuva käyttöliittymän JLabeliin.
        imageLabel.setIcon(img);
    }

    private ImageIcon fetchImage(URL imageUrl) {
        // Haetaan kuva annetulla URL:illa
        System.out.println(Thread.currentThread().getName() + " alkoi hakea kuvaa");
        ImageIcon imageIcon = new ImageIcon(imageUrl);

        System.out.println(Thread.currentThread().getName() + " haki kuvan");
        return imageIcon;
    }


    /**
     *  Luokka luomaan kuvanhakuthread
     */
    class ImageRequestHandler extends Thread {

        protected ArrayList<URL> urls;


        public ImageRequestHandler() {
            urls = new ArrayList<>();
        }

        public void run() {
            try {
                while (!urls.isEmpty()) {
                    urls = new ArrayList<URL>(urls.subList(urls.size() - 1, urls.size()));
                    ImageIcon img = fetchImage(urls.remove(0));
                    setImage(img);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void addUrl(URL imageUrl) {
            urls.add(imageUrl);
        }

    }
}
