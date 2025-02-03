public class Point {
    private double x;
    private double y;

    private static int count = 0;

    public Point(double x, double y){
        this.x = x;
        this.y = y;

        count++;
    }

    public static int getCount(){
        return Point.count;
    }

    @Override
    public String toString(){
        return String.format("(%.1f; %.1f)", this.x, this.y);
    }

    public double distanceTo(Point other){
        return Math.hypot(other.x - this.x, other.y - this.y);
    }

    // näiden avulla päästään lukemaan private tietojäseniä
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }


}