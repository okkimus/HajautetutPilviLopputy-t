/**
 * Authors:
 * Jan-Mikael Ihanus, jamiih@utu,fi, 516329
 * Mikko Metsäranta, misame@utu.fi, 515662
 *
 * course: HAJAUTETUT OHJELMISTOJÄRJESTELMÄT JA PILVIPALVELUT, SYKSY 2017
 */


/**
 * Luokka kuvaa kartan mahdollisia kerroksia.
 *
 */
public class Layer {

    private String name;
    private String title;
    // Data XML:stä tulee muodossa "0" tai "1", joten alustetaan muuttujat -1:llä. Saadaan tietää, onko muuttujan tieto
    // käytettävästä datasta vai ei. (Defaultina Java alustaa kokonaisluvut arvolla 0.)
    private int queryable = -1;
    private int opaque = -1;
    private int cascaded = -1;

    public void setName(String name){
        this.name=name;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setQueryable(int queryable){
        this.queryable=queryable;
    }

    public void setOpaque(int opaque) {
        this.opaque = opaque;
    }

    public void setCascaded(int cascaded) {
        this.cascaded = cascaded;
    }

    public String getName(){
        return name;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Layer: NAME=" + name + ", TITLE=" + title + ", OPAQUE: " + opaque + ", QUERYABLE: " + queryable + ", CASCADED: " + cascaded;
    }
}
