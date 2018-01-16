package drawing.drawing.login;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.TwitterApi;

import java.util.List;

import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.database.UserListener;
import drawing.drawing.personalization.Personalization;
import drawing.drawing.utils.CrashAnalyticsHelper;
import drawing.drawing.vectordrawing.VectorDrawing;

public class Login extends AppCompatActivity implements LoginInterface {
    private final static String TAG = "KJKP6_LOGIN";
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
        }

        CrashAnalyticsHelper.signOut();

        List<? extends UserInfo> providerData = FirebaseAuth.getInstance().getCurrentUser().getProviderData();
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
                case "twitter.com":
                    //todo check that nothing need to be done
                    --remainingProvider;
                    signoutCheck(listener);
                    break;
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

    // ==================================USER DATA CHECK============================================
    private void onSuccessfulLogin() {
        CrashAnalyticsHelper.signIn(FirebaseAuth.getInstance().getCurrentUser());
        Database.getInstance().addUserListenerWithoutNotifying(userDataCheckListener);
        Database.getInstance().addUserEventListener();
    }

    private UserListener userDataCheckListener = new UserListener() {
        @Override
        public void onUpdate(User user) {
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            if (fUser == null) {
                Log.e(TAG, "firebase user null after Auth");
                return;
            }
            Database.getInstance().removeUserListener(this);
            if (user == null) {
                Log.w(TAG, "user " + FirebaseAuth.getInstance().getCurrentUser().getUid() + " is new");
                Database.getInstance().addUserListenerWithoutNotifying(userDataCreateListener);
                Database.getInstance().setUser(new User(fUser.getDisplayName(), fUser.getEmail()));
            } else {
                Log.w(TAG, "user is old");
                Intent i = new Intent(Login.this, VectorDrawing.class);
                startActivity(i);
                Login.this.finish();
            }
        }
    };

    private UserListener userDataCreateListener = new UserListener() {
        @Override
        public void onUpdate(User user) {
            Database.getInstance().removeUserListener(this);
            if (user == null) {
                Log.w(TAG, "user creation failed");
            } else {
                Log.w(TAG, "user " + FirebaseAuth.getInstance().getCurrentUser().getUid() + " is created");
                onSuccessfulUserData();
            }
        }
    };


    private void onSuccessfulUserData() {
        Intent i = new Intent(Login.this, Personalization.class);
        startActivity(i);
        Login.this.finish();
    }

    // =============================LOGIN INTERFACE IMPLEMENTATION==================================
    public void setCurrentFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void signinWithAuthCredential(AuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            onSuccessfulLogin();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signinWithEmainAndPassword(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signinUserWithEmail:success");
                            onSuccessfulLogin();
                        } else {
                            Log.w(TAG, "signinUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(Login.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "signinUserWithEmail:failure", e);
                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void registerWithEmailAndPassword(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            sendConfirmationEmail();
                            onSuccessfulLogin();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(Login.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "createUserWithEmail:failure", e);
                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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
        //String url = "http://www.example.com/verify?uid=" + fUser.getUid();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                //.setUrl(url)
                .setAndroidPackageName("drawing.drawing", false, null)
                //.setIOSBundleId("com.example.ios")
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
}
