package drawing.drawing.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

/**
 * Created by leo on 15/01/18.
 */

public class NetworkHelper {
    public static boolean requireNetworkActivation(Activity activity) {
        if (!isOnline(activity)) {
            NoInternetConnectionDialog internetDialog = new NoInternetConnectionDialog();
            internetDialog.show(activity.getFragmentManager(), "NETWORK");
            return true;
        } else {
            return false;
        }
    }

    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } else {
            return false;
        }
    }

    public static class NoInternetConnectionDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Internet connection is required, please activate it and retry")
                    .setTitle("No internet connection")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            return builder.create();
        }
    }
}
