public class Tnt extends AbstractObject implements MovableObject{
    private int inhibitor=1;
    public Tnt(int x) {
        this.x=x;
    }

    @Override
    public void move() {

    }

    @Override
    public void crush() {

    }

    @Override
    public void setClipCount() {
        if (inhibitor%2==0) {
            clipCount++;
            inhibitor++;
        } else {
            inhibitor++;
        }

    }
}
