import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;

public class SharedData {
    public int[] summat;
    private int lkm;

    public SharedData(int summausPavelijoidenLkm) {
        this.summat = new int[summausPavelijoidenLkm];
        this.lkm = 0;
    }

    /**
     * Summaa summattavan luvun listalla olevaan indeksiin (palvelijanNumero)
     * ja kasvattaa SharedDatan lukumäärä kenttää yhdellä
     */
    public synchronized void summaa(int palvelijanNumero, int summattava) {
        summat[palvelijanNumero] += summattava;
        lkm++;
    }

    /**
     * Laskee summat listan lukujen summan ja palauttaa sen
     */
    public synchronized int palautaKokonaissumma() {
        //System.out.println(Arrays.toString(summat));
        int summa = 0;
        for (int i : summat) {
            summa += i;
        }
        return summa;
    }

    /**
     * Palauttaa indeksin, jolla on suurin summa
     */
    public synchronized int palautaSuurimmanSummanPalvelija() {
        //System.out.println(Arrays.toString(summat));
        int indeksi = 0;
        for (int i = 0; i < summat.length; i++) {
            if (summat[indeksi] < summat[i]) {
                indeksi = i;
            }
        }
        return indeksi + 1;
    }

    public int palautaLukujenLukumaara() {

        //System.out.println(Arrays.toString(summat));
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
}
