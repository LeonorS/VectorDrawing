package drawing.drawing.controller.tools;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 02:02.
 */

public class DefaultTool extends Tool {
    private static final String TAG = "KJKP6_DEFAULT_TOOL";
    private Point initialPos;

    public DefaultTool(ToolListener listener) {
        super(listener);
        Log.d(TAG, "Create tool");
        listener.unselect();
    }

    @Override
    public boolean onDown(MotionEvent event) {
        super.onDown(event);
        initialPos = new Point((int)event.getX(), (int)event.getY());
        Log.d(TAG, "selected = " + (touched != null));
        return true;
    }

    @Override
    public boolean onMove(MotionEvent event) {
        super.onMove(event);

        if (touched != null) {
            Log.d(TAG, "move");
            anchor = listener.move(event.getX(), event.getY(), touched, anchor);
        }
        return true;
    }

    @Override
    public boolean onUp(MotionEvent event) {
        super.onUp(event);
        listener.finalMove(event.getX(), event.getY(), touched, initialPos);
        listener.unselect();
        return true;
    }
}