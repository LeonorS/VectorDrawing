package drawing.drawing.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import drawing.drawing.utils.NetworkHelper;
import drawing.drawing.R;


/**
 * Parade for FretX
 * Created by pandor on 26/08/17 17:15.
 */

public class SigninFragment extends Fragment {
    private final static String TAG = "KJKP6_SIGNIN";
    private final static int RC_SIGN_IN = 456;
    private LoginInterface loginInterface;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signinButton;
    private LoginButton facebookLoginButton;
    private CallbackManager facebookCallbackManager;
    private TwitterLoginButton twitterLoginButton;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton googleLoginButton;
    private TextView register;
    private TextView forgot;
    private View root;

    public static SigninFragment newInstance(LoginInterface loginInterface) {
        SigninFragment fragment = new SigninFragment();
        fragment.loginInterface = loginInterface;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.login_signin_fragment, null);
        emailEditText = (EditText) root.findViewById(R.id.email_edittext);
        passwordEditText = (EditText) root.findViewById(R.id.password_edittext);
        signinButton = (Button) root.findViewById(R.id.signin_button);
        facebookLoginButton = (LoginButton) root.findViewById(R.id.connectWithFbButton);
        googleLoginButton = (SignInButton) root.findViewById(R.id.sign_in_button);
        twitterLoginButton = (TwitterLoginButton) root.findViewById(R.id.login_button);
        register = (TextView) root.findViewById(R.id.register_textview);
        forgot = (TextView) root.findViewById(R.id.forgot_textview);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FACEBOOK
        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.setFragment(this);
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:success");
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                loginInterface.signinWithAuthCredential(credential);
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:cancel");
                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "facebook:error" + exception.toString());
                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });

        //TWITTER
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitter:success");
                AuthCredential credential = TwitterAuthProvider.getCredential(
                        result.data.getAuthToken().token,
                        result.data.getAuthToken().secret);
                loginInterface.signinWithAuthCredential(credential);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d(TAG, "twitter:failure");
                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });

        //GOOGLE+
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "google:error" + connectionResult.getErrorMessage());
                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //EMAIL AND PASSWORD
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkHelper.requireNetworkActivation(getActivity()))
                    return;
                signinWithEmailAndPassword();
            }
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    signinWithEmailAndPassword();
                    return true;
                }
                return false;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment registerFragment = RegisterFragment.newInstance(loginInterface);
                fragmentTransaction.replace(R.id.container, registerFragment, "selection");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
                loginInterface.setCurrentFragment(registerFragment);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment signinFragment = ForgotPasswordFragment.newInstance(loginInterface);
                fragmentTransaction.replace(R.id.container, signinFragment, "selection");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
                loginInterface.setCurrentFragment(signinFragment);
            }
        });
    }


    private void signinWithEmailAndPassword() {
        root.requestFocus();
        hideKeyboard(getActivity());
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (!checkInput(email, password))
            return;
        loginInterface.signinWithEmainAndPassword(email, password);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            loginInterface.signinWithAuthCredential(credential);
        } else {
            Log.d(TAG, "google:error " + result.getStatus().toString());
            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "fragment result");
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
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

    boolean checkInput(String email, String password) {
        if (email.length() == 0)
            return false;
        else if (password.length() == 0)
            return false;
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            mGoogleApiClient.stopAutoManage(activity);
            mGoogleApiClient.disconnect();
        }
    }
}
