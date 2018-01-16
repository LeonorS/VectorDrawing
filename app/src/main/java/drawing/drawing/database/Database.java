package drawing.drawing.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

/**
 * Parade for FretX
 * Created by pandor on 29/08/17 10:45.
 */

public class Database {
    private final static String TAG = "KJKP6_DATABASE";
    private FirebaseDatabase database;

    private DatabaseReference usersRef;
    private final Vector<UserListener> userListeners = new Vector<>();
    private User user = null;

    private Database() {
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        usersRef = database.getReference("users");
    }

    private static class Holder {
        private static final Database instance = new Database();
    }

    public static Database getInstance() {
        return Holder.instance;
    }

    //==============================================================================================
    //Todo remove race conditions
    public void addUserEventListener() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            usersRef.child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    Log.d(TAG, "Database.user updated!");
                    notifyUserListeners();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "Database.user update failed:" + databaseError.getMessage());
                    notifyUserListeners();
                }
            });
        }
    }

    public void addUserListenerWithoutNotifying(UserListener listener) {
        userListeners.add(listener);
    }

    public void addUserListener(UserListener listener) {
        userListeners.add(listener);
        listener.onUpdate(user);
    }

    public void removeUserListener(UserListener listener) {
        userListeners.remove(listener);
    }

    private void notifyUserListeners() {
        for (UserListener userListener: userListeners) {
            userListener.onUpdate(user);
        }
    }

    public void setUser(User user) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.child(fUser.getUid()).setValue(user);
        Log.d(TAG, "Database.user update requested!");
    }

    public User getUser() {
        return user;
    }
}
