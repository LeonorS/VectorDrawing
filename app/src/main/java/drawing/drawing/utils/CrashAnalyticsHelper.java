package drawing.drawing.utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseUser;

/**
 * VectorDrawing for FretX
 * Created by pandor on 16/01/18 15:49.
 */

public class CrashAnalyticsHelper {
    private static final String TAG = "KJKP6_CRASHANALYTICS";

    public static boolean logUser(FirebaseUser fUser) {
        if (fUser == null) {
            Log.e(TAG, "Unable to log user, firebase user is null");
            return false;
        }
        Crashlytics.setUserIdentifier(fUser.getUid());
        Crashlytics.setUserEmail(fUser.getEmail());
        Crashlytics.setUserName(fUser.getDisplayName());
        return true;
    }
}
