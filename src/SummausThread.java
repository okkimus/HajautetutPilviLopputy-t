import java.io.*;
import java.net.*;

//luokka luomaan porttiyhteys
public class SummausThread {

    //väliaikainen kovakoodattu portti
    private static int PORT=54121;

    public void SummausThread(int portti) {

        try {
        ServerSocket serverSocket = new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();

               System.out.println("Connection from" + clientSocket.getInetAddress() + " port " + clientSocket.getPort());

            //uusi thread
               new SummausThreadHandler(clientSocket).start();

           } catch (Exception e) {
            e.printStackTrace();
            }

    }

    //Luokka luomaan itse thread
    static class SummausThreadHandler extends Thread {
        private Socket clientSocket;

        public SummausThreadHandler(Socket s) {
            clientSocket = s;
        }

        public void run() {

            try {
                InputStream iS = clientSocket.getInputStream();
                OutputStream oS = clientSocket.getOutputStream();
                ObjectOutputStream oOut = new ObjectOutputStream(oS);
                ObjectInputStream oIn = new ObjectInputStream(iS);

                //testaukseen
                while (true) {
                    System.out.println(oIn.readObject());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//SummausThreadHandler
} //SummausThread

