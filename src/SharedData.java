import java.io.IOException;
import java.net.ServerSocket;


/**
 * Authors:
 * Mikko Metsäranta, misame@utu.fi, 515662
 * Jan-Mikael Ihanus, jamiih@utu,fi, 516329
 *
 * course: HAJAUTETUT OHJELMISTOJÄRJESTELMÄT JA PILVIPALVELUT, SYKSY 2017
 */


/**
 * Luokka SharedData, johon tallennetaan summauspalvelijoiden tietoja liittyen summiin
 *
 */

public class SharedData {
    public int[] summat; //taulukko, joka tallentaa yhteen soluun yhden summauspalvelijan summan
    private int lkm; // seuraa lukujen lukumäärää


    /**
     * Luo SharedData -olion
     * @param summausPavelijoidenLkm summauspalvelijoiden lukumäärä
     */

    public SharedData(int summausPavelijoidenLkm) {
        this.summat = new int[summausPavelijoidenLkm];
        this.lkm = 0;
    }

    /**
     * @param palvelijanNumero mikä summauspalvelija on kyseessä
     * @param summattava luku, joka lisätään ko. summauspalvelijan summaan
     *
     * metodi kasvattaa lkm:n arvoa yhdellä
     */

    public synchronized void summaa(int palvelijanNumero, int summattava) {
        summat[palvelijanNumero] += summattava;
        lkm++;
    }

    /**
     * Laskee summat listan lukujen summan ja palauttaa sen
     */

    public synchronized int palautaKokonaissumma() {

        int summa = 0;
        for (int i : summat) {
            summa += i;
        }
        return summa;
    }

    /**
     *
     * @return palauttaa summauspalvelijan numeron jolla on suurin summa
     */

    public synchronized int palautaSuurimmanSummanPalvelija() {

        int indeksi = 0;
        for (int i = 0; i < summat.length; i++) {
            if (summat[indeksi] < summat[i]) {
                indeksi = i;
            }
        }
        return indeksi + 1;
    }

    /**
     *
     * @return palauttaa Y:ltä välitettyjen lukujen lukumäärän X:lle
     */

    public int palautaLukujenLukumaara() {

        return lkm;
    }

    /**
     * @param porttiLkm kertoo halutun määrän portteja joita halutaan (satunnainen portti tietyssä välissä)
     * @return portit palauttaa vektorin porttiLkm:n eri portteja
     */

    public static int[] satunnaisetPortit(int porttiLkm) {

        int min_port = 49152;
        int max_port = 65565;
        int PortRange = (max_port - min_port) + 1;
        int[] portit = new int[porttiLkm];
        int port;

        //käydään portit läpi ja tallennetaan taulukkoon
        for (int i = 0; i < porttiLkm; i++) {
            port = (int) (Math.random() * PortRange) + min_port;
            if (onkoPorttiVapaa(port)) {
                portit[i] = port;
            } else i--;
        }
        return portit;
    }

    /**
     * @param portti on se portti joka tarkistetaan, että onko se vapaana käytettäväksi
     * @return palauttaa true, jos haluttu portti on vapaana, false jos porttia ei saada suljettua (käytössä)
     */

    public static boolean onkoPorttiVapaa(int portti) {
        try {
            new ServerSocket(portti).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
} //SharedData
