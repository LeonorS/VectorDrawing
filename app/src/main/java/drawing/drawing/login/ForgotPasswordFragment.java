package drawing.drawing.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import drawing.drawing.R;
import drawing.drawing.utils.Network;
import drawing.drawing.utils.ReCaptcha;


/**
 * Parade for FretX
 * Created by pandor on 26/08/17 16:52.
 */

public class ForgotPasswordFragment extends Fragment {
    private final static String TAG = "KJKP6_PASSWORD";
    private static final String SiteKey = "6LcpzjQUAAAAAD0dkI0uQ54c75TtJ_SVXaCGNaDT";
    private static final String SecretKey  = "6LcpzjQUAAAAACh_yXlvmcmOwMN1jdYD3KpvFO0c";
    private GoogleApiClient mGoogleApiClient;
    private LoginInterface loginInterface;
    private Button recover;
    private EditText emailEdittext;

    public static ForgotPasswordFragment newInstance(LoginInterface loginInterface) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.loginInterface = loginInterface;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_forgot_password_fragment, null);
        recover = (Button) root.findViewById(R.id.recover_button);
        emailEdittext = (EditText) root.findViewById(R.id.email_edittext);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Network.requireNetworkActivation(getActivity()))
            return;

        ReCaptcha reCaptcha = new ReCaptcha.Builder()
                .addOnSuccessListener(new ReCaptcha.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        //ToDo send recovery mail
                    }
                })
                .addOnFailureListener(new ReCaptcha.OnFailureListener() {
                    @Override
                    public void onFailure() {
                        Log.d(TAG, "captcha has failed");
                    }
                })
                .build();
        reCaptcha.verify(getActivity());
    }
}