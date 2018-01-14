package drawing.drawing.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by leo on 06/12/17.
 */

public class Segment extends Figure{

    private float x1, y1, x2, y2;
    private double margin = 8;

    public Segment(float x1, float y1, float x2, float y2, double margin){
        super();
        addPoint(new Point((int)x1, (int)y1));
        addPoint(new Point((int)x2, (int)y2));
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.margin = margin;
    }

    @Override
    public boolean contains(float x, float y) {
        int x1 = (int)Math.min(this.x1, this.x2);
        int x2 = (int)Math.max(this.x1, this.x2);
        int y1 = (int)Math.min(this.y1, this.y1);
        int y2 = (int)Math.max(this.y1, this.y2);

        if(x1 == x2){
            return x >= x1 - margin && x <= x1 + margin && y >= y1 && y <= y2;
        }
        if(y1 == y2){
            return x >= x1 && x <= x2 && y >= y1 - margin && y <= y1 + margin;
        }
        if(x >= x1 && x <= x2 && y >= y1 && y <= y2){
            double m = (this.y1 - this.y2) / (this.x1 - this.x2);
            double m1 = (this.y1 - y) / (this.x1 - x);
            double m2 = (this.y2 - y) / (this.x2 - x);
            double margin_2 = (margin * 2.5)/(double)(x2 - x1 + y2 - y1);
            return m >= m1 - margin_2 && m <= m1 + margin_2 || m >= m2 - margin_2 && m <= m2 + margin_2;
        }
        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        myPaint.setColor(Color.BLUE);
        myPaint.setStyle(Paint.Style.FILL);
        if(selected == true){
            myPaint.setColor(Color.RED);
            myPaint.setStyle(Paint.Style.FILL);
        }
        myPaint.setStrokeWidth(4);
        canvas.drawLine(x1, y1, x2, y2, myPaint);
    }

    @Override
    public Point move(int x, int y, Point anchor) {

        x1 += x - anchor.x;
        y1 += y - anchor.y;
        x2 += x - anchor.x;
        y2 += y - anchor.y;
        anchor = new Point(x, y);
        return anchor;
    }

    @Override
    public boolean intersects(Selector selector) {
        Rect r = selector.rectangle;
        Segment top = new Segment(r.left, r.top, r.right, r.top, margin);
        Segment left = new Segment(r.left, r.top, r.left, r.bottom, margin);
        Segment bottom = new Segment(r.left, r.bottom, r.right, r.bottom, margin);
        Segment right = new Segment(r.right, r.top, r.right, r.bottom, margin);
        boolean topp = intersects(top);
        if (topp) {
            return true;
        }
        boolean leftp = intersects(left);
        if (leftp) {
            return true;
        }
        boolean bottomp = intersects(bottom);
        if (bottomp) {
            return true;
        }
        boolean rightp = intersects(right);
        if (rightp) {
            return true;
        }
        return false;
    }

    public boolean intersects(Segment l){
        float x3 = l.getCoordinate().get(0);
        float y3 = l.getCoordinate().get(1);
        float x4 = l.getCoordinate().get(2);
        float y4 = l.getCoordinate().get(3);
        float x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        float y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        if(this.contains(x, y) && l.contains(x, y)){
            return true;
        }
        return false;
    }

    public ArrayList<Float> getCoordinate(){
        ArrayList<Float> a = new ArrayList<>(4);
        a.add(x1);
        a.add(y1);
        a.add(x2);
        a.add(y2);
        return a;
    }

    public void setMargin(double value){
        margin = value;
    }
}
