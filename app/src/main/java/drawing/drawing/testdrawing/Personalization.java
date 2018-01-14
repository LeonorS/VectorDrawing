package drawing.drawing.testdrawing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        final LinearLayout layout= findViewById(R.id.drawingSpace);
        customView = new TestDrawing(Personalization.this);
        layout.addView(customView);

        layout.setVisibility(View.GONE);

        ctn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctn_btn.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        });
    }
}
