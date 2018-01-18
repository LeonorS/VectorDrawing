package drawing.drawing.workspace;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.database.Drawing;
import drawing.drawing.vectordrawing.VectorDrawing;

import static drawing.drawing.vectordrawing.VectorDrawing.DRAWING_NAME;

public class Workspace extends AppCompatActivity {
    private static final String TAG = "KJKP6_WORKSPACE";
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawingAdapter drawingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        recyclerView = findViewById(R.id.rec_view);
        fab = findViewById(R.id.fab);
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Map<String, Drawing> drawingMap = Database.getInstance().getUser().getDrawings();
        final ArrayList<Drawing> drawings = new ArrayList(drawingMap.values());
        drawingAdapter = new DrawingAdapter(this, drawings, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                String name = drawings.get(itemPosition).name;
                Intent myIntent = new Intent(Workspace.this, VectorDrawing.class);
                myIntent.putExtra(DRAWING_NAME, name);
                startActivity(myIntent);
            }
        });
        recyclerView.setAdapter(drawingAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                Log.d(TAG, "scroll dy = " + dy);
                if (dy > 0  || dy < 0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Workspace.this, VectorDrawing.class);
                startActivity(myIntent);
                //dont call finish on this one
            }
        });
    }
}
