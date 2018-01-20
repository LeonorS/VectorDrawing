package drawing.drawing.database;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leo on 17/01/18.
 */

@IgnoreExtraProperties
public class User {
    private final static String TAG = "KJKP6_USER";
    private final static int DEFAULT_POINT_MARGIN = -1;
    private final static int DEFAULT_SEGMENT_MARGIN = -1;
    public String username;
    public String email;
    public int point_margin;
    public int segment_margin;
    public Map<String, Drawing> drawings;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        drawings = new HashMap<>();
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.point_margin = DEFAULT_POINT_MARGIN;
        this.segment_margin = DEFAULT_SEGMENT_MARGIN;
        this.drawings = new HashMap<>();
    }

    public boolean isPrecisionSet() {
        return point_margin != DEFAULT_POINT_MARGIN && segment_margin != DEFAULT_SEGMENT_MARGIN;
    }

    public void setPrecision(int point_margin, int segment_margin) {
        this.point_margin = point_margin;
        this.segment_margin = segment_margin;
        Log.d(TAG, "point_margin : " + point_margin + "; seg_margin ; " + segment_margin );
    }

    public Map<String, Drawing> getDrawings() {
        return drawings;
    }

    public void addDrawing(String name) {
        final Date currentTime = Calendar.getInstance().getTime();
        final Drawing drawing = new Drawing(name, currentTime.toString(), currentTime.toString());
        final Database database = Database.getInstance();
        final User user = database.getUser();
        drawings.put(name, drawing);
        database.setUser(user);
        Log.d(TAG, "saving new user file named: " + name);
    }

    public boolean isOverwriting(String name) {
        return drawings.keySet().contains(name);
    }
}