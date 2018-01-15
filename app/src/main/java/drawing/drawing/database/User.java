package drawing.drawing.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Parade for FretX
 * Created by pandor on 29/08/17 00:48.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}