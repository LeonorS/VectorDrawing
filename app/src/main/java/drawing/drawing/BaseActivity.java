package drawing.drawing;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import drawing.drawing.messaging.MessagingHandler;

/**
 * VectorDrawing for FretX
 * Created by pandor on 22/01/18 22:24.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "KJKP6_BASE_ACTIVITY";

//    @Override
//    protected void onCreate(Bundle saveInstance) {
//        Log.d(TAG, "Create");
//        MessagingHandler.getInstance().setActivity(this);
//        super.onCreate(saveInstance);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed");
        MessagingHandler.getInstance().setActivity(this);
        MessagingHandler.getInstance().resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Paused");
        MessagingHandler.getInstance().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroyed");
        MessagingHandler.getInstance().setActivity(null);
    }
}
