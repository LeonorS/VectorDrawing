package drawing.drawing.controller;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.controller.tools.DefaultTool;
import drawing.drawing.controller.tools.Tool;
import drawing.drawing.controller.tools.ToolListener;
import drawing.drawing.model.Figure;
import drawing.drawing.model.Model;
import drawing.drawing.model.Selector;
import drawing.drawing.vectordrawing.DrawingView;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 01:18.
 */

public class Controller implements ControllerViewInterface, ToolListener {

    private Model model;
    private DrawingView drawingView;
    private ControllerActivityInterface controllerActivityInterface;
    private Tool tool;

    public Controller(Model model, DrawingView drawingView, ControllerActivityInterface controllerActivityInterface) {
        this.model = model;
        this.controllerActivityInterface = controllerActivityInterface;
        this.drawingView = drawingView;

        tool = new DefaultTool(this);
        drawingView.setController(this);
    }

    public void reset() {
        tool = new DefaultTool(this);
        model.reset();
        drawingView.invalidate();
    }

    /**********************************************************************************************/
    public Tool getTool() {
        return tool;
    }

    public ArrayList<Figure> getFigures() {
        if (model != null)
            return model.getFigures();
        else
            return new ArrayList<>();
    }

    public ArrayList<Figure> getLinkedFigures(ArrayList<Integer> linkedId) {
        return model.findFiguesById(linkedId);
    }

    /**********************************************************************************************/
    public void setTool(Tool tool) {
        this.tool = tool;
        drawingView.invalidate();
    }

    public Figure getTouched(float x, float y) {
        return model.findFigure(x, y);
    }

    public void createPoint(float x, float y) {
        model.makePoint(x, y);
    }
    public void createSegment(Point anchor, float x, float y) {
        model.makeSegment(x, y, anchor);
    }
    public void createLine(Point anchor, float x, float y) {
        model.makeLine(x, y, anchor);
    }
    public void createIso() {
        model.makeIso(model.getSelected());
    }
    public Point move(float x, float y, Figure figure, Point anchor) {
        return model.moveFigure(x, y, figure, anchor);
    }
    public void select(Selector selector) {

        model.selectFigure(selector);
    }
    public void unselect() {
        model.uncheckFigure();
    }
    public void clean() {model.cleanCurrentFigure();}

    /**********************************************************************************************/
    public boolean canUndo() {
        return model.sizeFigures() > 0;
    }

    public boolean canRedo() {
        return model.sizeCanceled() > 0;
    }

    public void undo(){
        if(model.sizeFigures() == 0){
            return;
        }
        model.removeLastFigure();
        drawingView.invalidate();
    }

    public void redo(){
        if (model.sizeCanceled() == 0){
            return;
        }
        model.addLastCanceled();
        drawingView.invalidate();
    }

    public Model getModel() {
        return model;
    }

    public DrawingView getView() {
        return drawingView;
    }

    public void updatePrecision() {
        final User user = Database.getInstance().getUser();
        model.setPrecision(user.point_margin, user.segment_margin);
    }
}
