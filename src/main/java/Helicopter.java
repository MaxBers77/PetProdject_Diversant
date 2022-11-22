public class Helicopter extends AbstractObject implements MovableObject{
    private int count=0;
    ModelGame model;
    private int maxFragmentQuantity=4;
    private int minFragmentQuantity=1;
    private int lastParachutistX=0;


    public Helicopter(int direction, ModelGame model, int y) {
        this.model=model;
        clipCount=1;
        if (direction==0) {
            this.horizontalSpeed = -3;
            this.x = 1280;
            this.y = y;
            this.verticalSpeed = 0;
        } else {
            this.horizontalSpeed = 3;
            this.x = 0;
            this.y = y+50;
            this.verticalSpeed = 0;
        }
    }

    @Override
    public String toString() {
        return "x "+x+"y "+y+"horizontalSpeed "+horizontalSpeed+"verticalSpeed "+verticalSpeed;
    }

    @Override
    public void move() {

        x+=horizontalSpeed;
        y+=verticalSpeed;
        if (count<=3){
            double q=Math.random();
            if (q<0.005 && (x<550 || x>700)&&Math.abs(x-lastParachutistX)>20){
                model.getObjects().add(new Parachutist(x,y+30,model));
                lastParachutistX=x;
                count++;
            }
        }
    }

    public void crush(){
        model.incrementScore(100);
        int countFragment=randomNumberGenerate(minFragmentQuantity,maxFragmentQuantity);
        for (int i=0;i<countFragment;i++){
            int c=randomNumberGenerate(-6,15);
            double startSpeed=horizontalSpeed/10*(randomNumberGenerate(4,12)-6);
            if (startSpeed<0 && startSpeed>-3){
                startSpeed=-4;
            }
            if (startSpeed>=0 && startSpeed<3){
                startSpeed=4;
            }
            startSpeed=startSpeed/6.0;
            double a=randomNumberGenerate(5,8)/100.0;
            double b=-0.05;

            model.getObjects().add(new Fragment(x,y,c,a,b,startSpeed));
        }
    }
    public int randomNumberGenerate(int min,int max){
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    @Override
    public void setClipCount() {
        clipCount++;
    }
}
