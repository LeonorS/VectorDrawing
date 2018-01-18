package drawing.drawing.vectordrawing;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.login.Login;
import drawing.drawing.model.Model;
import drawing.drawing.personalization.Personalization;
import drawing.drawing.storage.Storage;

import static drawing.drawing.personalization.Personalization.OUTSIDE_WORKFLOW;

public class VectorDrawing extends AppCompatActivity {
    public static final String DRAWING_NAME = "drawingName";
    private static final int PRECISION_REQUEST_CODE = 42;
    private static final String TAG = "KJKP6_VECTOR_DRAWING";
    private CustomView customView;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawing);

        customView = findViewById(R.id.drawingSpace);

        final Database database = Database.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(DRAWING_NAME)) {
            name = bundle.getString(DRAWING_NAME);
            Log.d(TAG, "Loading file: " + name);
            setTitle(name);
            Storage.getInstance().getModel(name, new Storage.OnStorageCompleteListener() {
                @Override
                public void onSuccess(Object obj) {
                    customView.setModel((Model)obj);
                }

                @Override
                public void onFailure(String error) {
                    finish();
                }
            });
        }

        final User user = database.getUser();
        customView.getModel().setPrecision(user.point_margin, user.segment_margin);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        customView.getModel().setSize(metrics.widthPixels, metrics.heightPixels);
//width, height, point_margin, seg_margin

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
            case R.id.action_precision:
                Intent myIntent = new Intent(VectorDrawing.this, Personalization.class);
                myIntent.putExtra(OUTSIDE_WORKFLOW, true);
                startActivityForResult(myIntent, PRECISION_REQUEST_CODE);
                return true;

            //todo move this to user profile activity
            case R.id.action_logout:
                Login.signout(this, new Login.OnSignoutCompleteListener() {
                    @Override
                    public void onComplete() {
                        Intent myIntent = new Intent(VectorDrawing.this, Login.class);
                        startActivity(myIntent);
                        finish();
                    }
                });
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
                SavingDialogFragment savingDialog = SavingDialogFragment.newInstance(customView.getModel(), customView.getPreview(), new SavingDialogFragment.OnSaveListener() {
                    @Override
                    public void onSave(String name) {
                        VectorDrawing.this.name = name;
                        setTitle(name);
                    }
                    @Override
                    public void onCancel() {
                    }
                });
                savingDialog.show(getSupportFragmentManager(), "saving");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PRECISION_REQUEST_CODE) {
            final User user = Database.getInstance().getUser();
            Log.d(TAG, "update precision: " + user.point_margin + " | " + user.segment_margin);
            customView.getModel().setPrecision(user.point_margin, user.segment_margin);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Save work before exiting?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (name != null) {
                            Storage.getInstance().setModel(VectorDrawing.this, name, customView.getModel(), null);
                            Storage.getInstance().setPreview(VectorDrawing.this, name, customView.getPreview(), null);
                            VectorDrawing.this.finish();
                        } else {
                            SavingDialogFragment savingDialog = SavingDialogFragment.newInstance(customView.getModel(), customView.getPreview(), new SavingDialogFragment.OnSaveListener() {
                                @Override
                                public void onSave(String name) {
                                    VectorDrawing.this.name = name;
                                    setTitle(name);
                                    VectorDrawing.this.finish();
                                }
                                @Override
                                public void onCancel() {
                                    Log.d(TAG, "failed to save work");
                                }
                            });
                            savingDialog.show(getSupportFragmentManager(), "saving");
                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VectorDrawing.this.finish();
                    }
                })
                .create()
                .show();
    }
}

