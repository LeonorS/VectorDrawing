package drawing.drawing.controller.action;

import android.graphics.Point;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Model;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 07:44.
 */

public class MoveAction implements Action {
    private float x;
    private float y;
    private Point anchor;
    private Figure figure;

    public MoveAction(float x, float y, Figure figure, Point anchor) {
        this.figure = figure;
        this.x = x;
        this.y = y;
        this.anchor = anchor;
    }

    public void undo(Model model) {
        model.moveFigure(anchor.x, anchor.y, figure, new Point((int)x, (int)y));
    }

    public void redo(Model model) {
        model.moveFigure(x, y, figure, anchor);
    }
}
