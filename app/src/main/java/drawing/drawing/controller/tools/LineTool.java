package drawing.drawing.controller.tools;

import android.util.Log;
import android.view.MotionEvent;

import drawing.drawing.model.Figure;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 02:04.
 */

public class LineTool extends Tool {
    private static final String TAG = "KJKP6_LINE_TOOL";
    private Figure figure;

    public LineTool(ToolListener listener) {
        super(listener);
        Log.d(TAG, "Create tool");
        listener.unselect();
    }

    @Override
    public boolean onMove(MotionEvent event) {
        super.onMove(event);
        if (figure != null)
            listener.remove(figure);
        figure = listener.createLine(anchor, event.getX(), event.getY());
        return true;
    }

    @Override
    public boolean onUp(MotionEvent event) {
        super.onUp(event);
        listener.finalLine(figure);
        return false;
    }
}
