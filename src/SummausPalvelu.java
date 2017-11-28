//import com.sun.org.apache.xpath.internal.SourceTree;
import java.io.*;
import java.net.*;

public class SummausPalvelu {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private InputStream iS;
    private OutputStream oS;
    private ObjectOutputStream oOut;
    private ObjectInputStream oIn;
    private final int PORT = 5000;
    private final boolean verbose = true;

    public SummausPalvelu() {
        try {
            serverSocket = new ServerSocket(PORT);
            lahetaPortti();
            clientSocket = serverSocket.accept();
            iS = clientSocket.getInputStream();
            oS = clientSocket.getOutputStream();
            oOut = new ObjectOutputStream(oS);
            oIn = new ObjectInputStream(iS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        SummausPalvelu s = new SummausPalvelu();
        s.lahetaPortit(s.luePorttienLkm());



    }

    // TODO: tätä ennen täytyy threadit luoda, ja katsoa niiden portit(?)
    private void lahetaPortit(int lkm) throws IOException {
        // kovakoodatut portit
        int[] portit = {54321, 54320, 54322, 54323, 54324, 54325, 54326, 54327, 54328, 54329};
        for (int i = 0; i < lkm; i++) {
            System.out.println("Kirjoitetaan: " + portit[i]);
            oOut.writeInt(portit[i]);
        }
        oOut.flush();
    }

    private int luePorttienLkm() {
        int lkm = -1;
        try {
            lkm = oIn.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lkm;
    }
    
    private void lahetaPortti() throws Exception {
        if (verbose) {
            System.out.println("Yritetään lähettää TCP portin numeroa.");
        }
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        System.out.println(serverSocket);
        
        byte[] sendPort = new byte[1024];

        //String serverSocketPort = String.valueOf(getServerSocket().getPort());
        String serverSocketPort = String.valueOf(PORT);

        sendPort = serverSocketPort.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendPort, sendPort.length, IPAddress, 3126);
        clientSocket.send(sendPacket);
        if (verbose) {
            System.out.println("UDP paketti lähetetty, suljetaan UPD socket.");
        }
        clientSocket.close();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
