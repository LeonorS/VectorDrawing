package drawing.drawing.controller.action;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 07:43.
 */

public abstract class Action {
    public abstract void undo();
    public abstract void redo();
}
