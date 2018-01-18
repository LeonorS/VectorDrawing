package drawing.drawing.vectordrawing;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.model.Model;
import drawing.drawing.storage.Storage;

/**
 * VectorDrawing for FretX
 * Created by pandor on 17/01/18 21:52.
 */

public class SavingDialogFragment extends DialogFragment {
    private static final String TAG = "";
    private Bitmap preview;
    private Model model;
    private OnSaveListener listener;

    public interface OnSaveListener {
        void onSave(String name);
        void onCancel();
    }

    public static SavingDialogFragment newInstance(Model model, Bitmap preview, @Nullable OnSaveListener listener) {
        SavingDialogFragment dialog = new SavingDialogFragment();
        dialog.model = model;
        dialog.preview = preview;
        dialog.listener = listener;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_save, null);
        builder.setView(view);

        final EditText edt = view.findViewById(R.id.editText);

        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String name = edt.getText().toString();
                        if (name.isEmpty())
                            return;
                        Database.getInstance().getUser().addDrawing(name);
                        Storage.getInstance().setModel(getActivity(), name, model, null);
                        Storage.getInstance().setPreview(getActivity(), name, preview, null);
                        if (listener != null)
                            listener.onSave(name);
                        //todo handle saving errors
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SavingDialogFragment.this.getDialog().cancel();
                        if (listener != null)
                            listener.onCancel();
                    }
                });
        return builder.create();
    }
}