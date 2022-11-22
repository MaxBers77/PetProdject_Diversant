import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Controller extends MouseAdapter implements MouseMotionListener    {
    private ModelGame model;
    private View view;
    private Gun gun;
    private int x;
    public MouseEvent ev;
    private Timer shellTimer;
    public Date date;
    private boolean isShellGenerated=false;

    public Controller(ModelGame model) {
        this.model = model;
        view=new View(this,model);
        view.setBackground(Color.WHITE);
        this.gun=view.getGun();
        this.x=680;
        this.date=new Date();
    }

    public Timer getShellTimer() {
        return shellTimer;
    }

    public boolean isShellGenerated() {
        return isShellGenerated;
    }

    public View getView() {
        return view;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        aiming(e);
    }

    public void aiming(MouseEvent e){

        int tempX=e.getX();
        int delta=(tempX-x)/3;
        if (delta>10){
            delta=10;
        }
        if (delta<-10){
            delta=-10;
        }
        if (delta<2 && delta>0){
            delta=2;
        }
        if (delta>-2 &&delta<0){
            delta=-2;
        }
        x=tempX;
        if (gun.getDirection()<=-20&&gun.getDirection()>=-160) {
            int tempDirecnion = gun.getDirection() + delta;
            gun.setDirection(tempDirecnion);
        } else if (gun.getDirection()>=-19){
            gun.setDirection(-20);
        } else if (gun.getDirection()<=-161){
            gun.setDirection(-160);
        }
        view.repaint();

    }

    public int getX() {
        return x;
    }




    @Override
    public void mouseDragged(MouseEvent e) {
        aiming(e);
        Date currentDate=new Date();
        if ((currentDate.getTime()-date.getTime())>300) {

            int direction = view.getGun().getDirection();
            model.generatShell(direction);
            date = new Date();
        }

    }




    @Override
    public void mousePressed(MouseEvent e) {
        shellTimer=new Timer();
        TimerTask taskShell=new TimerTask() {
            @Override
            public void run() {
                isShellGenerated=true;
                Date currentDate=new Date();
                if ((currentDate.getTime()-date.getTime())>200 && !model.isStopShell()) {
                    int direction = view.getGun().getDirection();
                    model.generatShell(direction);
                    date = new Date();
                }
            }
        };
        shellTimer.schedule(taskShell,0,200);
        }

    @Override
    public void mouseReleased(MouseEvent e) {
        shellTimer.cancel();
        isShellGenerated=false;
    }
}
