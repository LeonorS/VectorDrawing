package drawing.drawing.controller;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

import drawing.drawing.database.Database;
import drawing.drawing.database.User;
import drawing.drawing.model.Figure;
import drawing.drawing.model.Model;
import drawing.drawing.model.Selector;
import drawing.drawing.vectordrawing.ControllerActivityInterface;
import drawing.drawing.vectordrawing.ControllerViewInterface;
import drawing.drawing.vectordrawing.DrawingView;

import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.DEFAULT_ACTION;

/**
 * Created by leo on 20/01/18.
 */

public class Controller implements ControllerViewInterface {
    private final static String TAG = "KJKP6_CONTROLER";
    private Model model;
    private DrawingView drawingView;
    private ControllerActivityInterface controllerActivityInterface;

    public Controller(Model model, DrawingView drawingView, ControllerActivityInterface controllerActivityInterface) {
        this.model = model;
        this.controllerActivityInterface = controllerActivityInterface;
        this.drawingView = drawingView;
        drawingView.setController(this);
    }

    //==============================================================================================
    public void cleanFigure() {
        model.cleanCurrentFigure();
    }

    public void reset() {
        model.reset();
        drawingView.reset();
    }

    public void uncheckFigure() {
        model.uncheckFigure();
    }

    public ArrayList<Figure> selectFigure(Selector selector) {
        return model.selectFigure(selector);
    }

    public Figure findFigure(float x, float y) {
        return model.findFigure(x, y);
    }

    public ArrayList<Figure> getFigures() {
        if (model != null)
            return model.getFigures();
        else
            return new ArrayList<>();
    }

    public Point moveFigure(float x, float y, Figure figure, Point anchor) {
        return model.moveFigure(x, y, figure, anchor);
    }

    public void makeFigure(float x, float y, DrawingView.DrawingAction current_action, Point anchor, ArrayList<Figure> selected) {
        switch (current_action) {
            case POINT_ACTION:
                model.makePoint(x, y);
                break;
            case SEG_ACTION:
                model.makeLine(current_action, x, y, anchor);
                break;
            case LINE_ACTION:
                model.makeLine(current_action, x, y, anchor);
                break;
            case ISO_ACTION:
                model.makeIso(selected);
                drawingView.current_action = DEFAULT_ACTION;
                break;
        }
        controllerActivityInterface.invalidateOptionMenu();
    }

    public ArrayList<Figure> findFiguresById(ArrayList<Integer> ids) {
        return model.findFiguesById(ids);
    }

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
