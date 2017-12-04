public class SharedData {
    private int[] summat;
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
     *
     */
    public int palautaKokonaissumma() {
        int summa = 0;
        for (int i : summat) {
            summa += i;
        }
        return summa;
    }

    /**
     * Palauttaa indeksin, jolla on suurin summa
     *
     */
    public int palautaSuurimmanSummanPalvelija() {
        int indeksi = 0;
        for (int i = 0; i < summat.length - 1; i++) {
            if (summat[i] < summat[i+1]) {
                indeksi = i;
            }
        }
        return indeksi;
    }

    public int palautaLukujenLukumaara() {
        return lkm;
    }


    public static int randomPort (int port_count_to_get){

        int min_port=1024;
        int max_port=65565;

        int PortRange = (max_port - min_port) + 1;
        return (int)(Math.random() * PortRange) + min_port;
    }

}
