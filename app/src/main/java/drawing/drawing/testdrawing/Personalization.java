package drawing.drawing.testdrawing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import drawing.drawing.R;
import drawing.drawing.model.Figure;
import drawing.drawing.model.PointFigure;
import drawing.drawing.vectordrawing.CustomView;
import drawing.drawing.vectordrawing.VectorDrawing;

/**
 * Created by leo on 14/01/18.
 */

public class Personalization extends AppCompatActivity {

    private TestDrawing customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalization);

        final Button ctn_btn = findViewById(R.id.ctn_btn);
        final TextView tv = findViewById(R.id.text);
        final LinearLayout layout = findViewById(R.id.drawingSpace);
        final boolean[] endingTest = {false};

        customView = new TestDrawing(Personalization.this);
        layout.addView(customView);

        layout.setVisibility(View.GONE);

        ctn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (endingTest[0] == false) {
                    ctn_btn.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(Personalization.this, VectorDrawing.class);
                    startActivity(intent);
                }
            }
        });

        customView.setCustomObjectListener(new TestDrawing.MyCustomObjectListener() {

            @Override
            public void endingTest(int point_margin, int seg_margin) {
                Log.d("DEBUG", "point_margin : " + point_margin + "; seg_margin : " + seg_margin);
                endingTest[0] = true;
                ctn_btn.setText("start");
                ctn_btn.setVisibility(View.VISIBLE);
                tv.setText("Very good ! You can start drawing.");
                tv.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            }
        });
    }
}
