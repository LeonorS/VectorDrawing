package drawing.drawing.controller.tools;

import android.graphics.Point;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Selector;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 02:24.
 */

public interface ToolListener {
    Figure getTouched(float x, float y);

    void createPoint(float x, float y);
    void createSegment(Point anchor, float x, float y);
    void createLine(Point anchor, float x, float y);
    void createIso();

    Point move(float x, float y, Figure figure, Point anchor);
    void select(Selector selector);
    void unselect();
    void clean();
}
