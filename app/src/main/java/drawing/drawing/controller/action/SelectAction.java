package drawing.drawing.controller.action;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Model;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 09:30.
 */

public class SelectAction implements Action{
    private ArrayList<Figure> selection;

    public SelectAction(ArrayList<Figure> selection) {
        this.selection = selection;
    }

    public void undo(Model model) {
        model.select(selection);
    }

    public void redo(Model model) {
        model.unselect(selection);
    }
}
