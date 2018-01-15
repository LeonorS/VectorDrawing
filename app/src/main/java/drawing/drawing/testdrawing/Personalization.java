package drawing.drawing.testdrawing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import drawing.drawing.R;
import drawing.drawing.vectordrawing.VectorDrawing;

/**
 * Created by leo on 14/01/18.
 */

public class Personalization extends AppCompatActivity {

    private TestDrawing customView;
    private LinearLayout layout;
    private boolean drawingViewVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization);

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
                    final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.preference_first_launch), false);
                    editor.commit();

                    Intent intent = new Intent(Personalization.this, VectorDrawing.class);
                    startActivity(intent);
                    Personalization.this.finish();
                }
            }
        });

        customView.setCustomObjectListener(new TestDrawing.MyCustomObjectListener() {

            @Override
            public void endingTest(int point_margin, int seg_margin) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Personalization.this);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putInt("point_margin", point_margin);
                edit.apply();
                edit.putInt("seg_margin", seg_margin);
                edit.apply();

                endingTest[0] = true;
                ctn_btn.setText("start");
                tv.setText("Very good ! You can start drawing.");
                toggleVisibility();
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
}
