package drawing.drawing.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import drawing.drawing.messaging.MessagingInterface;
import drawing.drawing.utils.NetworkHelper;
import drawing.drawing.R;
import drawing.drawing.utils.ReCaptchaHelper;

/**
 * Parade for FretX
 * Created by pandor on 26/08/17 17:07.
 */

public class RegisterFragment extends Fragment {
    private static final String TAG = "KJKP6_LOGIN_REGISTER";
    private static final int PASSWORD_MIN_LENGTH = 6;
    private LoginInterface loginInterface;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private View root;
    private String email;
    private String username;
    private String password;
    private boolean passwordVisible = false;
    private MessagingInterface messagingInterface;


    public static RegisterFragment newInstance(LoginInterface loginInterface, MessagingInterface messagingInterface) {
        RegisterFragment fragment = new RegisterFragment();
        fragment.loginInterface = loginInterface;
        fragment.messagingInterface = messagingInterface;
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login_register, null);
        usernameEditText = root.findViewById(R.id.username_edittext);
        emailEditText = root.findViewById(R.id.email_edittext);
        passwordEditText = root.findViewById(R.id.password_edittext);
        registerButton = root.findViewById(R.id.register_button);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    registerButton.performClick();
                    return true;
                }
                return false;
            }
        });
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //final int DRAWABLE_LEFT = 0;
                //final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                //final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (passwordVisible) {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                        }
                        passwordVisible = !passwordVisible;
                        return true;
                    }
                    v.performClick();
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                root.requestFocus();

                email = emailEditText.getText().toString();
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                //Todo notify user on requirement / errors
                if (!checkInput(email, username, password))
                    return;

                if (NetworkHelper.requireNetworkActivation(getActivity()))
                    return;

                loginInterface.setLastUsed(email);

                ReCaptchaHelper reCaptcha = new ReCaptchaHelper.Builder()
                        .addOnSuccessListener(new ReCaptchaHelper.OnSuccessListener() {
                            @Override
                            public void onSuccess() {
                                loginInterface.registerWithEmailAndPassword(email, password);
                            }
                        })
                        .addOnFailureListener(new ReCaptchaHelper.OnFailureListener() {
                            @Override
                            public void onFailure() {
                                Log.d(TAG, "captcha has failed");
                            }
                        })
                        .build();

                reCaptcha.verify(getActivity());
            }
        });
    }

    boolean checkInput(String email, String username, String password) {
        //TODO implement email format check with regexp
        if (email.length() == 0)
            return false;
        else if (username.length() == 0)
            return false;
        else if (password.length() < PASSWORD_MIN_LENGTH)
            return false;
        return true;
    }

    //TODO move to keyboard utils
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
