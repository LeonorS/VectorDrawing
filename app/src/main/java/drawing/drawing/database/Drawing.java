package drawing.drawing.database;

/**
 * Created by leo on 12/01/18.
 */

public class Drawing {

    public String name;
    public String lastUpdate;
    public String creation;

    public Drawing() {
        // Default constructor required for calls to DataSnapshot.getValue(Drawing.class)
    }

    public Drawing(String name, String creation, String lastUpdate) {
        this.name = name;
        this.lastUpdate = lastUpdate;
        this.creation = creation;
    }
}
