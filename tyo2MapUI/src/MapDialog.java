import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;


/**
 * Authors:
 * Jan-Mikael Ihanus, jamiih@utu,fi, 516329
 * Mikko Metsäranta, misame@utu.fi, 515662
 * <p>
 * course: HAJAUTETUT OHJELMISTOJÄRJESTELMÄT JA PILVIPALVELUT, SYKSY 2017
 */


/** Kartankatseluohjelman graafinen käyttoliittymä
 *
 * Luokka, joka hoitaa uuden säikeen luomisen kuvan hakua ja päivitystä varten.
 *
 */

public class MapDialog extends JFrame {

    // Käyttöliittymän komponentit, karttakuva (imageLabel) oikealle, napit vasemmalle

    private JLabel imageLabel = new JLabel();
    private JPanel leftPanel = new JPanel();

    private JButton refreshB = new JButton("Päivitä");
    private JButton leftB = new JButton("<");
    private JButton rightB = new JButton(">");
    private JButton upB = new JButton("^");
    private JButton downB = new JButton("v");
    private JButton zoomInB = new JButton("+");
    private JButton zoomOutB = new JButton("-");


    private LayerHandler lh = new LayerHandler();
    private Coordinates cord = new Coordinates(180, -180, 90, -90);
    private newImageRequest nir = new newImageRequest(imageLabel);

    /**
     *
     * @param lh viittaa LayerHander -luokkaan, joka käsittelee XML-tiedostosta tarvittavat tiedot karttakerroksiin
     */

    public MapDialog(LayerHandler lh) throws Exception {

        // Valmistelee ikkunan ja lisää siihen komponentit

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Ladataan kuva joka näytetään kun ohjelma käynnistetään

        imageLabel.setIcon(new ImageIcon(new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&BBOX=-180,-90,180,90&SRS=EPSG:4326&WIDTH=953&HEIGHT=480&LAYERS=bluemarble,cities&STYLES=&FORMAT=image/png&TRANSPARENT=true")));

        add(imageLabel, BorderLayout.EAST);

        // napit aktiiviseksi

        ButtonListener bl = new ButtonListener();
        refreshB.addActionListener(bl);
        leftB.addActionListener(bl);
        rightB.addActionListener(bl);
        upB.addActionListener(bl);
        downB.addActionListener(bl);
        zoomInB.addActionListener(bl);
        zoomOutB.addActionListener(bl);

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        leftPanel.setMaximumSize(new Dimension(100, 600));

        // Silmukka, joka luo karttakerroksen eri kerrosvaihtoehdot valittaviksi checkboxeiksi
        // jättää pois ensimmäisen kerroksen (WMS Demo Server for MapServer)

        for (int i = 1; i < lh.getLayerList().size(); i++) {
            Layer layer = lh.getLayerList().get(i);

            leftPanel.add(new LayerCheckBox(layer.getName(), layer.getTitle(), true));
        }

        //Painikkeet vasemmalle puolelle

        leftPanel.add(refreshB);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(leftB);
        leftPanel.add(rightB);
        leftPanel.add(upB);
        leftPanel.add(downB);
        leftPanel.add(zoomInB);
        leftPanel.add(zoomOutB);

        add(leftPanel, BorderLayout.WEST);

        pack();
        setVisible(true);

    }

    public static void main(String[] args) throws Exception {

        URL url = new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities");

        xmlParserToFile xP = new xmlParserToFile(url);

        LayerHandler handler = new LayerHandler();

        SAXparser Sp = new SAXparser(handler);

        new MapDialog(handler);

    }

    // Kontrollinappien kuuntelija

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == refreshB) {
                try {
                    updateImage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == leftB) {

                // Siirtyminen kartalla vasemmalle
                cord.moveLeft(10);
                try {
                    updateImage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == rightB) {

                // Siirtyminen kartalle oikealle
                cord.moveRight(10);
                try {
                    updateImage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == upB) {

                // Siirtyminen kartalla ylöspäin
                cord.moveUp(10);
                try {
                    updateImage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == downB) {

                // Siirtyminen kartalla alaspäin
                cord.moveDown(10);
                try {
                    updateImage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == zoomInB) {

                // Zoomaa sisäänpäin kartalla
                cord.zoomIn(12);
                try {
                    updateImage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == zoomOutB) {

                // Zoomaa ulospäin kartalla
                cord.zoomOut(12);
                try {
                    updateImage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Valintalaatikko, joka muistaa karttakerroksen nimen
    private class LayerCheckBox extends JCheckBox {
        private String name = "";

        public LayerCheckBox(String name, String title, boolean selected) {
            super(title, null, selected);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // Tarkastetaan mitkä karttakerrokset on valittu,
    // tehdään uudesta karttakuvasta pyyntö palvelimelle ja päivitetään kuva
    public void updateImage() throws Exception {

        String s = "";

        // Tutkitaan, mitkä valintalaatikot on valittu, ja
        // kerätään s:ään pilkulla erotettu lista valittujen kerrosten
        // nimistä (käytetään haettaessa uutta kuvaa)
        Component[] components = leftPanel.getComponents();
        for (Component com : components) {
            if (com instanceof LayerCheckBox)
                if (((LayerCheckBox) com).isSelected()) s = s + com.getName() + ",";
        }
        if (s.endsWith(",")) s = s.substring(0, s.length() - 1);

        // haetaan koordinaatit
        int xU = cord.getxUpper();
        int xL = cord.getxLower();
        int yU = cord.getyUpper();
        int yL = cord.getyLower();

        String coordinates = xL + "," + yL + "," + xU + "," + yU;

        System.out.println("COORDINATES NOW: " + coordinates);

        URL imageUrl = new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&BBOX="
                + coordinates
                + "&SRS=EPSG:4326&WIDTH=953&HEIGHT=480&LAYERS="
                + s
                + "&STYLES=&FORMAT=image/png&TRANSPARENT=true");

        //newImageData nid = new newImageData();

        nir.updateMap(imageUrl);

    }
} // MapDialog
