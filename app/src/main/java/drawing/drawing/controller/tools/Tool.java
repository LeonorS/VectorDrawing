package drawing.drawing.controller.tools;

import android.graphics.Point;
import android.view.MotionEvent;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Selector;

/**
 * VectorDrawing for FretX
 * Created by pandor on 20/01/18 01:57.
 */

public class Tool {
    protected ToolListener listener;
    protected Figure touched;
    protected Point anchor;
    protected Selector selector;

    public Tool(ToolListener listener) {
        this.listener = listener;
    }

//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                touched = model.findFigure(event.getX(), event.getY());
//
//                onDown(event);

//                touched = null;
//                makeAnchor(event.getX(), event.getY());
//
//                if (current_action == DEFAULT_ACTION) {
//                    touched = controllerInterface.findFigure(event.getX(), event.getY());
//                }
//                if (current_action == POINT_ACTION) {
//                    controllerInterface.makeFigure(event.getX(), event.getY(), current_action, anchor, selected);
//                    invalidate();
//                }
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                onMove(event);
//
//                if (current_action == DEFAULT_ACTION) {
//                    anchor = controllerInterface.moveFigure(event.getX(), event.getY(), touched, anchor);
//                    invalidate();
//                }
//                else if (current_action == SELECT_ACTION){
//                    makeSelector(event.getX(), event.getY());
//                    invalidate();
//                }
//                else if (current_action == SEG_ACTION || current_action == LINE_ACTION){
//                    controllerInterface.makeFigure(event.getX(), event.getY(), current_action, anchor, selected);
//                    invalidate();
//                }
//                break;
//
//            case MotionEvent.ACTION_UP:
//                onUp(event);
//
//                if (current_action == DEFAULT_ACTION) {
//                    resetSelection();
//                }
//                else if (current_action == SELECT_ACTION) {
//                    cleanSelector();
//                }
//                else if (current_action == SEG_ACTION  || current_action == LINE_ACTION){
//                    controllerInterface.cleanFigure();
//                }
//                break;
//        }
//        return true;
//    }

    public Selector getSelector() {
        return selector;
    }
    public boolean onDown(MotionEvent event) {
        touched = listener.getTouched(event.getX(), event.getY());
        anchor = new Point((int)event.getX(), (int)event.getY());
        return false;
    }
    public boolean onMove(MotionEvent event) {return false;}
    public boolean onUp(MotionEvent event) {return false;}
}
