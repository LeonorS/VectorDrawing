package drawing.drawing.storage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Model;
import drawing.drawing.utils.JsonHelper;
import drawing.drawing.utils.NetworkHelper;

/**
 * Created by leo on 18/01/18.
 */

public class Storage {
    private static final String TAG = "KJKP6_STORAGE";
    private static final long ONE_MEGABYTE = 1024 * 1024;
    private StorageReference storageRef;

    //ToDo make this listener generic for all storage actions
    public interface OnStorageCompleteListener {
        void onSuccess(Object obj);
        void onFailure(String error);
    }

    private Storage() {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    private static class Holder {
        private static final Storage instance = new Storage();
    }

    public static Storage getInstance() {
        return Holder.instance;
    }

    public void setModel(Activity activity, String name, final Model model, @Nullable final OnStorageCompleteListener listener) {
        if (NetworkHelper.requireNetworkActivation(activity)) {
            if (listener != null)
                listener.onFailure("network failure");
        }
        final JsonHelper<Model> jsonHelper = new JsonHelper<>(Model.class);
        jsonHelper.registerTypeAdapter(Figure.class);
        final String save = jsonHelper.saveToJson(model);
        final StorageReference drawRef = storageRef.child("draws");
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null)
            return;
        final StorageReference userDrawRef = drawRef.child(fUser.getUid());
        final StorageReference fileRef = userDrawRef.child(name);
        final UploadTask uploadTask = fileRef.putBytes(save.getBytes());
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w(TAG, "UploadFailed: " + exception.getMessage());
                if (listener != null)
                    listener.onFailure("upload failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.w(TAG, "UploadSucceeded: " + downloadUrl.toString());
                if (listener != null)
                    listener.onSuccess(model);
            }
        });
    }

    public void getModel(String name, @NonNull final OnStorageCompleteListener listener) {
        final StorageReference drawRef = storageRef.child("draws");
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null)
            return;
        final StorageReference userDrawRef = drawRef.child(fUser.getUid());
        final StorageReference fileRef = userDrawRef.child(name);
        fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d(TAG, "received JSON: " + new String(bytes));
                final String jsonString = new String(bytes);
                final JsonHelper<Model> jsonHelper = new JsonHelper<>(Model.class);
                jsonHelper.registerTypeAdapter(Figure.class);
                final Model model = jsonHelper.loadToObject(jsonString);
                listener.onSuccess(model);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "getModel failed with error: " + exception.getMessage());
                listener.onFailure(exception.getMessage());
            }
        });
    }

    public void setPreview(Activity activity, String name, final Bitmap preview, @Nullable final OnStorageCompleteListener listener) {
        if (NetworkHelper.requireNetworkActivation(activity)) {
            if (listener != null)
                listener.onFailure("network failure");
        }
        final StorageReference drawRef = storageRef.child("preview");
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null)
            return;
        final StorageReference userDrawRef = drawRef.child(fUser.getUid());
        final StorageReference fileRef = userDrawRef.child(name);
        FileOutputStream out = null;
        try {
            File f = activity.getFilesDir();
            String s = f.getCanonicalPath();
            out = new FileOutputStream(s + "/temp.png");
            preview.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            preview.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            InputStream stream = new FileInputStream(new File(activity.getFilesDir(), "temp.png"));
            UploadTask uploadTask = fileRef.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.w(TAG, "UploadFailed: " + exception.getMessage());
                    if (listener != null)
                        listener.onFailure("upload failed");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.w(TAG, "UploadSucceeded: " + downloadUrl.toString());
                    if (listener != null)
                        listener.onSuccess(null);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPreviewDownloadUrl(String name, @NonNull final OnStorageCompleteListener listener) {
        final StorageReference drawRef = storageRef.child("preview");
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null)
            return;
        final StorageReference userDrawRef = drawRef.child(fUser.getUid());
        final StorageReference fileRef = userDrawRef.child(name);

        fileRef.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e.getMessage());
            }
        }). addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                listener.onSuccess(uri);
            }
        });
    }

}
