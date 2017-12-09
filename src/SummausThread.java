/**
 * Authors:
 * Mikko Metsäranta, misame@utu.fi, 515662
 * Jan-Mikael Ihanus, jamiih@utu,fi, 516329
 *
 * course: HAJAUTETUT OHJELMISTOJÄRJESTELMÄT JA PILVIPALVELUT, SYKSY 2017
 *
 */


import java.io.*;
import java.net.*;

/**
 * Luokka summauspalvelijoiden luomiseen ja hallinnoimiseen
 */

public class SummausThread {
    private SharedData sd;


    /**
     *
     * @param portit lisältää tarvittavat portit summauspalvelijoille
     * @param lkm montako palvelijaa tarvitaan
     * @param sd jaetun luokkatiedoston osoittaminen
     */
    public SummausThread(int[] portit, int lkm, SharedData sd) {
        this.sd = sd;
        for(int i=0; i<lkm; i++) {
            try {
                ServerSocket serverSocket = new ServerSocket(portit[i]);
                Socket clientSocket = serverSocket.accept();
                System.out.println("Luotiin " + i + ". summausPalvelija sockettiin " + serverSocket);
                System.out.println("Connection from" + clientSocket.getInetAddress() + " port " + clientSocket.getPort());

                //uusi thread
                new SummausThreadHandler(clientSocket, i, sd).start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Luokka luomaan itse thread
     */

    static class SummausThreadHandler extends Thread {
        private Socket clientSocket;
        private int indeksi;
        private SharedData sd;

        /**
         *
         * @param s viittaa sokettiin
         * @param i vastaa indeksiä, joka on siis kuinka mones luku on kyseessä jonka Workdistributor on lähettänyt
         * @param sharedData viittaa SharedData -luokkatiedostoon, joka tallentaa/lokeroi summauslukuja
         */

        public SummausThreadHandler(Socket s, int i, SharedData sharedData) {
            indeksi = i;
            clientSocket = s;
            sd = sharedData;
        }

        public void run() {
            try {

                // streamit kohdilleen
                InputStream iS = clientSocket.getInputStream();
                OutputStream oS = clientSocket.getOutputStream();
                ObjectOutputStream oOut = new ObjectOutputStream(oS);
                ObjectInputStream oIn = new ObjectInputStream(iS);

                //alustaa onko lukuja jäljellä streamissä
                boolean lukujaJaljella = true;

                while (lukujaJaljella) {
                    int luku = oIn.readInt();

                    // testaukseen
                    //System.out.println("WorkDistributorilta tullut luku: "+ luku);

                    if (luku == 0) {
                        lukujaJaljella = false;
                    } else {
                        synchronized (sd){
                            sd.summaa(indeksi, luku);
                        }
                    }
                }
                clientSocket.close();
            }catch (EOFException e) {

            } catch (IOException closeException) {
                // hallittu exceptionin hallinta
                closeException.printStackTrace();
            }
        }
    }//SummausThreadHandler
} //SummausThread

