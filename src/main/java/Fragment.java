public class Fragment implements MovableObject{
    private int xStart, yStart,c;
    double a,b;
    private int x,y;
    double startSpeed;
    private double currentX;
    protected int[]delta=new int[6];


    public Fragment(int xStart, int yStart, int c, double a, double b, double startSpeed) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.c = c;
        this.a = a;
        this.b = b;
        this.startSpeed = startSpeed;
        this.x = 0;
        this.y = 0;
        this.currentX=x;
        for (int i=0;i<6;i++){
            delta[i]=randomNumberGenerate(0,16)-8;
        }
    }

    @Override
    public void move() {
        currentX+=startSpeed;
        y=(int) (a*(Math.pow(currentX,2))+b*currentX+c);
    }

    @Override
    public int getX() {

        return (int) (currentX+xStart);
    }

    @Override
    public int getY() {

        return y+yStart;
    }

    @Override
    public void crush() {

    }

    @Override
    public void setClipCount() {

    }
    public int randomNumberGenerate(int min,int max){
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    @Override
    public String toString() {
        return "x: "+x+"  y: "+y+"  a: "+a+"  b: "+b+"  c: "+c+"  startSpeed: "+ startSpeed;
    }
}
