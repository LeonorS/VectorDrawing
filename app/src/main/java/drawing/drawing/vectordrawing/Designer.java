package drawing.drawing.vectordrawing;

/**
 * Created by leo on 19/01/18.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Iso;
import drawing.drawing.model.PointFigure;
import drawing.drawing.model.Line;
import drawing.drawing.model.Selector;

public class Designer {

    private Paint myPaint;

    public Designer(){
        myPaint = new Paint();
        myPaint.setAntiAlias(true);
    }

    public void onDrawIso(Canvas canvas, Iso i, ArrayList<Figure> points){

        myPaint.setColor(Color.GRAY);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setStrokeWidth(1);

        Log.d("onDrawIso", "" + points.size());


        for(Figure f : points){

            PointFigure p = (PointFigure) f;

            int x1, y1, x2, y2;
            try {
                x1 = i.getPoint().x;
            } catch (Exception e){
                Log.d("onDrawIso", "x1");
                e.printStackTrace();
                return;
            }
            try {
                y1 = i.getPoint().y;
            } catch (Exception e){
                Log.d("onDrawIso", "y1");
                e.printStackTrace();
                return;
            }
            try {
                x2 = p.getPoint().x;
            } catch (Exception e){
                Log.d("onDrawIso", "x2");
                e.printStackTrace();
                return;
            }
            try {
                y2 = p.getPoint().y;
            } catch (Exception e){
                Log.d("onDrawIso", "y2");
                e.printStackTrace();
                return;
            }
            canvas.drawLine(x1, y1, x2, y2, myPaint);
        }
        if(i.selected == true){
            myPaint.setColor(Color.RED);
            myPaint.setStyle(Paint.Style.FILL);
        }
        canvas.drawCircle(i.getPoint().x, i.getPoint().y, i.getWidthPoint(), myPaint);
    }

    public void onDrawSegment(Canvas canvas, Line s){

        myPaint.setColor(Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);

        if(s.selected == true){
            myPaint.setColor(Color.RED);
            myPaint.setStyle(Paint.Style.FILL);
        }
        myPaint.setStrokeWidth(3);
        canvas.drawLine(s.getP1().x, s.getP1().y, s.getP2().x, s.getP2().y, myPaint);
        canvas.drawCircle(s.getP1().x, s.getP1().y, 5, myPaint);
        canvas.drawCircle(s.getP2().x, s.getP2().y, 5, myPaint);
    }

    public void onDrawLine(Canvas canvas, Line s){

        myPaint.setColor(Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);

        if(s.selected == true){
            myPaint.setColor(Color.RED);
            myPaint.setStyle(Paint.Style.FILL);
        }
        myPaint.setStrokeWidth(3);
        canvas.drawLine(s.getP1().x, s.getP1().y, s.getP2().x, s.getP2().y, myPaint);
    }

    public void onDrawPointFigure(Canvas canvas, PointFigure p){

        myPaint.setColor(Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);
        if(p.selected == true){
            myPaint.setColor(Color.RED);
            myPaint.setStyle(Paint.Style.FILL);
        }
        canvas.drawCircle(p.getPoint().x, p.getPoint().y, p.getWidthPoint(), myPaint);
    }

    public void onDrawSelector(Canvas canvas, Selector s){

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
