package drawing.drawing.personalization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.icu.text.LocaleDisplayNames;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Line;
import drawing.drawing.model.Model;
import drawing.drawing.model.PointFigure;
import drawing.drawing.vectordrawing.Designer;

/**
 * Created by leo on 14/01/18.
 */

public class TestDrawing extends View {
    private static final String TAG = "KJKP6_TEST_DRAWING";

    public interface MyCustomObjectListener {
        void endingTest(int point_margin, int seg_margin);
    }

    public static final int POINT_TEST = 0;
    public static final int SEG_TEST = 1;
    public int CURRENT_TEST = POINT_TEST;
    private int point_margin;
    private int seg_margin;

    private Figure touched;
    private Point anchor;
    private Model model;
    private Designer designer;
    private MyCustomObjectListener listener;

    public TestDrawing(Context context, AttributeSet attrs) {
        super(context, attrs);
        designer = new Designer();
        model = new Model(0, 0, 0, 0);
        model.makePoint(200, 200);
    }

    public void setCustomObjectListener(MyCustomObjectListener listener) {
        this.listener = listener;
    }

    public boolean onTouchEvent(MotionEvent event) {

        Figure f = model.getFigures().get(0);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                makeAnchor(event.getX(), event.getY());
                touched = null;
                if (CURRENT_TEST == POINT_TEST) {
                    PointFigure p = (PointFigure) f;
                    point_margin = Math.max(Math.abs(anchor.x - p.getPoints().get(0).x), Math.abs(anchor.y - p.getPoints().get(0).y));
                    if (point_margin <= 50) {
                        p.setMargin(point_margin);
                        touched = model.findFigure(anchor.x, anchor.y);
                    }
                } else if (CURRENT_TEST == SEG_TEST) {
                    Line s = (Line) f;
                    seg_margin = Math.abs(anchor.y - s.getPoints().get(0).y);
                    if (seg_margin <= 25) {
                        s.setMargin(seg_margin);
                        touched = model.findFigure(anchor.x, anchor.y);
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                anchor = model.moveFigure(event.getX(), event.getY(), touched, anchor);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (CURRENT_TEST == POINT_TEST && touched != null){
                    CURRENT_TEST = SEG_TEST;
                    model.reset();
                    model.makeSegment(200, 200,  new Point(600, 400));
                    Log.d(TAG, "test point ok" + model.getFigures().size());
                    invalidate();
                } else if (CURRENT_TEST == SEG_TEST && touched != null){
                    if (listener != null){
                        Log.d("TestDrawing", "point_margin : " + point_margin + "; seg_margin ; " + seg_margin );
                        listener.endingTest(point_margin, seg_margin);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Figure f : model.getFigures()) {
            if (f instanceof Line){
                designer.onDrawSegment(canvas, (Line) f);
            }
            else if (f instanceof PointFigure){
                designer.onDrawPointFigure(canvas, (PointFigure) f);
            }
        }
    }

    public  void makeAnchor(float x, float y){
        anchor = new Point((int) x, (int) y);
    }
}
