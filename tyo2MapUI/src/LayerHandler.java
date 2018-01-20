import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.sound.midi.Soundbank;

/**
 * Authors:
 * Jan-Mikael Ihanus, jamiih@utu,fi, 516329
 * Mikko Metsäranta, misame@utu.fi, 515662
 *
 * course: HAJAUTETUT OHJELMISTOJÄRJESTELMÄT JA PILVIPALVELUT, SYKSY 2017
 */


/**
 * Apuluokka SAX-parserille. Hoitaa tarvittavat toimenpiteet laukaistuille tapahtumille SAX-parserissa.
 *
 */
public class LayerHandler extends DefaultHandler {

    // lista kartan kerroksille

    private List<Layer> layerList = null;
    private Layer laye = null;

    boolean layer = false;
    boolean name = false;
    boolean title = false;

    // palauttaa listan kerroksista

    public List<Layer> getLayerList() {
        return layerList;
    }

    @Override
    public void startDocument () throws SAXException {
        System.out.println("start document");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("end document");
    }

    /**
     * Lukee elementin tagin ja/tai attribuutteja. Asettaa tarpeellisia totuusarvoja, että datasta saadaan myöhemmin
     * uutettua oikeat tiedot.
     *
     * Metodissa otetaan vain huomioon Layer-elementit
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        // Tarkistetaan onko elementti "Layer" elementti
        if (qName.equalsIgnoreCase("layer")) {

            // Jos kerroslistaa ei ole alustettu, alustetaan se.
            if (layerList == null)
                layerList = new ArrayList<>();
            laye = new Layer();

            // Mikäli tägi sisältää attribuutteja, käydään ne lävitse.
            for (int i = 0; i < attributes.getLength(); i++) {
                String aName = attributes.getQName(i);
                if (aName.equals("opaque"))
                    laye.setOpaque(Integer.parseInt(attributes.getValue(i)));
                else if (aName.equals("cascaded"))
                    laye.setCascaded(Integer.parseInt(attributes.getValue(i)));
                else if (aName.equals("queryable"))
                    laye.setQueryable(Integer.parseInt(attributes.getValue(i)));
            }
            // Todetaan, että Layer löytyi.
            layer = true;
            System.out.println("layer found");

        // Jos löytyy layer ja elementtin tagi on "name", tehdään tarvittavat asetukset totuusarvolle.
        } else if (layer && qName.equalsIgnoreCase("name")) {
            name = true;

        // Jos löytyy layer ja elementtin tagi on "title", tehdään tarvittavat asetukset totuusarvolle.
        } else if (layer && qName.equalsIgnoreCase("title")) {
            title = true;
        }
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

    /**
     * Lukee tagin sisältämän datan. Otetaan huomioon eri totuusarvot, että tiedetään mitä luetaan.
     *
     */
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        //System.out.println("characters");
        String read = new String(ch, start, length);

        // Jos datasta on löytyn "Layer" ja "Name" tagit
        if (layer && name) {
            laye.setName(read);

            // Jos luotavassa layerissä on jo myös otsikko, layer on valmis. Merkataan totuusarvo falseksi, että seuraavan
            // layerin lukeminen tulee mahdolliseksi
            if (laye.getTitle() != null)
                layer = false;
            else
                // Lisätään layer listaan
                layerList.add(laye);

            // "name" -kentän tietue on nyt luettu
            name = false;

        } else if (layer && title) {
            laye.setTitle(read);

            // Jos luotavassa layerissä on jo myös nimi, layer on valmis. Merkataan totuusarvo falseksi, että seuraavan
            // layerin lukeminen tulee mahdolliseksi
            if (laye.getName() != null)
                layer = false;
            else
                layerList.add(laye);
            // "title" -kentän tietue on nyt luettu
            title = false;
        }
    }
}