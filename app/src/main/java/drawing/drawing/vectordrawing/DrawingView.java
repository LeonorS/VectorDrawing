package drawing.drawing.vectordrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Iso;
import drawing.drawing.model.Line;
import drawing.drawing.model.PointFigure;
import drawing.drawing.model.Selector;
import drawing.drawing.model.StraightLine;

import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.DEFAULT_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.LINE_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.POINT_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.SEG_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.SELECT_ACTION;

/**
 * Created by leo on 03/12/17.
 */

public class DrawingView extends View {
    private ControllerViewInterface controllerInterface;
    public enum DrawingAction {DEFAULT_ACTION, POINT_ACTION, SELECT_ACTION, SEG_ACTION, LINE_ACTION, ISO_ACTION}
    public DrawingAction current_action = DEFAULT_ACTION;
    private Figure touched = null;
    private Selector selector = null;
    private Point  anchor;
    private ArrayList<Figure> selected;
    private Designer designer;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        selected = new ArrayList<>();
        designer = new Designer();
    }

    public void setController(ControllerViewInterface controller) {
        this.controllerInterface = controller;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touched = null;
                makeAnchor(event.getX(), event.getY());
                if (current_action == DEFAULT_ACTION) {
                    touched = controllerInterface.findFigure(event.getX(), event.getY());
                }
                if (current_action == POINT_ACTION) {
                    controllerInterface.makeFigure(event.getX(), event.getY(), current_action, anchor, selected);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (current_action == DEFAULT_ACTION) {
                    anchor = controllerInterface.moveFigure(event.getX(), event.getY(), touched, anchor);
                    invalidate();
                }
                else if (current_action == SELECT_ACTION){
                    makeSelector(event.getX(), event.getY());
                    invalidate();
                }
                else if (current_action == SEG_ACTION || current_action == LINE_ACTION){
                    controllerInterface.makeFigure(event.getX(), event.getY(), current_action, anchor, selected);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (current_action == DEFAULT_ACTION) {
                    resetSelection();
                }
                else if (current_action == SELECT_ACTION) {
                    cleanSelector();
                }
                else if (current_action == SEG_ACTION  || current_action == LINE_ACTION){
                    controllerInterface.cleanFigure();
                }
                break;
        }
        return (true);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if (controllerInterface == null)
            return;
        for (Figure figure : controllerInterface.getFigures()) {
            if (figure instanceof Iso){
                Iso iso = (Iso) figure;
                ArrayList<Integer> ids = iso.getIdsLinked();
                final ArrayList<Figure> linkedFigures = controllerInterface.findFiguresById(iso.getIdsLinked());
                designer.onDrawIso(canvas, iso, linkedFigures);
            }
            else if (figure instanceof StraightLine){
                designer.onDrawLine(canvas, (Line) figure);
            }
            else if (figure instanceof Line){
                designer.onDrawSegment(canvas, (Line) figure);
            }
            else if (figure instanceof PointFigure){
                designer.onDrawPointFigure(canvas, (PointFigure) figure);
            }
        }
        if (selector != null){
            designer.onDrawSelector(canvas, selector);
        }
    }

    private void makeSelector(float x, float y){
        resetSelection();
        selector = new Selector(anchor, (int) x, (int) y);
        selected = controllerInterface.selectFigure(selector);
    }

    private  void makeAnchor(float x, float y){
        anchor = new Point((int) x, (int) y);
    }

    private void cleanSelector(){
        current_action = DEFAULT_ACTION;
        selector = null;
        invalidate();
    }

    protected void resetSelection(){
        controllerInterface.uncheckFigure();
        selected = new ArrayList<>();
        invalidate();
    }

    protected void makeIso(){
        Log.d("CustomView", "makeIso entry");
        controllerInterface.makeFigure(0, 0, current_action, anchor, selected);
        resetSelection();
        invalidate();
        Log.d("CustomView", "makeIso exit");
    }

    protected void makeInter(){
        controllerInterface.makeFigure(0, 0, current_action, anchor, selected);
        resetSelection();
        invalidate();
    }

    public void reset(){
        current_action = DEFAULT_ACTION;
        resetSelection();
        invalidate();
    }

    public Bitmap getPreview() {
        return Bitmap.createBitmap(getDrawingCache());
    }
}
