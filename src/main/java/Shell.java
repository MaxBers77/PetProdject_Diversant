public class Shell implements MovableObject{
    public static int countShell;
    private int xStart, yStart, direction, distance;
    private int x,y,currentDistance;

    public Shell(int xStart, int yStart, int direction, int distance) {
        countShell++;
        this.xStart = xStart;
        this.yStart = yStart;
        this.direction = direction;
        this.distance = distance;
        this.x=xStart;
        this.y=yStart;
        this.currentDistance=70;
    }

    @Override
    public void move() {
        x=(int) (xStart+currentDistance*Math.cos(Math.toRadians(direction)));
        y=(int) (yStart+currentDistance*Math.sin(Math.toRadians(direction)));
        currentDistance+=distance;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "shell # "+countShell+" x "+x+" y "+y + " direction: "+direction;
    }

    @Override
    public void crush() {

    }

    @Override
    public void setClipCount() {

    }
}
