package drawing.drawing.controller.tools;

import android.util.Log;
import android.view.MotionEvent;

import drawing.drawing.model.Figure;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 02:04.
 */

public class SegmentTool extends Tool {
    private static final String TAG = "KJKP6_SEGMENT_TOOL";
    private Figure figure;

    public SegmentTool(ToolListener listener) {
        super(listener);
        Log.d(TAG, "Create tool");
        listener.unselect();
    }

    @Override
    public boolean onMove(MotionEvent event) {
        super.onMove(event);
        if (figure != null)
            listener.remove(figure);
        figure = listener.createSegment(anchor, event.getX(), event.getY());
        return true;
    }

    @Override
    public boolean onUp(MotionEvent event) {
        super.onUp(event);
        listener.finalSegment(figure);
        return false;
    }
}
