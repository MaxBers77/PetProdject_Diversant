public class Gun {
    private int direction, a,b,r,z;
    private int[] x1x=new int[4];
    private int[] y1y=new int[4];

    public Gun(int direction, int a, int b, int r, int z) {
        this.direction = direction;
        this.a = a;
        this.b = b;
        this.r = r;
        this.z=z;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public int[] getX1x() {
        return x1x;
    }

    public int[] getY1y() {
        return y1y;
    }


    public void move() {
        int x=(int) (a+(r*Math.cos(Math.toRadians(direction-z))));
        int y=(int) (b+(r*Math.sin(Math.toRadians(direction-z))));
        int x1=(int) (a+(r*Math.cos(Math.toRadians(direction+z))));
        int y1=(int)(b+(r*Math.sin(Math.toRadians(direction+z))));
        int x2=(int) (a+(r*Math.cos(Math.toRadians(direction+180-z))));
        int y2=(int)(b+(r*Math.sin(Math.toRadians(direction+180-z))));
        int x3=(int) (a+(r*Math.cos(Math.toRadians(direction+180+z))));
        int y3=(int)(b+(r*Math.sin(Math.toRadians(direction+180+z))));
        int x4=(x+x3)/2;
        int y4=(y+y3)/2;
        int x5=(x1+x2)/2;
        int y5=(y1+y2)/2;

        x1x= new int[]{x,x1,x5,x4};
        y1y= new int[]{y,y1,y5,y4};
    }

}
