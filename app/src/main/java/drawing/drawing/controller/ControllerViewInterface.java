package drawing.drawing.controller;


import java.util.ArrayList;

import drawing.drawing.controller.tools.Tool;
import drawing.drawing.model.Figure;

/**
 * VectorDrawing for FretX
 * Created by pandor on 19/01/18 23:11.
 */

public interface ControllerViewInterface {
    ArrayList<Figure> getFigures();
    ArrayList<Figure> getLinkedFigures(ArrayList<Integer> linked);
    Tool getTool();
}
