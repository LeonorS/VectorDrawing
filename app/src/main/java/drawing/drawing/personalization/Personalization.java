package drawing.drawing.personalization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import drawing.drawing.R;
import drawing.drawing.vectordrawing.VectorDrawing;

/**
 * VectorDrawing for FretX
 * Created by pandor on 16/01/18 04:01.
 */

public class Personalization extends AppCompatActivity implements PersonalizationListener {
    private static final String TAG = "KJKP6_PERSONALIZATION";
    private FragmentManager fragmentManager;
    private Fragment fragment;

    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Intent intent = new Intent(Personalization.this, VectorDrawing.class);
                startActivity(intent);
        }
        ++state;
    }
}