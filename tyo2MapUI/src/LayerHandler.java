import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class LayerHandler extends DefaultHandler {

    //List to hold Layer object
    private List<Layer> layerList = null;
    private Layer laye = null;


    //getter method for list of layers
    public List<Layer> getLayerList() {
        return layerList;
    }

    boolean layer = false;
    boolean name = false;
    boolean title = false;

    @Override
    public void startDocument () throws SAXException {
        System.out.println("start document");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("end document");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

            System.out.println("start element: "+qName);
        if (qName.equalsIgnoreCase("layer")) {
                layer = true;
                layerList = new ArrayList<>();
                laye = new Layer();

            System.out.println("layer found");

        } else if (qName.equalsIgnoreCase("name")) {
            //set boolean values for fields, will be used in setting layer variables
            name = true;
        } else if (layer && qName.equalsIgnoreCase("title")) {
            title = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        System.out.println("end element: " +qName);

        if (qName.equalsIgnoreCase("Layer")) {
            //add layer object to list
            layerList.add(laye);
        }
         
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        System.out.println("characters");

        if (layer && name) {
            //name element, set layer name
            laye.setName(new String(ch, start, length));
            name= false;
        } else if (title) {
            laye.setTitle(new String(ch, start, length));
            title = false;
        }

    }
}