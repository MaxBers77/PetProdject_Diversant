public class Diversant extends AbstractObject implements MovableObject{
    protected int countOfDivers;
    protected boolean isArrived=false;

    public Diversant(int x) {
        this.x=x;
        this.y=590;
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public void move() {
        if (countOfDivers==0) {
            if ( x < 575) {
                x += 10;
            } else if (x>=575 && x<600){
                x = 575;
                isArrived=true;
            } else if(x>690){
                x -= 10;
            } else if (x<=690 && x>640){
                x=690;
                isArrived=true;
            }
        } else if (countOfDivers==1){
            if ( x < 559) {
                x += 10;
            } else if (x>=559 && x<575){
                x = 559;
                isArrived=true;
            } else if(x>706){
                x -= 10;
            } else if (x<=706 && x>690){
                x=706;
                isArrived=true;
            }
        } else if (countOfDivers==2){
            if ( x < 575) {
                x += 10;
            } else if (x>=575 && x<600){
                x = 575;
                isArrived=true;
            } else if(x>690){
                x -= 10;
            } else if (x<=690 && x>640){
                x=690;
                isArrived=true;
            }
            if ((x>=559 && x<575) || (x<=706 && x>690)){
                y=568;
            }
        } else if (countOfDivers==3){
            if ( x < 575) {
                x += 10;
            } else if (x>=575 && x<600){
                x = 575;
                isArrived=true;
            } else if(x>690){
                x -= 10;
            } else if (x<=690 && x>640){
                x=690;
                isArrived=true;
            }
            if ((x>=559 && x<575) || (x<=706 && x>690)){
                y=568;
            } else if((x>=575 && x<600) || x<=690 && x>640){
                y=546;
            }
        }

    }

    @Override
    public void crush() {}

    @Override
    public void setClipCount() {}

    public void setCountOfDivers(int countOfDivers) {
        this.countOfDivers = countOfDivers;
    }


}

