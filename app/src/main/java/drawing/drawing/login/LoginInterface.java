package drawing.drawing.login;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.AuthCredential;

/**
 * Parade for FretX
 * Created by pandor on 27/08/17 23:24.
 */

public interface LoginInterface {
    void setCurrentFragment(Fragment fragment);
    void signinWithAuthCredential(AuthCredential credential);
    void signinWithEmailAndPassword(String email, String password);
    void registerWithEmailAndPassword(String email, String password);
}
