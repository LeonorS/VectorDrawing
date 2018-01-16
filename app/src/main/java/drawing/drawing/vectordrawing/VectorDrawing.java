package drawing.drawing.vectordrawing;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.login.Login;
import drawing.drawing.utils.JsonHelper;
import drawing.drawing.utils.NetworkHelper;

public class VectorDrawing extends AppCompatActivity {
    private static final String TAG = "KJKP6_VECTOR_DRAWING";
    private CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawing);

        final Database database = Database.getInstance();
        final User user = database.getUser();
        final int point_margin = user.getPointMargin();
        final int seg_margin = user.getSegmentMargin();

        final LinearLayout layout = findViewById(R.id.drawingSpace);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        customView = new CustomView(VectorDrawing.this, point_margin, seg_margin, metrics.widthPixels, metrics.heightPixels);
        layout.addView(customView);

        Button clearBtn = findViewById(R.id.clearBtn);
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
                customView.current_action = customView.ISO_ACTION;
                customView.makeIso();
            }
        });

        Button segBtn = findViewById(R.id.segBtn);
        segBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.SEG_ACTION;
            }
        });

        Button lineBtn = findViewById(R.id.lineBtn);
        lineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.LINE_ACTION;
            }
        });

        Button interBtn = findViewById(R.id.interBtn);
        interBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.current_action = customView.INTER_ACTION;
                customView.makeInter();
            }
        });

        //Todo create associated drawings
        //==========================================================================================
//        ImageView icon = new ImageView(this); // Create an icon
//        icon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
//        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
//                .setContentView(icon)
//                .setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER)
//                .build();
//        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
//
//        // repeat many times:
//        ImageView itemIcon1 = new ImageView(this);
//        itemIcon1.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
//        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
//
//        ImageView itemIcon2 = new ImageView(this);
//        itemIcon2.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
//        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
//
//        ImageView itemIcon3 = new ImageView(this);
//        itemIcon3.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
//        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();
//
//        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
//                .addSubActionView(button1)
//                .addSubActionView(button2)
//                .addSubActionView(button3)
//                .attachTo(actionButton)
//                .build();
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
            //todo move this to user profile activity
            case R.id.action_logout:
                Login.signout(this);
                Intent myIntent = new Intent(VectorDrawing.this, Login.class);
                startActivity(myIntent);
                finish();
                return true;

            case R.id.action_undo:
                customView.undo();
                return true;

            case R.id.action_redo:
                customView.redo();
                return true;

            case R.id.action_reset:
                customView.reset();
                return true;

            case R.id.action_save:

                if (NetworkHelper.requireNetworkActivation(this)) {
                    return true;
                }

                final String save = JsonHelper.saveToJson(customView.getModel());

                //Todo move this to drawing storage dedicated class
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference drawRef = storageRef.child("draws");
                StorageReference userDrawRef = drawRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                final StorageReference exampleRef = userDrawRef.child("example.json");

                UploadTask uploadTask = exampleRef.putBytes(save.getBytes());
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.w(TAG, "UploadFailed: " + exception.getMessage());
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.w(TAG, "UploadSucceeded: " + downloadUrl.toString());
                    }
                });
                Log.d(TAG, "JSON: " + save);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}

