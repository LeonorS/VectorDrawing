package drawing.drawing.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.List;

import drawing.drawing.BaseActivity;
import drawing.drawing.R;
import drawing.drawing.messaging.CustomProgressDialog;
import drawing.drawing.messaging.MessagingHandler;
import drawing.drawing.workspace.Workspace;
import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.database.UserListener;
import drawing.drawing.personalization.Personalization;
import drawing.drawing.utils.CrashAnalyticsHelper;

/**
 * Created by leo on 12/01/18.
 */

public class Login extends BaseActivity implements LoginInterface {
    private final static String TAG = "KJKP6_LOGIN";
    private final static String LAST_USED_KEY = "last_used";
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private static int remainingProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = SigninFragment.newInstance(this);
        fragmentTransaction.add(R.id.container, fragment, "selection");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0)
            super.onBackPressed();
        else
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnSignoutCompleteListener {
        void onComplete();
    }

    public static void signout(final Activity activity, final OnSignoutCompleteListener listener) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null) {
            Log.w(TAG, "unable to signout user, fUser is null");
            return;
        }
        CrashAnalyticsHelper.signOut();
        List<? extends UserInfo> providerData = fUser.getProviderData();
        remainingProvider = providerData.size();
        for (UserInfo data: providerData) {
            switch (data.getProviderId()) {
                case "firebase":
                    FirebaseAuth.getInstance().signOut();
                    --remainingProvider;
                    signoutCheck(listener);
                    break;
                case "facebook.com":
                    LoginManager.getInstance().logOut();
                    --remainingProvider;
                    signoutCheck(listener);
                    break;
                case "google.com":
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();
                    final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful())
                                Log.w(TAG, "Google logout failed");
                            --remainingProvider;
                            signoutCheck(listener);
                        }
                    });
                    break;
                default:
                    --remainingProvider;
                    signoutCheck(listener);
            }
            Log.d(TAG, "signed oud of " + data.getProviderId());
        }
    }

    //todo wait until signout or timeout
    public static void signoutCheck(final OnSignoutCompleteListener listener) {
        Log.d(TAG,"remainingProvider: " + remainingProvider);
        if (remainingProvider == 0) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listener.onComplete();
                }
            }, 300);
        }
    }

    private void onSuccessfulLogin() {
        CrashAnalyticsHelper.signIn(FirebaseAuth.getInstance().getCurrentUser());
        Database.getInstance().addUserListenerWithoutNotifying(userDataCheckListener);
        Database.getInstance().addUserEventListener();
    }

    private UserListener userDataCheckListener = new UserListener() {
        @Override
        public void onUpdate(User user) {
            MessagingHandler.getInstance().show(CustomProgressDialog.DialogType.PROGRESS, "Retrieving account...");
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            if (fUser == null) {
                Log.e(TAG, "firebase user null after Auth");
                return;
            }
            Database.getInstance().removeUserListener(this);
            if (user == null) {
                MessagingHandler.getInstance().show(CustomProgressDialog.DialogType.PROGRESS, "Creating account...");
                Log.w(TAG, "user " + fUser.getUid() + " is new");
                Database.getInstance().addUserListenerWithoutNotifying(userDataCreateListener);
                Database.getInstance().setUser(new User(fUser.getDisplayName(), fUser.getEmail()));
            } else {
                MessagingHandler.getInstance().dismiss();
                Log.w(TAG, "user is old");
                Intent i = new Intent(Login.this, Workspace.class);
                startActivity(i);
                finish();
            }
        }
    };

    private UserListener userDataCreateListener = new UserListener() {
        @Override
        public void onUpdate(User user) {
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            if (fUser == null) {
                Log.e(TAG, "firebase user null after Auth");
                return;
            }
            Database.getInstance().removeUserListener(this);
            if (user == null) {
                MessagingHandler.getInstance().dismiss();
                Log.w(TAG, "user creation failed");
            } else {
                MessagingHandler.getInstance().dismiss();
                Log.w(TAG, "user " + fUser.getUid() + " is created");
                onSuccessfulUserData();
            }
        }
    };

    private void onSuccessfulUserData() {
        Intent i = new Intent(Login.this, Personalization.class);
        startActivity(i);
        finish();
    }

    public void setCurrentFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void signinWithAuthCredential(AuthCredential credential) {
        MessagingHandler.getInstance().show(CustomProgressDialog.DialogType.PROGRESS, "Signing in...");
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "signInWithCredential:success");
                        onSuccessfulLogin();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "signInWithCredential:failure", e);
                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        MessagingHandler.getInstance().show(CustomProgressDialog.DialogType.FAIL, "Sign in failed", e.getMessage());
                    }
                });
    }

    public void signinWithEmailAndPassword(String email, String password) {
        MessagingHandler.getInstance().show(CustomProgressDialog.DialogType.PROGRESS, "Signing in...");
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "signinUserWithEmail:success");
                        onSuccessfulLogin();
                    }
                })
                .addOnFailureListener(Login.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "signinUserWithEmail:failure", e);
                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        MessagingHandler.getInstance().show(CustomProgressDialog.DialogType.FAIL, "Sign in failed", e.getMessage());
                    }
                });
    }

    public void registerWithEmailAndPassword(String email, String password) {
        MessagingHandler.getInstance().show(CustomProgressDialog.DialogType.PROGRESS, "Registering...");
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "createUserWithEmail:success");
                            sendConfirmationEmail();
                            onSuccessfulLogin();
                    }
                })
                .addOnFailureListener(Login.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "createUserWithEmail:failure", e);
                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        MessagingHandler.getInstance().show(CustomProgressDialog.DialogType.FAIL, "Registration failed", e.getMessage());
                    }
                });
    }

    private void sendConfirmationEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fUser = auth.getCurrentUser();
        if (fUser == null) {
            Log.e(TAG, "firebase user is null after registration");
            return;
        }
        //Todo use url to redirect user to login page
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName("drawing.drawing", false, null)
                .build();
        fUser.sendEmailVerification(actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Email not sent.");
                    }
                });
    }

    public void setLastUsed(String last) {
        final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LAST_USED_KEY, last);
        editor.commit();
    }

    public String getLastUsed() {
        final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(LAST_USED_KEY, "");
    }
}
