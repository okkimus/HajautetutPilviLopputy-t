import javax.swing.*;

public class newImageData {

    private ImageIcon imageIcon = null;

    public newImageData() {

    }

    public synchronized void setNewImageData (ImageIcon imageIcon){
        this.imageIcon = imageIcon;
    }

    public synchronized ImageIcon getImageIcon() {
        return imageIcon;
    }
}
