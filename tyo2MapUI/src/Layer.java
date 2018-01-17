public class Layer {

    private String name;
    private String title;
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
