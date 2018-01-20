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
import android.widget.Button;
import android.widget.EditText;

import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.messaging.CustomProgressDialog;
import drawing.drawing.messaging.MessagingInterface;
import drawing.drawing.model.Model;
import drawing.drawing.storage.Storage;

/**
 * Created by leo on 17/01/18.
 */

public class SavingDialogFragment extends DialogFragment {
    private static final String TAG = "KJKP6_SAVING_DIALOG";
    private Bitmap preview;
    private Model model;
    private OnSaveListener listener;
    private EditText edt;
    private MessagingInterface messagingInterface;
    private boolean override;

    public interface OnSaveListener {
        void onSave(String name);
        void onCancel();
    }

    public static SavingDialogFragment newInstance(Model model, Bitmap preview, @Nullable OnSaveListener listener, MessagingInterface messagingInterface) {
        SavingDialogFragment dialog = new SavingDialogFragment();
        dialog.model = model;
        dialog.preview = preview;
        dialog.listener = listener;
        dialog.messagingInterface = messagingInterface;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_save, null);
        builder.setView(view);
        edt = view.findViewById(R.id.editText);
        builder.setPositiveButton("save", null)
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

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = edt.getText().toString();
                if (name.isEmpty())
                    return;
                if (!override && Database.getInstance().getUser().isOverwriting(name)) {
                    messagingInterface.show(CustomProgressDialog.DialogType.WARNING,
                            "A file with this name already exist",
                            "Reclick save to overwrite, else click cancel");
                    override = true;
                    return;
                }
                Database.getInstance().getUser().addDrawing(name);
                Storage.getInstance().setModel(getActivity(), name, model, null);
                Storage.getInstance().setPreview(getActivity(), name, preview, null);
                if (listener != null) {
                    listener.onSave(name);
                }
                dismiss();
                //todo handle saving errors
            }
        });
    }
}