import javax.swing.*;
import java.net.URL;

public class newImageRequest {

    private newImageData nid;

    public newImageRequest(newImageData nid) {

        try {

            new ImageRequestHandler(nid).start();

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    class ImageRequestHandler extends Thread {

        newImageData nid;

        public ImageRequestHandler(newImageData newimagedata) {

        nid = newimagedata;
        }

        public void run() {
            try {

                ImageIcon imageIcon = new ImageIcon(new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&BBOX=-180,-90,180,90&SRS=EPSG:4326&WIDTH=953&HEIGHT=480&LAYERS=bluemarble&STYLES=&FORMAT=image/png&TRANSPARENT=true"));
                nid.setNewImageData(imageIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
