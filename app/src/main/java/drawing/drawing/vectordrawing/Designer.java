package drawing.drawing.vectordrawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Iso;
import drawing.drawing.model.Line;
import drawing.drawing.model.PointFigure;
import drawing.drawing.model.Segment;
import drawing.drawing.model.Selector;

/**
 * Created by leo on 15/01/18.
 */

public class Designer {

    private Paint myPaint;

    public Designer(){
        myPaint = new Paint();
        myPaint.setAntiAlias(true);
    }

    public void onDraw(Canvas canvas, ArrayList<Figure> figures, Selector selector){

        for (Figure f : figures) {

            if (f instanceof Iso){
                onDrawIso(canvas, (Iso)f);
            }
            else if (f instanceof Line || f instanceof Segment){
                onDrawSegment(canvas, (Segment) f);
            }
            else if (f instanceof PointFigure){
                onDrawPointFigure(canvas, (PointFigure) f);
            }
        }

        if (selector != null){
            onDrwSelector(canvas, selector);
        }
    }

    private void onDrawIso(Canvas canvas, Iso i){

        myPaint.setColor(Color.GRAY);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setStrokeWidth(1);

        ArrayList<PointFigure> points = i.getPointsFigure();
        for(PointFigure p : points){
            canvas.drawLine(i.getPoint().x, i.getPoint().y, p.getPoint().x, p.getPoint().y, myPaint);
        }
        if(i.selected == true){
            myPaint.setColor(Color.RED);
            myPaint.setStyle(Paint.Style.FILL);
        }
        canvas.drawCircle(i.getPoint().x, i.getPoint().y, i.getWidthPoint(), myPaint);
    }

    private void onDrawSegment(Canvas canvas, Segment s){

        myPaint.setColor(Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);

        if(s.selected == true){
            myPaint.setColor(Color.RED);
            myPaint.setStyle(Paint.Style.FILL);
        }
        myPaint.setStrokeWidth(3);
        canvas.drawLine(s.getP1().x, s.getP1().y, s.getP2().x, s.getP2().y, myPaint);
    }

    private void onDrawPointFigure(Canvas canvas, PointFigure p){

        myPaint.setColor(Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);
        if(p.selected == true){
            myPaint.setColor(Color.RED);
            myPaint.setStyle(Paint.Style.FILL);
        }
        canvas.drawCircle(p.getPoint().x, p.getPoint().y, p.getWidthPoint(), myPaint);
    }

    private void onDrwSelector(Canvas canvas, Selector s){

        Rect rectangle = s.getRectangle();
        myPaint.setColor(Color.RED);
        myPaint.setStrokeWidth(3);
        myPaint.setAlpha(20);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectangle, myPaint);
        myPaint.setColor(Color.RED);
        myPaint.setAlpha(100);
        myPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectangle, myPaint);
    }
}
