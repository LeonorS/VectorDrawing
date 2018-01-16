package drawing.drawing.personalization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.database.UserListener;
import drawing.drawing.vectordrawing.VectorDrawing;

/**
 * Created by leo on 14/01/18.
 */

public class Personalization_old extends AppCompatActivity {
    private static final String TAG = "KJKP6_PERSONALIZATION";

    private TestDrawing customView;
    private LinearLayout layout;
    private boolean drawingViewVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization_old);

        final Button ctn_btn = findViewById(R.id.ctn_btn);
        final TextView tv = findViewById(R.id.text);
        final boolean[] endingTest = {false};

        layout = findViewById(R.id.layout);
        customView = findViewById(R.id.drawing_view);

        initVisibility();

        ctn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (endingTest[0] == false) {
                    toggleVisibility();
                } else {
//                    final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putBoolean(getString(R.string.preference_first_launch), false);
//                    editor.commit();

                    Intent intent = new Intent(Personalization_old.this, VectorDrawing.class);
                    startActivity(intent);
                    Personalization_old.this.finish();
                }
            }
        });

        customView.setCustomObjectListener(new TestDrawing.MyCustomObjectListener() {

            @Override
            public void endingTest(int point_margin, int seg_margin) {

                //Todo race condition if going to next activity too fast
                final Database database = Database.getInstance();
                final User user = database.getUser();
                user.setPrecision(point_margin, seg_margin);
                Database.getInstance().addUserListenerWithoutNotifying(userDataUpdateListener);
                Log.d(TAG, "set user");
                database.setUser(user);

//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Personalization_old.this);
//                SharedPreferences.Editor edit = preferences.edit();
//                edit.putInt("point_margin", point_margin);
//                edit.apply();
//                edit.putInt("seg_margin", seg_margin);
//                edit.apply();

                endingTest[0] = true;
                ctn_btn.setText("start");
                tv.setText("Very good ! You can start drawing.");
            }
        });
    }

    private void initVisibility() {
        drawingViewVisible = false;
        customView.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
    }

    private void toggleVisibility() {
        if (drawingViewVisible) {
            customView.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        } else {
            customView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }
        drawingViewVisible = !drawingViewVisible;
    }

    private final UserListener userDataUpdateListener = new UserListener() {
        @Override
        public void onUpdate(User user) {
            Log.d(TAG, "user updated");
            toggleVisibility();
        }
    };
}
