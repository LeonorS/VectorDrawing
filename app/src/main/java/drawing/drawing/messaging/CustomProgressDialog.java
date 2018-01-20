package drawing.drawing.messaging;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import drawing.drawing.R;

import static android.view.View.GONE;
import static drawing.drawing.messaging.CustomProgressDialog.DialogType.INFO;
import static drawing.drawing.messaging.CustomProgressDialog.DialogType.PROGRESS;

/**
 * Created by leo on 18/01/18.
 */

public class CustomProgressDialog extends DialogFragment implements MessagingInterface {
    private static final String TAG = "KJKP6_PROGRESS_DIALOG";
    private FragmentManager fragmentManager;
    private String line1;
    private String line2;
    private DialogType type;
    private ProgressBar progressBar;
//    private ImageView imageView;
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

    //DialogType type, String line1, String line2
    public static MessagingInterface newInstance(FragmentManager fragmentManager) {
        CustomProgressDialog dialog = new CustomProgressDialog();
        dialog.fragmentManager = fragmentManager;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_info, container, false);

        progressBar = view.findViewById(R.id.progress);
//        imageView = view.findViewById(R.id.icon);
        singleLayout = view.findViewById(R.id.single);
        dualLayout = view.findViewById(R.id.dual);
        singleTxt = view.findViewById(R.id.singleTxt);
        dualTxt1 = view.findViewById(R.id.dualTxt1);
        dualTxt2 = view.findViewById(R.id.dualTxt2);

        singleLayout.setVisibility(View.VISIBLE);
        dualLayout.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        //imageView.setVisibility(GONE);
        setCancelable(false);

        show(type, line1, line2);
        return view;
    }

    //==============================================================================================
    public void show(DialogType type, String line1, String line2) {
        this.line1 = line1;
        this.line2 = line2;
        this.type = type;

        //Todo change this to allow the Show/Hide
        if (!isAdded())
            show(fragmentManager, "info");
        else {
            if (line2 != null) {
                dualTxt1.setText(line1);
                dualTxt2.setText(line2);
                setDualLine();
            } else {
                singleTxt.setText(line1);
                setSingleLine();
            }

//            setType(type);
        }
    }

    public void show(DialogType type, String line1) {
        show(type, line1, null);
    }

    public void show(String line1, String line2) {
        show(INFO, line1, null);
    }

    public void show(String line1) {
        show(INFO, line1, null);
    }

    public void allowCancellation() {
        setCancelable(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    //==============================================================================================
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
//                imageView.setVisibility(GONE);
                progressBar.setVisibility(View.VISIBLE);
                currentType = PROGRESS;
                setCancelable(false);
                break;
            case FAIL:
//                imageView.setImageResource(R.drawable.button_action);
                setCancelable(true);
                break;
            case INFO:
//                imageView.setImageResource(R.drawable.button_action);
                setCancelable(true);
                break;
            case SUCCESS:
//                imageView.setImageResource(R.drawable.button_action);
                setCancelable(true);
                break;
            case WARNING:
//                imageView.setImageResource(R.drawable.button_action);
                setCancelable(true);
                break;
            default:
        }

        if (type != PROGRESS && currentType == PROGRESS) {
            currentType = type;
            progressBar.setVisibility(GONE);
//            imageView.setVisibility(View.VISIBLE);
        }
    }
}
