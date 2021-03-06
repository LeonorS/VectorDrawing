package drawing.drawing.splashscreen;

/**
 * Created by leo on 13/01/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import drawing.drawing.BaseActivity;
import drawing.drawing.R;
import drawing.drawing.workspace.Workspace;
import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.database.UserListener;
import drawing.drawing.login.Login;
import drawing.drawing.personalization.Personalization;
import drawing.drawing.utils.CrashAnalyticsHelper;

public class SplashScreen extends BaseActivity {
    private static final String TAG = "KJKP6_SPLASH_SCREEN";
    private static final int SPLASH_SCREEN_DELAY = 2000;

    private UserListener userDataCheckListener = new UserListener() {
        @Override
        public void onUpdate(User user) {
            Database.getInstance().removeUserListener(this);
            if (user == null) {
                Log.w(TAG, "user is null");
                Intent myIntent = new Intent(SplashScreen.this, Login.class);
                startActivity(myIntent);
                finish();
            } else if (!user.isPrecisionSet()) {
                Log.w(TAG, "user " + FirebaseAuth.getInstance().getCurrentUser().getUid() + " precision not set");
                CrashAnalyticsHelper.signIn(FirebaseAuth.getInstance().getCurrentUser());
                Intent myIntent = new Intent(SplashScreen.this, Personalization.class);
                startActivity(myIntent);
                finish();
            } else {
                Log.w(TAG, "user is old");
                CrashAnalyticsHelper.signIn(FirebaseAuth.getInstance().getCurrentUser());
                Intent myIntent = new Intent(SplashScreen.this, Workspace.class);
                startActivity(myIntent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Log.w(TAG, "user is not logged in");
                    Intent myIntent = new Intent(SplashScreen.this, Login.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    Log.w(TAG, "user is logged in");
                    Database.getInstance().addUserListenerWithoutNotifying(userDataCheckListener);
                    //Todo should be hidden in database builder - Race condition prone
                    Database.getInstance().addUserEventListener();
                }
            }
        }, SPLASH_SCREEN_DELAY);
    }
}
