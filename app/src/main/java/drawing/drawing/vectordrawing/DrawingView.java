package drawing.drawing.vectordrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import drawing.drawing.controller.ControllerViewInterface;
import drawing.drawing.controller.tools.Tool;
import drawing.drawing.model.Figure;
import drawing.drawing.model.Intersection;
import drawing.drawing.model.Iso;
import drawing.drawing.model.Line;
import drawing.drawing.model.PointFigure;
import drawing.drawing.model.Selector;
import drawing.drawing.model.StraightLine;


/**
 * Created by leo on 03/12/17.
 */

public class DrawingView extends View {
    private static final String TAG = "KJKP6_VIEW";
    private ControllerViewInterface controllerInterface;

    private Designer designer;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        designer = new Designer();
    }

    public void setController(ControllerViewInterface controller) {
        this.controllerInterface = controller;
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean requireRedraw = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                requireRedraw = controllerInterface.getTool().onDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                requireRedraw = controllerInterface.getTool().onMove(event);
                break;
            case MotionEvent.ACTION_UP:
                requireRedraw = controllerInterface.getTool().onUp(event);
                break;
        }
        if (requireRedraw)
            invalidate();
        return true;
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
                for(Integer i : ids){
                    Log.d("onDraw", ""+i);
                }
                final ArrayList<Figure> linkedFigures = controllerInterface.getLinkedFigures(iso.getIdsLinked());
                designer.onDrawIso(canvas, iso, linkedFigures);
            }
            else if (figure instanceof StraightLine || figure instanceof Line){
                designer.onDrawSegment(canvas, (Line) figure);
            }
            else if (figure instanceof Intersection){
                designer.onDrawInter(canvas, (Intersection) figure);
            }
            else if (figure instanceof PointFigure){
                designer.onDrawPointFigure(canvas, (PointFigure) figure);
            }
        }

        final Tool tool = controllerInterface.getTool();
        final Selector selector = tool.getSelector();
        if (selector != null){
            designer.onDrawSelector(canvas, selector);
        }
    }

    public Bitmap getPreview() {
        return Bitmap.createBitmap(getDrawingCache());
    }
}
