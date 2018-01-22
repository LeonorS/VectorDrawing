package drawing.drawing.login;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.AuthCredential;

/**
 * Created by leo on 12/01/18.
 */

public interface LoginInterface {
    void setCurrentFragment(Fragment fragment);
    void signinWithAuthCredential(AuthCredential credential);
    void signinWithEmailAndPassword(String email, String password);
    void registerWithEmailAndPassword(String email, String password);
    void setLastUsed(String email);
    String getLastUsed();
}
