import sun.security.provider.SHA;

import java.io.*;
import java.net.*;

//luokka luomaan porttiyhteys
public class SummausThread {
    private SharedData sd;


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

    //Luokka luomaan itse thread
    static class SummausThreadHandler extends Thread {
        private Socket clientSocket;
        private int indeksi;
        private SharedData sd;

        public SummausThreadHandler(Socket s, int i, SharedData sharedData) {
            indeksi = i;
            clientSocket = s;
            sd = sharedData;
        }

        public void run() {
            try {
                InputStream iS = clientSocket.getInputStream();
                OutputStream oS = clientSocket.getOutputStream();
                ObjectOutputStream oOut = new ObjectOutputStream(oS);
                ObjectInputStream oIn = new ObjectInputStream(iS);

                //testaukseen
                boolean lukujaJaljella = true;

                while (lukujaJaljella) {
                    int luku = oIn.readInt();
                    if (luku == 0) {
                        lukujaJaljella = false;
                    } else {
                        sd.summaa(indeksi, luku);
                    }
                }
                clientSocket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//SummausThreadHandler
} //SummausThread

