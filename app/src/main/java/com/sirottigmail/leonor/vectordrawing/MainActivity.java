package com.sirottigmail.leonor.vectordrawing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    protected LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pointBtn = (Button) findViewById(R.id.pointBtn);
        Button clearBtn = (Button) findViewById(R.id.clearBtn);
        layout=(LinearLayout)findViewById(R.id.drawingSpace);
        final CustomView customView = new CustomView(MainActivity.this);
        layout.addView(customView);

        pointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.POINT_ACTION;
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.removeAllViews();
                layout.addView(new CustomView(MainActivity.this));
                customView.current_action = customView.DEFAULT_ACTION;
            }
        });
    }
}

