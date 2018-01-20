package drawing.drawing.controller.tools;

import android.util.Log;
import android.view.MotionEvent;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 02:04.
 */

public class LineTool extends Tool {
    private static final String TAG = "KJKP6_LINE_TOOL";

    public LineTool(ToolListener listener) {
        super(listener);
        Log.d(TAG, "Create tool");
        listener.unselect();
    }

    @Override
    public boolean onMove(MotionEvent event) {
        super.onMove(event);
        listener.createLine(anchor, event.getX(), event.getY());
        return true;
    }

    @Override
    public boolean onUp(MotionEvent event) {
        listener.clean();
        return false;
    }
}
