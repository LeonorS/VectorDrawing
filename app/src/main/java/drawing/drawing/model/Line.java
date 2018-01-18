package drawing.drawing.model;

import android.graphics.Point;
import android.graphics.Rect;

public class Line extends Figure{

    private double margin = 8;
//    private Vector<Intersection> inter;
//    private Model model;

    public Line(int x1, int y1, int x2, int y2, double margin/*, Model model*/){
        super();
        addPoint(new Point(x1, y1));
        addPoint(new Point(x2, y2));
        this.margin = margin;
//        this.inter = new Vector<Intersection>();
//        this.model = model;
    }

//    public Vector<Intersection> getInter(){
//        return inter;
//    }

    public Point getP1(){
        return getPoints().get(0);
    }

    public Point getP2(){
        return getPoints().get(1);
    }

    public void setP1(Point p){
        this.changePoint(p, 0);
    }

    public void setP2(Point p){
        this.changePoint(p, 1);
    }

    public Point set(int x, int y, Point anchor){

        this.getP1().x += x - anchor.x;
        this.getP1().y += y - anchor.y;
        this.getP2().x += x - anchor.x;
        this.getP2().y += y - anchor.y;

        return new Point(x, y);
    }

//    public void setDep(){
//        for (int k = 0; k < inter.size(); k++){
//            if(!inter.get(k).setIntersection())
//                k--;
//        }
//    }

    public void movePoint(Point p, int which){
        super.changePoint(p, which);
    }

    public void movePoint(Point p1, Point p2){
        super.changePoint(p1, 0);
        super.changePoint(p2, 1);
    }

    @Override
    public boolean contains(float x, float y) {

        int x1 = Math.min(getP1().x, getP2().x);
        int x2 = Math.max(getP1().x, getP2().x);
        int y1 = Math.min(getP1().y, getP2().y);
        int y2 = Math.max(getP1().y, getP2().y);

        if(x1 == x2){
            return x >= x1 - margin && x <= x1 + margin && y >= y1 && y <= y2;
        }

        if(y1 == y2){
            return x >= x1 && x <= x2 && y >= y1 - margin && y <= y2 + margin;
        }

        if(x >= x1 && x <= x2 && y >= y1 && y <= y2){

            double m = (double)(getP1().y - getP2().y) / (double)(getP1().x - getP2().x);

            double m1 = (double)(getP1().y - y) / (double)(getP1().x - x);
            double m2 = (double)(getP2().y - y) / (double)(getP2().x - x);

            double margin_2 = (margin * 3.5)/(double)(x2 - x1 + y2 - y1);
            return m >= m1 - margin_2 && m <= m1 + margin_2 || m >= m2 - margin_2 && m <= m2 + margin_2;
        }

        return false;
    }

    @Override
    public boolean intersects(Selector selector) {
        Rect r = selector.rectangle;
        Line top = new Line(r.left, r.top, r.right, r.top, margin);
        Line left = new Line(r.left, r.top, r.left, r.bottom, margin);
        Line bottom = new Line(r.left, r.bottom, r.right, r.bottom, margin);
        Line right = new Line(r.right, r.top, r.right, r.bottom, margin);
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

//    @Override
//    public boolean intersects(Selector selector) {
//        Rect r = selector.rectangle;
//        Line top = new Line(r.left, r.top, r.right, r.top, margin, model);
//        Line left = new Line(r.left, r.top, r.left, r.bottom, margin, model);
//        Line bottom = new Line(r.left, r.bottom, r.right, r.bottom, margin, model);
//        Line right = new Line(r.right, r.top, r.right, r.bottom, margin, model);
//        boolean topp = intersects(top);
//        if (topp) {
//            return true;
//        }
//        boolean leftp = intersects(left);
//        if (leftp) {
//            return true;
//        }
//        boolean bottomp = intersects(bottom);
//        if (bottomp) {
//            return true;
//        }
//        boolean rightp = intersects(right);
//        if (rightp) {
//            return true;
//        }
//        return false;
//    }

    @Override
    public Point move(int x, int y, Point anchor) {

        Point p1 = getP1();
        Point p2 = getP2();
        int x1 = p1.x + x - anchor.x;
        int y1 = p1.y + y - anchor.y;
        int x2 = p2.x + x - anchor.x;
        int y2 = p2.y + y - anchor.y;
        setP1(new Point(x1, y1));
        setP2(new Point(x2, y2));

//        for (int k = 0; k < inter.size(); k++){
//            if(!inter.get(k).setIntersection()) {
//                model.removeInter(inter.get(k));
//                k--;
//            }
//        }

        return new Point(x, y);
    }

    public boolean intersects(Line l){
        int x1 = getP1().x;
        int y1 = getP1().y;
        int x2 = getP2().x;
        int y2 = getP2().y;
        float x3 = l.getP1().x;
        float y3 = l.getP1().y;
        float x4 = l.getP2().x;
        float y4 = l.getP2().y;
        float x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        float y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        if(this.contains(x, y) && l.contains(x, y)){
            return true;
        }
        return false;
    }

    public void setMargin(double value){
        margin = value;
    }
}
