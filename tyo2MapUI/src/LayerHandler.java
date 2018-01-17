import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.sound.midi.Soundbank;


public class LayerHandler extends DefaultHandler {

    //List to hold Layer object
    private List<Layer> layerList = null;
    private Layer laye = null;

    boolean layer = false;
    boolean name = false;
    boolean title = false;

    //getter method for list of layers
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

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        //System.out.println("start element: "+qName);
        if (qName.equalsIgnoreCase("layer")) {
            System.out.println("start element: "+qName);

            if (layerList == null)
                layerList = new ArrayList<>();
            laye = new Layer();

            for (int i = 0; i < attributes.getLength(); i++) {
                String aName = attributes.getQName(i);
                if (aName.equals("opaque"))
                    laye.setOpaque(Integer.parseInt(attributes.getValue(i)));
                else if (aName.equals("cascaded"))
                    laye.setCascaded(Integer.parseInt(attributes.getValue(i)));
                else if (aName.equals("queryable"))
                    laye.setQueryable(Integer.parseInt(attributes.getValue(i)));
            }
            layer = true;

            System.out.println("layer found");

        } else if (layer && qName.equalsIgnoreCase("name")) {
            //set boolean values for fields, will be used in setting layer variables
            name = true;
        } else if (layer && qName.equalsIgnoreCase("title")) {
            title = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        //System.out.println("end element: " +qName);

        /*
        if (qName.equalsIgnoreCase("Layer")) {
            System.out.println("end element: " +qName);
            //add layer object to list
            layerList.add(laye);
        }
        */
         
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        //System.out.println("characters");
        String read = new String(ch, start, length);


        if (layer && name) {
            System.out.println(read);
            //name element, set layer name
            laye.setName(read);
            if (laye.getTitle() != null)
                layer = false;
            else
                layerList.add(laye);
            name = false;
        } else if (title) {
            System.out.println(read);
            laye.setTitle(read);
            if (laye.getName() != null)
                layer = false;
            else
                layerList.add(laye);
            title = false;
        }
    }
}