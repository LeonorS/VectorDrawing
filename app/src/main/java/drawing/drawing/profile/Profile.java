package drawing.drawing.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.login.Login;
import drawing.drawing.messaging.CustomProgressDialog;
import drawing.drawing.messaging.MessagingInterface;
import drawing.drawing.personalization.Personalization;
import drawing.drawing.utils.NetworkHelper;

import static drawing.drawing.personalization.Personalization.OUTSIDE_WORKFLOW;

public class Profile extends AppCompatActivity {
    private static final String TAG = "KJKP6_PROFILE";
    private MessagingInterface messagingInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_test);

        messagingInterface = CustomProgressDialog.newInstance(getFragmentManager());
        setTitle("Profile");

        final ImageView mProfileImage = findViewById(R.id.photo);
        final TextView mUsernameTextView = findViewById(R.id.username);
        final TextView mEmailTextView = findViewById(R.id.email);
        final TextView mPrecisionButton = findViewById(R.id.precision);
        final TextView mLougoutButton = findViewById(R.id.logout);
        final ImageView mDeleteButton = findViewById(R.id.delete);

        mUsernameTextView.setText(Database.getInstance().getUser().username);
        mEmailTextView.setText(Database.getInstance().getUser().email);

        Uri photoURI = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        Picasso.with(this).load(photoURI).into(mProfileImage, new Callback() {
            @Override
            public void onSuccess() {
                final Bitmap bitmap = ((BitmapDrawable)mProfileImage.getDrawable()).getBitmap();
                final RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                mDrawable.setCircular(true);
                mDrawable.setColorFilter(ContextCompat.getColor(Profile.this, R.color.colorPrimaryDark), PorterDuff.Mode.DST_OVER);
                mProfileImage.setImageDrawable(mDrawable);
            }

            @Override
            public void onError() {
                //toDo handle error
            }
        });

        mPrecisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Profile.this, Personalization.class);
                myIntent.putExtra(OUTSIDE_WORKFLOW, true);
                startActivity(myIntent);
            }
        });

        mLougoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.signout(Profile.this, new Login.OnSignoutCompleteListener() {
                    @Override
                    public void onComplete() {
                        backToLogin();
                    }
                });
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkHelper.requireNetworkActivation(Profile.this))
                    return;

                DialogFragment dialog = DeleteConfirmationDialog.newInstance(Profile.this, messagingInterface);
                dialog.show(getFragmentManager(), "delete");
            }
        });
    }

    public void backToLogin() {
        Intent myIntent = new Intent(Profile.this, Login.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(myIntent);
    }

    public static class DeleteConfirmationDialog extends DialogFragment {
        private MessagingInterface messagingInterface;
        private Profile activity;

        public static DeleteConfirmationDialog newInstance(Activity activity, MessagingInterface messagingInterface) {
            DeleteConfirmationDialog dialog = new DeleteConfirmationDialog();
            dialog.activity = (Profile) activity;
            dialog.messagingInterface = messagingInterface;
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("This action cannot be undo, are you sure?")
                    .setTitle("Delete Account?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user == null)
                                return;
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //todo Beurk!
                                    activity.backToLogin();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    messagingInterface.show(CustomProgressDialog.DialogType.WARNING, "Your last authentication is outdated!",
                                            "Please logout, login and retry.");
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }
}
