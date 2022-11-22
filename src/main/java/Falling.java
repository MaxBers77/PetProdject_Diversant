public class Falling extends AbstractObject implements MovableObject{


    public Falling(int x, int y) {
        this.verticalSpeed=4;
        this.horizontalSpeed=0;
        this.x=x;
        this.y=y;

    }

    @Override
    public void move() {
        x += horizontalSpeed;
        y += verticalSpeed;
    }

    @Override
    public void crush() {

    }

    @Override
    public void setClipCount() {

    }
}
