package drawing.drawing.controller.action;

import drawing.drawing.model.Figure;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 07:44.
 */

public class CreateAction extends Action {
    private Figure figure;

    public CreateAction(Figure figure) {
        this.figure = figure;
    }
}
