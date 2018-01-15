package drawing.drawing.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import org.json.JSONException;
import org.json.JSONObject;

import drawing.drawing.R;

/**
 * VectorDrawing for FretX
 * Created by pandor on 15/01/18 22:36.
 */

public class ReCaptcha {
    private static final String TAG = "KJKP6_RECAPTCHA";
    private OnSuccessListener successListener;
    private OnFailureListener failureListener;

    private ReCaptcha(){};

    public interface OnSuccessListener {
        void onSuccess();
    }

    public interface OnFailureListener {
        void onFailure();
    }

    public static class Builder {
        private ReCaptcha reCaptcha;

        public Builder() {
            reCaptcha = new ReCaptcha();
        }

        public Builder addOnSuccessListener(OnSuccessListener listener) {
            reCaptcha.successListener = listener;
            return this;
        }

        public Builder addOnFailureListener(OnFailureListener listener) {
            reCaptcha.failureListener = listener;
            return this;
        }

        public ReCaptcha build() {
            return reCaptcha;
        }
    }

    public void verify(final Activity acitvity) {
        SafetyNet.getClient(acitvity)
                .verifyWithRecaptcha(acitvity.getString(R.string.reCaptcha_SiteKey))
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        if (!recaptchaTokenResponse.getTokenResult().isEmpty()) {
                            handleSiteVerify(acitvity, recaptchaTokenResponse.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.e(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            Log.e(TAG, "Unknown type of error: " + e.getMessage());
                        }
                        if (failureListener != null)
                            failureListener.onFailure();
                    }
                });
    }

    private void handleSiteVerify(Activity activity, String tokenResult) {
        AndroidNetworking.post("https://www.google.com/recaptcha/api/siteverify")
                .addBodyParameter("secret", activity.getString(R.string.reCaptcha_SecretKey))
                .addBodyParameter("response", tokenResult)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "captcha response: " + response.toString());
                        try {
                            if (response.getBoolean("success") && successListener != null)
                                successListener.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "ANerror: " + error.getMessage());
                    }
                });
    }
}
