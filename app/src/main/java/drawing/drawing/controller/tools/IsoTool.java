package drawing.drawing.controller.tools;

import android.util.Log;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 02:05.
 */

public class IsoTool extends Tool {
    private static final String TAG = "KJKP6_ISO_TOOL";

    public IsoTool(ToolListener listener) {
        super(listener);
        Log.d(TAG, "Create tool");
        listener.createIso();
        listener.unselect();
    }
}
