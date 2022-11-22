public class Plane extends AbstractObject implements MovableObject {
    private int count=0;
    ModelGame model;
    private int maxFragmentQuantity=4;
    private int minFragmentQuantity=1;
    private int firstBombX=0;

    public Plane(int direction, ModelGame model) {
        this.model = model;
        if (direction==0) {
            this.horizontalSpeed = -4;
            this.x = 1280;
            this.y = 30;
            this.verticalSpeed = 0;
        } else {
            this.horizontalSpeed = 4;
            this.x = 0;
            this.y = 65;
            this.verticalSpeed = 0;
        }
    }

    @Override
    public void move() {
        x+=horizontalSpeed;
        y+=verticalSpeed;
        if ((count<2 && horizontalSpeed<0 && x>800) ||(count<2 && horizontalSpeed>0 && x<500)){
            double q=Math.random();
            if (q<0.1 && Math.abs(x-firstBombX)>50){
                int startSpeed=2;
                if (horizontalSpeed<0){
                    startSpeed=-2;
                }
                model.getObjects().add(new Bomb(x,y,startSpeed,640,610,model));
                count++;
                firstBombX=x;
            }
        }
    }


    @Override
    public void crush() {
        model.incrementScore(200);
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

    }
}
