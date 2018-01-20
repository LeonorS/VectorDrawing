package drawing.drawing.controller.action;

import drawing.drawing.model.Model;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 07:43.
 */

public interface Action {
    void undo(Model model);
    void redo(Model model);
}
