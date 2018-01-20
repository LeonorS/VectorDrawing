package drawing.drawing.vectordrawing;

/**
 * Created by leo on 03/12/17.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import drawing.drawing.controller.Controller;
import drawing.drawing.messaging.CustomProgressDialog;
import drawing.drawing.messaging.MessagingInterface;
import drawing.drawing.profile.Profile;
import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.model.Model;
import drawing.drawing.storage.Storage;

import static drawing.drawing.messaging.CustomProgressDialog.DialogType.PROGRESS;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.DEFAULT_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.ISO_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.LINE_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.POINT_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.SEG_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.SELECT_ACTION;

public class VectorDrawing extends AppCompatActivity implements ControllerActivityInterface {
    public static final String DRAWING_NAME = "drawingName";
    private static final String TAG = "KJKP6_VECTOR_DRAWING";
    private static final int SIZE = 125;
    private String name;
    private MessagingInterface messagingInterface;

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawing);

        messagingInterface = CustomProgressDialog.newInstance(getFragmentManager());
        final DrawingView drawingView = findViewById(R.id.drawingSpace);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(DRAWING_NAME)) {
            name = bundle.getString(DRAWING_NAME);
            Log.d(TAG, "Loading file: " + name);
            setTitle(name);
            messagingInterface.show(PROGRESS, "Loading saved work");
            Storage.getInstance().getModel(name, new Storage.OnStorageCompleteListener() {
                @Override
                public void onSuccess(Object obj) {
                    Model model = (Model) obj;
                    final User user = Database.getInstance().getUser();
                    model.setPrecision(user.point_margin, user.segment_margin);
                    controller = new Controller(model, drawingView, VectorDrawing.this);
                    drawingView.invalidate();
                    messagingInterface.dismiss();
                }

                @Override
                public void onFailure(String error) {
                    finish();
                }
            });
        } else {
            final Model model = new Model();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            model.setSize(metrics.widthPixels, metrics.heightPixels);
            controller = new Controller(model, drawingView, this);
        }

        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageDrawable(getResources().getDrawable(R.drawable.drag_drop));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
        button1.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.CENTER));

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.current_action = DEFAULT_ACTION;
                drawingView.resetSelection();
            }
        });

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageDrawable(getResources().getDrawable(R.drawable.select));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        button2.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.CENTER));

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.current_action = SELECT_ACTION;
            }
        });

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageDrawable(getResources().getDrawable(R.drawable.point));
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();
        button3.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.CENTER));

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.current_action = POINT_ACTION;
            }
        });

        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageDrawable(getResources().getDrawable(R.drawable.iso));
        SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();
        button4.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.CENTER));

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.current_action = ISO_ACTION;
                drawingView.makeIso();
                Log.d("VectorDrawing !!!!!!!!!", "make iso done");
            }
        });

        ImageView itemIcon5 = new ImageView(this);
        itemIcon5.setImageDrawable(getResources().getDrawable(R.drawable.seg));
        SubActionButton button5 = itemBuilder.setContentView(itemIcon5).build();
        button5.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.CENTER));

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.current_action = SEG_ACTION;
            }
        });

        ImageView itemIcon6 = new ImageView(this);
        itemIcon6.setImageDrawable(getResources().getDrawable(R.drawable.line));
        SubActionButton button6 = itemBuilder.setContentView(itemIcon6).build();
        button6.setLayoutParams(new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.CENTER));

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.current_action = LINE_ACTION;
            }
        });

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .addSubActionView(button5)
                .addSubActionView(button6)
                .attachTo(actionButton)
                .setStartAngle(180)
                .setEndAngle(360)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (controller != null)
            controller.updatePrecision();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawing_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem undo = menu.findItem(R.id.action_undo);
        undo.setEnabled(controller != null && controller.canUndo());
        final MenuItem redo = menu.findItem(R.id.action_redo);
        redo.setEnabled(controller != null && controller.canRedo());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent myIntent = new Intent(VectorDrawing.this, Profile.class);
                startActivity(myIntent);
                return true;

            case R.id.action_undo:
                controller.undo();
                return true;

            case R.id.action_redo:
                controller.redo();
                return true;

            case R.id.action_reset:
                controller.reset();
                return true;

            case R.id.action_save:
                SavingDialogFragment savingDialog = SavingDialogFragment.newInstance(controller.getModel(), controller.getView().getPreview(), new SavingDialogFragment.OnSaveListener() {
                    @Override
                    public void onSave(String name) {
                        VectorDrawing.this.name = name;
                        setTitle(name);
                    }
                    @Override
                    public void onCancel() {
                    }
                }, messagingInterface);
                savingDialog.show(getSupportFragmentManager(), "saving");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Save work before exiting?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (name != null) {
                            Storage.getInstance().setModel(VectorDrawing.this, name, controller.getModel(), null);
                            Storage.getInstance().setPreview(VectorDrawing.this, name, controller.getView().getPreview(), null);
                            VectorDrawing.this.finish();
                        } else {
                            SavingDialogFragment savingDialog = SavingDialogFragment.newInstance(controller.getModel(), controller.getView().getPreview(), new SavingDialogFragment.OnSaveListener() {
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
                            }, messagingInterface);
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

    public void invalidateOptionMenu() {
        super.invalidateOptionsMenu();
    }
}

