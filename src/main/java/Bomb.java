public class Bomb extends AbstractObject implements MovableObject {
    private int xStart, yStart, x, y, xTarget,yTarget,deltaX,deltaY;
    protected int startSpeed;
    private double a;
    private ModelGame model;

    public Bomb(int xStart, int yStart, int startSpeed, int targetX, int targetY, ModelGame model) {
        this.model=model;
        this.xStart = xStart;
        this.yStart = yStart;
        this.startSpeed = startSpeed;
        this.x=0;
        this.y=0;
        this.xTarget=targetX;
        this.yTarget=targetY;
        deltaX=xTarget-xStart;
        deltaY=yTarget-yStart;
        a= (double) deltaY/(Math.pow(deltaX,2));

    }


    @Override
    public void move() {

        x+=startSpeed;
        y=(int) (a*Math.pow(x,2));

    }

    @Override
    public String toString() {
        return "x: "+x+" y: "+y+" xSt: "+xStart+" ySt: "+yStart+" deltX: "+deltaX+" deltY: "+deltaY+ " a: "+a;
    }

    @Override
    public void crush() {
        model.incrementScore(500);
    }


    public int getxStart() {
        return xStart;
    }

    public int getyStart() {
        return yStart;
    }

    public int getxTarget() {
        return xTarget;
    }

    public int getyTarget() {
        return yTarget;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public double getA() {
        return a;
    }

    @Override
    public int getX() {
        return x+xStart;
    }

    @Override
    public int getY() {
        return y+yStart;
    }

    @Override
    public void setClipCount() {

    }
}
