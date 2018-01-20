package drawing.drawing.controller;

import android.graphics.Point;
import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import drawing.drawing.controller.action.Action;
import drawing.drawing.controller.action.CreateAction;
import drawing.drawing.controller.action.MoveAction;
import drawing.drawing.controller.action.SelectAction;
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
    private Deque<Action> done;
    private Deque<Action> undone;
    private static final int MAX_ACTIONS = 10;

    public Controller(Model model, DrawingView drawingView, ControllerActivityInterface controllerActivityInterface) {
        this.model = model;
        this.controllerActivityInterface = controllerActivityInterface;
        this.drawingView = drawingView;

        drawingView.setController(this);
        reset();
    }

    public void reset() {
        tool = new DefaultTool(this);
        model.reset();
        done = new ArrayDeque<>();
        undone = new ArrayDeque<>();
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

    public Figure createPoint(float x, float y) {
        final Figure figure = model.makePoint(x, y);
        createAction(figure);
        return figure;
    }
    public Figure createSegment(Point anchor, float x, float y) {
        Figure figure = model.makeSegment(x, y, anchor);
        return figure;
    }
    public Figure createLine(Point anchor, float x, float y) {
        Figure figure = model.makeLine(x, y, anchor);
        return figure;
    }
    public Figure createIso() {
        final Figure figure = model.makeIso(model.getSelected());
        createAction(figure);
        return figure;
    }
    public void finalLine(Figure figure) {
        createAction(figure);
    }
    public void finalSegment(Figure figure) {
        createAction(figure);
    }
    public void remove(Figure figure) {
        model.removeFigure(figure);
    }

    public Point move(float x, float y, Figure figure, Point anchor) {
        return model.moveFigure(x, y, figure, anchor);
    }
    public void finalMove(float x, float y, Figure figure, Point anchor) {
        moveAction(x, y, figure, anchor);
    }
    public void select(Selector selector) {
        model.selectFigure(selector);
    }
    public void unselect() {model.uncheckFigure();}

    /**********************************************************************************************/
    public boolean canUndo() {
        return done.size() > 0;
    }

    public boolean canRedo() {
        return undone.size() > 0;
    }

    public void undo(){
        if(done.size() == 0) {
            return;
        }
        final Action action = done.getLast();
        action.undo(model);
        done.removeLast();
        if (undone.size() >= MAX_ACTIONS)
            undone.removeFirst();
        undone.addLast(action);
        drawingView.invalidate();
    }

    public void redo(){
        if (undone.size() == 0) {
            return;
        }
        final Action action = undone.getLast();
        action.redo(model);
        undone.removeLast();
        if (done.size() >= MAX_ACTIONS)
            done.removeFirst();
        done.addLast(action);
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

    /**********************************************************************************************/
    private void createAction(Figure figure) {
        final Action action = new CreateAction(figure);
        if (done.size() >= MAX_ACTIONS)
            done.removeFirst();
        done.addLast(action);
    }

    private void moveAction(float x, float y, Figure figure, Point anchor) {
        final Action action = new MoveAction(x, y, figure, anchor);
        if (done.size() >= MAX_ACTIONS)
            done.removeFirst();
        done.addLast(action);
    }
}
