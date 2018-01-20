package drawing.drawing.utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by leo on 16/01/18.
 */

public class CrashAnalyticsHelper {
    private static final String TAG = "KJKP6_CRASHANALYTICS";

    public static boolean signIn(FirebaseUser fUser) {
        if (fUser == null) {
            Log.e(TAG, "Unable to log user, firebase user is null");
            return false;
        }
        Crashlytics.setUserIdentifier(fUser.getUid());
        Crashlytics.setUserEmail(fUser.getEmail());
        Crashlytics.setUserName(fUser.getDisplayName());
        return true;
    }

    public static boolean signOut() {
        return true;
    }

}
