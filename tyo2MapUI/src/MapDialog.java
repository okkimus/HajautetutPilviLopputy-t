// Kartankatseluohjelman graafinen käyttoliittymä

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class MapDialog extends JFrame {

    // Käyttöliittymän komponentit

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

    public MapDialog(LayerHandler lh) throws Exception {

        // Valmistele ikkuna ja lisää siihen komponentit

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // ALLA OLEVAN TESTIRIVIN VOI KORVATA JOLLAKIN MUULLA ERI ALOITUSNÄKYMÄN
        // LATAAVALLA RIVILLÄ
        imageLabel.setIcon(new ImageIcon(new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&BBOX=-180,-90,180,90&SRS=EPSG:4326&WIDTH=953&HEIGHT=480&LAYERS=bluemarble,cities&STYLES=&FORMAT=image/png&TRANSPARENT=true")));

        add(imageLabel, BorderLayout.EAST);

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

        // TODO:
        // ALLA OLEVIEN KOLMEN TESTIRIVIN TILALLE SILMUKKA JOKA LISÄÄ KÄYTTÖLIITTYMÄÄN
        // KAIKKIEN XML-DATASTA HAETTUJEN KERROSTEN VALINTALAATIKOT MALLIN MUKAAN
        for (Layer layer : lh.getLayerList())
            leftPanel.add(new LayerCheckBox(layer.getName(), layer.getTitle(), true));
        //leftPanel.add(new LayerCheckBox("bluemarble", "Maapallo", true));
        //leftPanel.add(new LayerCheckBox("cities", "Kaupungit", false));

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
        updateImage();

    }

    public static void main(String[] args) throws Exception {

        URL url = new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities");

        xmlParserToFile xP = new xmlParserToFile(url);

        System.out.println("done");

        LayerHandler handler = new LayerHandler();

        SAXparser Sp = new SAXparser(handler);

        new MapDialog(handler);

    }

    // Kontrollinappien kuuntelija
    // KAIKKIEN NAPPIEN YHTEYDESSÄ VOINEE HYÖDYNTÄÄ updateImage()-METODIA
    private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == refreshB) {
                //try { updateImage(); } catch(Exception ex) { ex.printStackTrace(); }
            }
            if(e.getSource() == leftB) {
                // TODO:
                // VASEMMALLE SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
            }
            if(e.getSource() == rightB) {
                // TODO:
                // OIKEALLE SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
            }
            if(e.getSource() == upB) {
                // TODO:
                // YLÖSPÄIN SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
            }
            if(e.getSource() == downB) {
                // TODO:
                // ALASPÄIN SIIRTYMINEN KARTALLA
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
            }
            if(e.getSource() == zoomInB) {
                // TODO:
                // ZOOM IN -TOIMINTO
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
            }
            if(e.getSource() == zoomOutB) {
                // TODO:
                // ZOOM OUT -TOIMINTO
                // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
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
        public String getName() { return name; }
    }

    // Tarkastetaan mitkä karttakerrokset on valittu,
    // tehdään uudesta karttakuvasta pyyntö palvelimelle ja päivitetään kuva
    public void updateImage() throws Exception {

        String s = "";

        // Tutkitaan, mitkä valintalaatikot on valittu, ja
        // kerätään s:��n pilkulla erotettu lista valittujen kerrosten
        // nimistä (käytetään haettaessa uutta kuvaa)
        Component[] components = leftPanel.getComponents();
        for(Component com:components) {
            if(com instanceof LayerCheckBox)
                if(((LayerCheckBox)com).isSelected()) s = s + com.getName() + ",";
        }
        if (s.endsWith(",")) s = s.substring(0, s.length() - 1);

        // TODO:
        // getMap-KYSELYN URL-OSOITTEEN MUODOSTAMINEN JA KUVAN PÄIVITYS ERILLISESSÄ SÄIKEESSÄ

        newImageData nid = new newImageData();
        newImageRequest nir = new newImageRequest(nid);
        Thread.sleep(2000);
        imageLabel.setIcon(nid.getImageIcon());
    }

} // MapDialog
