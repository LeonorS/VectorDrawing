package drawing.drawing.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import drawing.drawing.R;
import drawing.drawing.messaging.CustomProgressDialog;
import drawing.drawing.messaging.MessagingInterface;
import drawing.drawing.utils.NetworkHelper;


/**
 * Parade for FretX
 * Created by pandor on 26/08/17 16:52.
 */

public class ForgotPasswordFragment extends Fragment {
    private final static String TAG = "KJKP6_PASSWORD";
    private LoginInterface loginInterface;
    private Button recover;
    private EditText emailEdittext;
    private String email;

    public static ForgotPasswordFragment newInstance(LoginInterface loginInterface) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.loginInterface = loginInterface;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login_forgot_password, null);
        recover = (Button) root.findViewById(R.id.recover_button);
        emailEdittext = (EditText) root.findViewById(R.id.email_edittext);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkHelper.requireNetworkActivation(getActivity()))
                    return;

                resetPassword();

//                ReCaptchaHelper reCaptcha = new ReCaptchaHelper.Builder()
//                        .addOnSuccessListener(new ReCaptchaHelper.OnSuccessListener() {
//                            @Override
//                            public void onSuccess() {
//                                Log.d(TAG, "captcha has succeeded");
//                                resetPassword();
//                            }
//                        })
//                        .addOnFailureListener(new ReCaptchaHelper.OnFailureListener() {
//                            @Override
//                            public void onFailure() {
//                                Log.d(TAG, "captcha has failed");
//                            }
//                        })
//                        .build();
//                reCaptcha.verify(getActivity());
            }
        });
    }

    private void resetPassword() {
        email = emailEdittext.getText().toString();

        final MessagingInterface mi = CustomProgressDialog.newInstance(getActivity().getFragmentManager());
        mi.show(CustomProgressDialog.DialogType.PROGRESS, "Sending email...", null);

        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.useAppLanguage();
            auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Email sent to " + email);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Fragment signinFragment = SigninFragment.newInstance(loginInterface);
                            fragmentTransaction.replace(R.id.container, signinFragment, "selection");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commitAllowingStateLoss();
                            loginInterface.setCurrentFragment(signinFragment);
                            mi.show(CustomProgressDialog.DialogType.SUCCESS, "Email sent!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Email not sent: " + e.getMessage());
                            mi.show(CustomProgressDialog.DialogType.SUCCESS, "Email not sent!", e.getMessage());
                        }
                    });
        } catch (IllegalArgumentException e) {
            Log.w(TAG, e.toString());
            //ToDo implement email format validation during typing of email
        }
    }
}