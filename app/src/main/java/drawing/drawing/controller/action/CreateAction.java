package drawing.drawing.controller.action;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Model;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 07:44.
 */

public class CreateAction implements Action {
    private Figure figure;

    public CreateAction(Figure figure) {
        this.figure = figure;
    }

    public void undo(Model model) {
        model.removeFigure(figure);
    }

    public void redo(Model model) {
        model.addFigure(figure);
    }
}
