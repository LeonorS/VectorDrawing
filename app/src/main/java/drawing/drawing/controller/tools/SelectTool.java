package drawing.drawing.controller.tools;

import android.util.Log;
import android.view.MotionEvent;

import drawing.drawing.model.Selector;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 02:03.
 */

public class SelectTool extends Tool {
    private static final String TAG = "KJKP6_SELECT_TOOL";

    public SelectTool(ToolListener listener) {
        super(listener);
        Log.d(TAG, "Create tool");
    }

    @Override
    public boolean onMove(MotionEvent event) {
        super.onMove(event);

        selector = new Selector(anchor, (int) event.getX (), (int) event.getY());
        listener.select(selector);
        return true;
    }

    @Override
    public boolean onUp(MotionEvent event) {
        selector = null;
        return true;
    }
}
