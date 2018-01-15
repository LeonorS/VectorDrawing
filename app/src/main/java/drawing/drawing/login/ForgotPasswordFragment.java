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

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(SafetyNet.API)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getActivity(), "Connection failed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        mGoogleApiClient.connect();

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyNet.SafetyNetApi.verifyWithRecaptcha(mGoogleApiClient, SiteKey)
                        .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                            @Override
                            public void onResult(SafetyNetApi.RecaptchaTokenResult result) {
                                Status status = result.getStatus();

                                //TODO: implement password recovery
                                if ((status != null) && status.isSuccess()) {
                                    Log.e(TAG, "success!");
                                } else {
                                    Log.e(TAG, "error happened! " + status.getStatusMessage());
                                }
                            }
                        });
            }
        });
    }
}