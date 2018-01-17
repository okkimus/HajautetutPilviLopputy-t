import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.*;
import org.w3c.dom.*;

public class xmlParserToFile {


    public xmlParserToFile(URL url) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        TransformerFactory tfactory = TransformerFactory.newInstance();


        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Transformer xform = tfactory.newTransformer();

            Document document = db.parse(url.openStream());
            File myOutput = new File("output.xml");
            xform.transform(new DOMSource(document), new StreamResult(myOutput));

            //Normalize the XML Structure; It's just too important !!
            document.getDocumentElement().normalize();

            //Here comes the root node
            Element root = document.getDocumentElement();
            System.out.println(root.getNodeName());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
