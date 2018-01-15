package drawing.drawing.testdrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Model;
import drawing.drawing.model.PointFigure;
import drawing.drawing.model.Segment;
import drawing.drawing.vectordrawing.CustomView;
import drawing.drawing.vectordrawing.Designer;

/**
 * Created by leo on 14/01/18.
 */

public class TestDrawing extends View {

    public interface MyCustomObjectListener {
        void endingTest(int point_margin, int seg_margin);
    }

    private MyCustomObjectListener listener;

    public static final int POINT_TEST = 0;
    public static final int SEG_TEST = 1;
    public int CURRENT_TEST;

    private Figure touched;
    private Point anchor;

    private int point_margin;
    private int seg_margin;

    private Model model;
    private Designer designer;

    public TestDrawing(Context context, AttributeSet attrs) {
        super(context, attrs);
        model = new Model(0, 0, 0, 0);
        model.makeFigure(CustomView.POINT_ACTION, 200, 200, null, null);
        CURRENT_TEST = POINT_TEST;
        designer = new Designer();
    }

    public void setCustomObjectListener(MyCustomObjectListener listener) {
        this.listener = listener;
    }

    public boolean onTouchEvent(MotionEvent event) {

        Figure f = model.getFigures().get(0);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                makeAchor(event.getX(), event.getY());
                touched = null;

                if (CURRENT_TEST == POINT_TEST) {

                    PointFigure p = (PointFigure) f;

                    point_margin = Math.max(Math.abs(anchor.x - p.getPoints().get(0).x), Math.abs(anchor.y - p.getPoints().get(0).y));
                    if (point_margin <= 50) {
                        p.setMargin(point_margin);
                        touched = model.findFigure(anchor.x, anchor.y);
                    }
                }

                else if (CURRENT_TEST == SEG_TEST) {

                        Segment s = (Segment) f;

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
                    model.makeFigure(CustomView.SEG_ACTION, 100, 300, null, new Point(600, 300));
                    invalidate();
                }

                else if (CURRENT_TEST == SEG_TEST && touched != null){
                    if (listener != null){
                        listener.endingTest(point_margin, seg_margin);
                    }
                }
                break;
        }
        return (true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ArrayList<Figure> figures = model.getFigures();
        designer.onDraw(canvas, figures, null);
    }

    public  void makeAchor(float x, float y){
        anchor = new Point((int) x, (int) y);
    }
}
