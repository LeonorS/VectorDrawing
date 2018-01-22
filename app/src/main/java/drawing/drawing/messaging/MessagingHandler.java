package drawing.drawing.messaging;

/**
 * VectorDrawing for FretX
 * Created by pandor on 22/01/18 06:27.
 */

import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static drawing.drawing.messaging.CustomProgressDialog.DialogType.INFO;

/**
 * Message Handler class that supports buffering up of messages when the
 * activity is paused i.e. in the background.
 */
public class MessagingHandler extends PauseHandler {
    private static final String TAG = "KJKP6_MESSAGING_HANDLER";
    public final static int MSG_WHAT = ('F' << 16) + ('T' << 8) + 'A';
    public final static int MSG_SHOW_DIALOG = 1;
    public final static int MSG_DISMISS_DIALOG = 2;
    private CustomProgressDialog dialog;

    private MessagingHandler() {}

    private static class Holder {
        private static final MessagingHandler instance = new MessagingHandler();
    }

    public static MessagingHandler getInstance() {
        return Holder.instance;
    }

    /**
     * Activity instance
     */
    protected AppCompatActivity activity;

    /**
     * Set the activity associated with the handler
     *
     * @param activity
     *            the activity to set
     */
    public final void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    final protected boolean storeMessage(Message message) {
        // All messages are stored by default
        return true;
    }

    @Override
    final protected void processMessage(Message msg) {
        final AppCompatActivity activity = this.activity;
        if (activity != null) {
            switch (msg.what) {
                case MSG_WHAT:
                    switch (msg.arg1) {
                        case MSG_SHOW_DIALOG:
                            Log.d(TAG, "executing SHOW");
                            final FragmentManager fm = activity.getSupportFragmentManager();
                            final MessagingInfo info = (MessagingInfo) msg.obj;
                            dialog = CustomProgressDialog.newInstance();
                            dialog.show(fm, info.type, info.line1, info.line2);
                            break;
                        case MSG_DISMISS_DIALOG:
                            Log.d(TAG, "executing DISMISS");
                            if (dialog != null) {
                                dialog.dismiss();
                                dialog = null;
                            }
                            break;
                    }
                    break;
            }
        }
    }

    private class MessagingInfo {
        String line1;
        String line2;
        CustomProgressDialog.DialogType type;

        MessagingInfo(CustomProgressDialog.DialogType type, String line1, String line2) {
            this.type = type;
            this.line1 = line1;
            this.line2 = line2;
        }
    }

    public void show(CustomProgressDialog.DialogType type, String line1, String line2) {
        final MessagingInfo info = new MessagingInfo(type, line1, line2);
        handleMessage(obtainMessage(MSG_WHAT, MSG_SHOW_DIALOG, 0, info));
    }

    public void show(CustomProgressDialog.DialogType type, String line1) {
        show(type, line1, null);
    }

    public void show(String line1, String line2) {
        show(INFO, line1, line2);
    }

    public void show(String line1) {
        show(INFO, line1, null);
    }

    public void dismiss() {
        handleMessage(obtainMessage(MSG_WHAT, MSG_DISMISS_DIALOG, 0));
    }
}