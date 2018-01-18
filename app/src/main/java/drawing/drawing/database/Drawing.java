package drawing.drawing.database;

/**
 * VectorDrawing for FretX
 * Created by pandor on 17/01/18 17:13.
 */

public class Drawing {

    // Public attributes required for calls to DataSnapshot.getValue(User.class)
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
