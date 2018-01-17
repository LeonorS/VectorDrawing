package drawing.drawing.personalization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import drawing.drawing.R;
import drawing.drawing.workspace.Workspace;

/**
 * VectorDrawing for FretX
 * Created by pandor on 16/01/18 04:01.
 */

public class Personalization extends AppCompatActivity implements PersonalizationListener {
    public static final String OUTSIDE_WORKFLOW = "outsideWorkflow";
    private static final String TAG = "KJKP6_PERSONALIZATION";
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private boolean outsideWorkflow = false;

    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(OUTSIDE_WORKFLOW)) {
            outsideWorkflow = bundle.getBoolean(OUTSIDE_WORKFLOW);
        }

        setContentView(R.layout.activity_personalization);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = InfoFragment.newInstance(this, "Welcome !!!", "Let's measure your touch on the screen", "start");
        fragmentTransaction.add(R.id.container, fragment, "info");
        fragmentTransaction.commit();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "on complete call for state: " + state);
        FragmentTransaction fragmentTransaction;
        switch (state) {
            case 0:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = InfoFragment.newInstance(this, "", "Move the figures by touching them and slide your finger on the screen.", "continue");
                fragmentTransaction.replace(R.id.container, fragment, "info");
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = DrawingFragment.newInstance(this);
                fragmentTransaction.replace(R.id.container, fragment, "drawing");
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = InfoFragment.newInstance(this, "", "Very good ! You can start drawing.", "start");
                fragmentTransaction.replace(R.id.container, fragment, "info");
                fragmentTransaction.commit();
                break;
            default:
                if (!outsideWorkflow) {
                    Intent intent = new Intent(Personalization.this, Workspace.class);
                    startActivity(intent);
                } else {
                    setResult(RESULT_OK, new Intent());
                }
                Personalization.this.finish();
        }
        ++state;
    }
}