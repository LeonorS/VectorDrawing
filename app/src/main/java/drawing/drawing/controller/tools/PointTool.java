package drawing.drawing.controller.tools;

import android.util.Log;
import android.view.MotionEvent;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 01:57.
 */

public class PointTool extends Tool {
    private static final String TAG = "KJKP6_POINT_TOOL";

    public PointTool(ToolListener listener) {
        super(listener);
        Log.d(TAG, "Create tool");
        listener.unselect();
    }

    @Override
    public boolean onDown(MotionEvent event) {
        super.onDown(event);
        listener.createPoint(event.getX(), event.getY());
        return true;
    }
}
