package drawing.drawing.personalization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import drawing.drawing.R;
import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.database.UserListener;

/**
 * VectorDrawing for FretX
 * Created by pandor on 16/01/18 04:23.
 */

public class DrawingFragment extends Fragment {
    private static final String TAG = "KJKP6_PERSO_DRAWING";
    private TestDrawing drawingView;
    private PersonalizationListener listener;

    public static DrawingFragment newInstance(PersonalizationListener listener) {
        DrawingFragment fragment = new DrawingFragment();
        fragment.listener = listener;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.personalization_drawing_fragment, null);
        drawingView = (TestDrawing) root.findViewById(R.id.drawing_view);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawingView.setCustomObjectListener(new TestDrawing.MyCustomObjectListener() {
            @Override
            public void endingTest(int point_margin, int seg_margin) {

                //Todo race condition if going to next activity too fast
                final Database database = Database.getInstance();
                final User user = database.getUser();
                Log.d("DrawingFragment", "point_margin : " + point_margin + "; seg_margin ; " + seg_margin );
                user.setPrecision(point_margin, seg_margin);
                Database.getInstance().addUserListenerWithoutNotifying(userDataUpdateListener);
                Log.d(TAG, "set user");
                database.setUser(user);

//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Personalization_old.this);
//                SharedPreferences.Editor edit = preferences.edit();
//                edit.putInt("point_margin", point_margin);
//                edit.apply();
//                edit.putInt("seg_margin", seg_margin);
//                edit.apply();
            }
        });
    }

    private final UserListener userDataUpdateListener = new UserListener() {
        @Override
        public void onUpdate(User user) {
            Log.d(TAG, "user updated");
            listener.onComplete();
        }
    };
}
