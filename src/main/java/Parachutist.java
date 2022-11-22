public class Parachutist extends AbstractObject implements MovableObject{
    ModelGame model;
    private double currentY;
    public Parachutist(int x, int y, ModelGame model) {
        this.x=x;
        this.y=y;
        horizontalSpeed=0;
        verticalSpeed=0.55;
        this.model=model;
        this.currentY=y;
    }

    @Override
    public void move() {
            x += horizontalSpeed;
            currentY += verticalSpeed;
        if (currentY>=600){
            currentY=600;
        }
    }

    @Override
    public int getY() {
        return (int)currentY;
    }

    @Override
    public String toString() {
            return "парашютист"+"x "+x+"y "+y+"horizontalSpeed "+horizontalSpeed+"verticalSpeed "+verticalSpeed;
        }

    @Override
    public void crush() {

    }
    public void crush(MovableObject object){
        model.incrementScore(20);
        if (object.getX()>getX()-20 && object.getX()<getX()+20 && object.getY()>getY()-20 && object.getY()<getY()){
            model.getObjects().add(new Falling(x,getY()+20));
        }
    }

    @Override
    public void setClipCount() {

    }
}

