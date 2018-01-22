package drawing.drawing.profile;

/**
 * Created by leo on 26/08/17.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import drawing.drawing.BaseActivity;
import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.login.Login;
import drawing.drawing.personalization.Personalization;

import static drawing.drawing.personalization.Personalization.OUTSIDE_WORKFLOW;

public class Profile extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        final ImageView mProfileImage = findViewById(R.id.ivProfile);
        final TextView mUsernameTextView = findViewById(R.id.username);
        final TextView mEmailTextView = findViewById(R.id.email);
        final Button mPrecisionButton = findViewById(R.id.precision);
        final Button mLougoutButton = findViewById(R.id.logout);
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
                        Intent myIntent = new Intent(Profile.this, Login.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myIntent);
                    }
                });
            }
        });
    }
}
