package drawing.drawing.personalization;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import drawing.drawing.R;

/**
 * VectorDrawing for FretX
 * Created by pandor on 16/01/18 03:53.
 */

public class InfoFragment extends Fragment {
    private static final String TAG = "KJKP6_PERSO_INFO";
    private TextView text1;
    private TextView text2;
    private Button button;
    private String line1;
    private String line2;
    private String buttonText;
    private PersonalizationListener listener;

    public static InfoFragment newInstance(@NonNull PersonalizationListener listener, String line1, String line2, String buttonText) {
        InfoFragment fragment = new InfoFragment();
        fragment.line1 = line1;
        fragment.line2 = line2;
        fragment.buttonText = buttonText;
        fragment.listener = listener;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personalization_info, null);
        button = (Button) root.findViewById(R.id.button);
        text1 = (TextView) root.findViewById(R.id.line1);
        text2 = (TextView) root.findViewById(R.id.line2);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text1.setText(line1);
        text2.setText(line2);
        button.setText(buttonText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onComplete();
            }
        });
    }
}
