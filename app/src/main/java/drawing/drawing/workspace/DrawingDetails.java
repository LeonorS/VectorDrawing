package drawing.drawing.workspace;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import drawing.drawing.R;
import drawing.drawing.model.Model;
import drawing.drawing.storage.Storage;

import static drawing.drawing.vectordrawing.VectorDrawing.DRAWING_NAME;

public class DrawingDetails extends AppCompatActivity {
    private static final String TAG = "KJKP6_DRAWING_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_details);

        final ImageView imageView = findViewById(R.id.imageView);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(DRAWING_NAME)) {
            String name = bundle.getString(DRAWING_NAME);
            Log.d(TAG, "Preview file: " + name);
            Storage.getInstance().getPreview(name, new Storage.OnStorageCompleteListener() {
                @Override
                public void onSuccess(Object obj) {
                    Bitmap preview = (Bitmap) obj;
                    imageView.setImageBitmap(preview);
                }

                @Override
                public void onFailure(String error) {

                }
            });
        }
    }
}
