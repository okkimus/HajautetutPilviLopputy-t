import java.io.*;
import java.net.*;

public class SummausPalvelu {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private InputStream iS;
    private OutputStream oS;
    private ObjectOutputStream oOut;
    private static ObjectInputStream oIn;
    private final int PORT = 5000;
    private final boolean verbose = true;

    /**
     * Luodaan summauspalvelu ja sen tarvitsemat osaset.
     */
    public SummausPalvelu() {
        try {
            // ServerSocket kuuntelee PORT:iin tulevia yhteyksiä
            serverSocket = new ServerSocket(PORT);
            // Lähetetään Y:lle portti jota me kuunnellaan (siis serverSocketin portti)
            lahetaPortti();
            // Yhdistetään serverSocketti clientSockettiin jolla lähetetään/vastaanotetaan dataa
            clientSocket = serverSocket.accept();

            // Yhdistetään kaikki input- ja output-virrat
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

        int summausPalvelijoidenLkm = s.lueLuku();
        s.lahetaPortit(summausPalvelijoidenLkm);
        System.out.println("Printataan uudestaan luettu luku " + s.lueLuku());

        SharedData sd = new SharedData(summausPalvelijoidenLkm);

        for (int portti = 54321; portti < 54331; portti++) {
            SummausThread Summaus = new SummausThread();
            Summaus.SummausThread(portti);


        }

        boolean kaynnissa = true;
        ObjectOutputStream serverinVirtaUlos = s.getoOut();

        while (kaynnissa) {
            int komento = s.lueLuku();
            int vastaus = -1;
            // Ensimmäinen kerta kun käytän switchiä :D
            switch (komento) {
                case 0:
                    kaynnissa = false; // TODO: hoida kaikki sulkemiseen tarvittavat asiat.
                case 1:
                    vastaus = sd.palautaKokonaissumma();
                    break;
                case 2:
                    vastaus = sd.palautaSuurimmanSummanPalvelija();
                    break;
                case 3:
                    vastaus = sd.palautaLukujenLukumaara();
                    break;
            }
            serverinVirtaUlos.writeInt(vastaus);
        }
    }

    /**
     * Lähettää Y:lle summauspalvelijoiden porttien numerot
     * TODO: tätä ennen täytyy luoda threadit, ja katsoa niiden portit(?)
     *
     */
    private void lahetaPortit(int lkm) throws IOException {
        // kovakoodatut portit
        int[] portit = {54121, 54222, 54223, 54224, 54225, 54226, 54227, 54228, 54229, 54330};
        // käydään läpi portit ja kirjoitetaan ne ObjectOutputStreamiin
        for (int i = 0; i < lkm; i++) {
            System.out.println("Kirjoitetaan: " + portit[i]);
            oOut.writeInt(portit[i]);
        }
        oOut.flush();
    }

    /**
     * Lukee ja palauttaa Y:n lähettämän luvun
     *
     */
    private int lueLuku() {
        int luku = -1;
        try {
            // luetaan kokonaisluku ObjectInputStreamista
            luku = oIn.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return luku;
    }

    /**
     * Lähettää Y:lle portin numeron, jota tämä SummausPalvelu käyttää.
     *
     */
    private void lahetaPortti() throws Exception {
        if (verbose) {
            System.out.println("Yritetään lähettää TCP portin numeroa.");
        }
        // Luodaan DatagramSocket UDP-paketin lähettämiseksi
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        System.out.println(serverSocket);
        
        byte[] sendPort = new byte[1024];

        String serverSocketPort = String.valueOf(PORT);
        // Muutetaan portin numero merkkijonoksi, koska se oli helpompi vaihtoehto! getBytes() toimii vain merkkijonolle...
        sendPort = serverSocketPort.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendPort, sendPort.length, IPAddress, 3126);
        clientSocket.send(sendPacket);
        if (verbose) {
            System.out.println("UDP paketti lähetetty, suljetaan UPD socket.");
        }
        // Suljetaan DatagramSocket kun UDP paketti on lähetetty
        clientSocket.close();
    }

    public ObjectOutputStream getoOut() {
        return oOut;
    }
}
