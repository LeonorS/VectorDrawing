package drawing.drawing.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Parade for FretX
 * Created by pandor on 29/08/17 00:48.
 */

@IgnoreExtraProperties
public class User {

    private final static int DEFAULT_POINT_MARGIN = -1;
    private final static int DEFAULT_SEGMENT_MARGIN = -1;

    // Public attributes required for calls to DataSnapshot.getValue(User.class)
    public String username;
    public String email;
    public int point_margin;
    public int segment_margin;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.point_margin = DEFAULT_POINT_MARGIN;
        this.segment_margin = DEFAULT_SEGMENT_MARGIN;
    }

    public boolean isPrecisionSet() {
        return point_margin != DEFAULT_POINT_MARGIN && segment_margin != DEFAULT_SEGMENT_MARGIN;
    }

    public void setPrecision(int point_margin, int segment_margin) {
        this.point_margin = point_margin;
        this.segment_margin = segment_margin;
    }

}