public class Layer {

    private String name;
    private String title;
    private int queryable = 0;
    private int opaque = 0;
    private int cascaded = 0;

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
        return "Layer: NAME=" + name + ", TITLE=" + title;
    }
}
