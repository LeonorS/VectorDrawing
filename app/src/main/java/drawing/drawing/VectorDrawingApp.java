package drawing.drawing;

import android.app.Application;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import io.fabric.sdk.android.Fabric;

/**
 * Parade for FretX
 * Created by pandor on 28/08/17 15:41.
 */

public class VectorDrawingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Twitter.initialize(this);
        AndroidNetworking.initialize(getApplicationContext());
    }
}
