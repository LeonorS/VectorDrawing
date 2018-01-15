package drawing.drawing.vectordrawing;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import drawing.drawing.R;
import drawing.drawing.model.Figure;

public class VectorDrawing extends AppCompatActivity {


    private CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawing);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(VectorDrawing.this);
        final int point_margin = preferences.getInt("point_margin", 0);
        final int seg_margin = preferences.getInt("seg_margin", 0);

        final LinearLayout layout = findViewById(R.id.drawingSpace);
        double width = layout.getWidth();
        double height = layout.getHeight();
        customView = new CustomView(VectorDrawing.this, point_margin, seg_margin, width, height);
        layout.addView(customView);

        /*
        Button undoBtn = (Button) findViewById(R.id.undoBtn);
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.undo();
            }
        });

        Button redoBtn = findViewById(R.id.redoBtn);
        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.redo();
            }
        });

        Button cleanBtn = (Button) findViewById(R.id.cleanBtn);
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.DEFAULT_ACTION;
                customView.resetSelection();
                customView.figures = new ArrayList<Figure>();
            }
        });
        */

        Button clearBtn = (Button) findViewById(R.id.clearBtn);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.DEFAULT_ACTION;
                customView.resetSelection();
            }
        });

        Button pointBtn = findViewById(R.id.pointBtn);
        pointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.POINT_ACTION;
            }
        });

        Button selectBtn = findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.SELECT_ACTION;
            }
        });

        Button isoBtn = findViewById(R.id.isoBtn);
        isoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.makeIso();
                customView.current_action = customView.DEFAULT_ACTION;
            }
        });

        Button segBtn = findViewById(R.id.segBtn);
        segBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.SEG_ACTION;
            }
        });

        Button lineBtn = (Button) findViewById(R.id.lineBtn);

        lineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.LINE_ACTION;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_undo:
                customView.undo();
                return true;

            case R.id.action_redo:
                customView.redo();
                return true;

            case R.id.action_reset:
                customView.current_action = customView.DEFAULT_ACTION;
                customView.resetSelection();
                customView.figures = new ArrayList<Figure>();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}

