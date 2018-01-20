package drawing.drawing.vectordrawing;

import android.graphics.Point;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Selector;

/**
 * Created by leo on 19/01/18.
 */

public interface ControllerViewInterface {
    void cleanFigure();
    void reset();
    void uncheckFigure();
    Point moveFigure(float x, float y, Figure figure, Point anchor);
    ArrayList<Figure> selectFigure(Selector selector);
    Figure findFigure(float x, float y);
    ArrayList<Figure> getFigures();
    void makeFigure(float x, float y, DrawingView.DrawingAction currentAction, Point anchor, ArrayList<Figure> selected);
    ArrayList<Figure> findFiguresById(ArrayList<Integer> ids);
}
