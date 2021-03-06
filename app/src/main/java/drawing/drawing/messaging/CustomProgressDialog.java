package drawing.drawing.messaging;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import drawing.drawing.R;

import static android.view.View.GONE;
import static drawing.drawing.messaging.CustomProgressDialog.DialogType.PROGRESS;

/**
 * Created by leo on 18/01/18.
 */

public class CustomProgressDialog extends DialogFragment {
    private static final String TAG = "KJKP6_PROGRESS_DIALOG";
    private String line1;
    private String line2;
    private DialogType type;
    private ProgressBar progressBar;
    private LinearLayout singleLayout;
    private LinearLayout dualLayout;
    private TextView singleTxt;
    private TextView dualTxt1;
    private TextView dualTxt2;
    private boolean singleLineSelected = true;

    public enum DialogType {
        PROGRESS, FAIL, SUCCESS, WARNING, INFO
    }
    private DialogType currentType = PROGRESS;

    public static CustomProgressDialog newInstance() {
        CustomProgressDialog dialog = new CustomProgressDialog();
        dialog.setCancelable(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress);
        singleLayout = view.findViewById(R.id.single);
        dualLayout = view.findViewById(R.id.dual);
        singleTxt = view.findViewById(R.id.singleTxt);
        dualTxt1 = view.findViewById(R.id.dualTxt1);
        dualTxt2 = view.findViewById(R.id.dualTxt2);
        singleLayout.setVisibility(View.VISIBLE);
        dualLayout.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        updateContent();
    }

    //==============================================================================================
    void show(FragmentManager fragmentManager, DialogType type, String line1, String line2) {

        this.line1 = line1;
        this.line2 = line2;
        this.type = type;
        //Todo change this to allow the Show/Hide

        if (!isAdded()) {
            Log.d(TAG, "create dialog");
            super.show(fragmentManager, "messaging");
        }
        else {
            Log.d(TAG, "update existing dialog");
            updateContent();
        }
    }

    private void updateContent() {
        if (line2 != null) {
            dualTxt1.setText(line1);
            dualTxt2.setText(line2);
            setDualLine();
        } else {
            singleTxt.setText(line1);
            setSingleLine();
        }

        setType(type);
    }

    private void setSingleLine() {
        if (!singleLineSelected) {
            dualLayout.setVisibility(GONE);
            singleLayout.setVisibility(View.VISIBLE);
            singleLineSelected = !singleLineSelected;
        }
    }

    private void setDualLine() {
        if (singleLineSelected) {
            singleLayout.setVisibility(GONE);
            dualLayout.setVisibility(View.VISIBLE);
            singleLineSelected = !singleLineSelected;
        }
    }

    private void setType(DialogType type) {
        if (type == currentType)
            return;
        switch (type) {
            case PROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                currentType = PROGRESS;
                setCancelable(false);
                break;
            case FAIL: case INFO: case SUCCESS: case WARNING:
                setCancelable(true);
                break;
            default:
        }
        if (type != PROGRESS && currentType == PROGRESS) {
            currentType = type;
            progressBar.setVisibility(GONE);
        }
    }
}
