import java.io.*;
import java.net.*;

/**
 * Authors:
 * Mikko Metsäranta, misame@utu.fi, 515662
 * Jan-Mikael Ihanus, jamiih@utu,fi, 516329
 *
 * course: HAJAUTETUT OHJELMISTOJÄRJESTELMÄT JA PILVIPALVELUT, SYKSY 2017
 */


/**
 * Luokka Summauspalveluiden järjestemiseen. Toteuttaa käytännössä Ohjelman Y:n.
 *
 */


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
    private int kokeilut = 0;
    public int palvelijoidenLkm;

    /**
     * Luo SummausPalvelu -olion.
     *
     * Sisältää yhteydenotot
     **/

    public SummausPalvelu() {
        try {

            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(5000);

            boolean hyvaksytty = false;

            while (!hyvaksytty) {
                try {
                    // Lähetetään Y:lle portti jota me kuunnellaan max. 5 s. ajan (siis serverSocketin portti)
                    lahetaPortti();
                    clientSocket = serverSocket.accept();
                    System.out.println("Yhteys hyväksytty");
                    clientSocket.setSoTimeout(5000);
                    hyvaksytty = true;
                } catch (SocketTimeoutException e) {
                    System.err.println("Y ei vastannut 5 sekunnin sisällä...");

                    kokeilut++;
                    if (kokeilut >= 5) {
                        System.err.println("Kokeiltu 5 kertaa...lopetetaan.");
                        System.exit(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Yhdistetään kaikki input- ja output-virrat
            iS = clientSocket.getInputStream();
            oS = clientSocket.getOutputStream();
            oOut = new ObjectOutputStream(oS);
            oIn = new ObjectInputStream(iS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //SummausPalvelu

    public static void main(String[] args) throws Exception {

        SummausPalvelu s = new SummausPalvelu();
        boolean kaynnissa = true;
        ObjectOutputStream serverinVirtaUlos = s.getoOut();

        int summausPalvelijoidenLkm = -1;

        //Jollei Y välitä lukua, suljetaan palvelu
        try {
            summausPalvelijoidenLkm = s.lueLuku();
        } catch (Exception e) {
            System.out.println("Y ei välittänyt lukua...");
            serverinVirtaUlos.writeInt(-1);
            serverinVirtaUlos.flush();
            System.out.println("Suljetaan palvelu");
            System.exit(0);
        }

        System.out.println("saatiin Y:ltä pyyntö " + summausPalvelijoidenLkm + ":lle palvelijalle");
        System.out.println("Luodaan shared data");

        //uusi SharedData -olio
        SharedData sd = new SharedData(summausPalvelijoidenLkm);

        //luo taulukon joka on summausPalvelijoidenLkm:n kokeinen
        int[] portit = new int[summausPalvelijoidenLkm];

        //hakee ja tallentaa tarvittavan määrän satunnaisia portteja
        portit = sd.satunnaisetPortit(summausPalvelijoidenLkm);

        //lähettää portit lähetettäväksi Y:lle
        s.lahetaPortit(portit);

        //viedään portit ja tarvittava lkm portteja samalla kun luodaan uusi SummausThread -olio
        new SummausThread(portit, summausPalvelijoidenLkm, sd);

        //timeout socketille
        s.clientSocket.setSoTimeout(60000);

        while (kaynnissa) {
            int komento = -1;
            try {
                komento = s.lueLuku();
            } catch (SocketTimeoutException e) {
                System.out.println("Y:ltä ei ole tullut minuuttiin kyselyä...suljetaan ohjelma...");
                System.exit(0);
            }
            System.out.println("Y teki pyynnön: " + komento);
            int vastaus = -1;
            Thread.sleep(100);
            // Switch -rakenne Y:n lähettämille kyselyille
            switch (komento) {
                case 0: // Y:ltä tulee nolla, Summauspalvelun lopetuspyyntö
                    kaynnissa = false;
                    break;
                case 1: // Y:ltä tulee ykkönen, välitettyjen lukujen kokonaissumma
                    synchronized (sd) {
                        vastaus = sd.palautaKokonaissumma(); // tallentaa vastauksen
                    }
                    break;
                case 2: // Y:ltä tulee kakkonen, mille palvelijalle suurin summa
                    synchronized (sd) {
                        vastaus = sd.palautaSuurimmanSummanPalvelija(); // tallentaa vastauksen
                    }
                    break;
                case 3: // Y:ltä tulee kolmonen, summa kokonaislukuja joka välitetty palvelijoille
                    synchronized (sd) {
                        vastaus = sd.palautaLukujenLukumaara(); // tallentaa vastauksen
                    }
                    break;
            }
            if (kaynnissa) {

                // lähettää vastauksen Y:lle
                serverinVirtaUlos.writeInt(vastaus);
                serverinVirtaUlos.flush();
            } else {
                // suljetaan hallitusti yhteydet
                s.suljeSocketit();
                System.out.println("Suljetaan ohjelma");
                System.exit(0);
            }
        } // while
    } // main

    /**
     *
     * @param portit portit jotka lähetetään Y:lle
     * @throws IOException  expeption jollei kirjoittaminen onnistu
     */

    private void lahetaPortit(int[] portit) throws IOException {

        // käydään läpi portit ja kirjoitetaan ne ObjectOutputStreamiin
        for (int i = 0; i < portit.length; i++) {
            System.out.println("Kirjoitetaan: " + portit[i]);
            // kirjoitetaan kokonaisluku ObjectInputStreamiin
            oOut.writeInt(portit[i]);
        }
        oOut.flush();
    }

    /**
     * Lukee ja palauttaa Y:n lähettämän kokonaisluvun
     *
     */

    private int lueLuku() throws SocketTimeoutException {

        int luku = -1;
        try {
            // luetaan kokonaisluku ObjectInputStreamista
            luku = oIn.readInt();
        } catch (IOException e) {
            throw new SocketTimeoutException();
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
        DatagramSocket cSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        System.out.println(serverSocket);

        byte[] sendPort = new byte[1024];

        String serverSocketPort = String.valueOf(PORT);
        // Muutetaan portin numero merkkijonoksi, koska se on helpompi vaihtoehto! getBytes() toimii vain merkkijonolle...
        sendPort = serverSocketPort.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendPort, sendPort.length, IPAddress, 3126);
        cSocket.send(sendPacket);
        if (verbose) {
            System.out.println("UDP paketti lähetetty, suljetaan UPD socket.");
        }
        // Suljetaan DatagramSocket kun UDP paketti on lähetetty
        cSocket.close();
    }

    /**
     * Sulkee portit
     *
     * @throws IOException jollei porttien sulkeminen onnistu
     */
    private void suljeSocketit() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }

    public ObjectOutputStream getoOut() {
        return oOut;
    }
} //SummausPalvelu
