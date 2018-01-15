package drawing.drawing.splashscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import drawing.drawing.R;
import drawing.drawing.login.Login;
import drawing.drawing.vectordrawing.VectorDrawing;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        final boolean firstLaunch = !sharedPref.contains(getString(R.string.preference_first_launch));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent myIntent = new Intent(SplashScreen.this, VectorDrawing.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(SplashScreen.this, Login.class);
                    startActivity(myIntent);
                }
            }
        }, SPLASH_SCREEN_DELAY);


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (firstLaunch) {
//                    Intent myIntent = new Intent(SplashScreen.this, MainActivity.class);
//                    startActivity(myIntent);
//                } else {
//                    Intent myIntent = new Intent(SplashScreen.this, VectorDrawingApp.class);
//                    startActivity(myIntent);
//                }
//                SplashScreen.this.finish();
//            }
//        }, SPLASH_SCREEN_DELAY);
    }
}
