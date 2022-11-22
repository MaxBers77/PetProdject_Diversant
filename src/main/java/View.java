import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class View extends JPanel {
    private boolean isCrush=false;
    private Controller controller;
    private ModelGame model;
    private Gun gun;
    private Image parachutistImage;
    private Image planeLImage;
    private Image planeRImage;
    private Image helicopterL1Image;
    private Image helicopterL2Image;
    private Image helicopterR1Image;
    private Image helicopterR2Image;
    private Image bombLImage;
    private Image bombRImage;
    private Image fallingImage;
    private Image diversantImage;
    private Image tntImage;
    private Image tntImage1;
    private Image boom1Image;
    private Image boom2Image;
    private Image boom3Image;

    public View(Controller controller, ModelGame model) {
        this.controller = controller;
        this.model=model;
        this.gun=new Gun(-90,640,560,30,10);
        addMouseMotionListener(controller);
        addMouseListener(controller);
        Path pathParachutist= Path.of("src\\main\\resources\\parachutist.bmp").toAbsolutePath();
        File fileParachutist=pathParachutist.toFile();
        Path pathPlaneL=Path.of("src\\main\\resources\\plane2L.png").toAbsolutePath();
        File filePlaneL=pathPlaneL.toFile();
        Path pathPlaneR=Path.of("src\\main\\resources\\plane2R.png").toAbsolutePath();
        File filePlaneR=pathPlaneR.toFile();
        Path pathHelicopterL1=Path.of("src\\main\\resources\\helicopterL1.jpg").toAbsolutePath();
        File fileHelicopterL1=pathHelicopterL1.toFile();
        Path pathHelicopterL2=Path.of("src\\main\\resources\\helicopterL2.jpg").toAbsolutePath();
        File fileHelicopterL2=pathHelicopterL2.toFile();
        Path pathHelicopterR1=Path.of("src\\main\\resources\\helicopterR1.jpg").toAbsolutePath();
        File fileHelicopterR1=pathHelicopterR1.toFile();
        Path pathHelicopterR2=Path.of("src\\main\\resources\\helicopterR2.jpg").toAbsolutePath();
        File fileHelicopterR2=pathHelicopterR2.toFile();
        Path pathBombL=Path.of("src\\main\\resources\\bombL.jpg").toAbsolutePath();
        File fileBombL=pathBombL.toFile();
        Path pathBombR=Path.of("src\\main\\resources\\bombR.jpg").toAbsolutePath();
        File fileBombR=pathBombR.toFile();
        Path pathFalling=Path.of("src\\main\\resources\\falling.jpg").toAbsolutePath();
        File fileFalling=pathFalling.toFile();
        Path pathDiversant=Path.of("src\\main\\resources\\divers.jpg").toAbsolutePath();
        File fileDiversant=pathDiversant.toFile();
        Path pathTnt=Path.of("src\\main\\resources\\tnt.jpg").toAbsolutePath();
        File fileTnt=pathTnt.toFile();
        Path pathTnt1=Path.of("src\\main\\resources\\tnt2.jpg").toAbsolutePath();
        File fileTnt1=pathTnt1.toFile();
        Path pathboom1=Path.of("src\\main\\resources\\boom1.jpg").toAbsolutePath();
        File fileboom1=pathboom1.toFile();
        Path pathboom2=Path.of("src\\main\\resources\\boom2.jpg").toAbsolutePath();
        File fileboom2=pathboom2.toFile();
        Path pathboom3=Path.of("src\\main\\resources\\boom3.jpg").toAbsolutePath();
        File fileboom3=pathboom3.toFile();
        try {
            this.tntImage=ImageIO.read(fileTnt);
            this.tntImage1=ImageIO.read(fileTnt1);
            this.parachutistImage= ImageIO.read(fileParachutist);
            this.planeLImage=ImageIO.read(filePlaneL);
            this.planeRImage=ImageIO.read(filePlaneR);
            this.helicopterL1Image=ImageIO.read(fileHelicopterL1);
            this.helicopterL2Image=ImageIO.read(fileHelicopterL2);
            this.helicopterR1Image=ImageIO.read(fileHelicopterR1);
            this.helicopterR2Image=ImageIO.read(fileHelicopterR2);
            this.bombLImage=ImageIO.read(fileBombL);
            this.bombRImage=ImageIO.read(fileBombR);
            this.fallingImage=ImageIO.read(fileFalling);
            this.diversantImage=ImageIO.read(fileDiversant);
            this.boom1Image=ImageIO.read(fileboom1);
            this.boom2Image=ImageIO.read(fileboom2);
            this.boom3Image=ImageIO.read(fileboom3);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void notifyAboutMove() {
        repaint();
    }

    public void setCrush(boolean crush) {
        isCrush = crush;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);


            gunPaint(g);
            repaint();

        scorePaint(g);
        for (MovableObject object:model.getObjects()){
            if (object instanceof Helicopter){
                helicopterPaint(g,object);
            }
            if (object instanceof Parachutist){
                parachutistPaint(g,object);
            }
            if (object instanceof Shell){
                shellPaint(g,object);
            }
            if (object instanceof Fragment){
                fragmentPaint(g,object);
            }
            if (object instanceof Falling){
                fallingPaint(g,object);
            }
            if (object instanceof Plane){
                planePaint(g,object);
            }
            if (object instanceof Bomb){
                bombPaint(g,object);
            }
            if (object instanceof Diversant){
                diversantPaint(g,object);
            }
            if (object instanceof Tnt && !isCrush){
                tntPaint(g,object);
            }
            if (object instanceof Tnt && isCrush){
                boomPaint(g,object);
            }


        }
    }

    private void boomPaint(Graphics g, MovableObject object) {
        if (model.getBoomCount()==1){
            g.drawImage(boom1Image,615,510,50,50,null);
        }
        if (model.getBoomCount()==2){
            g.drawImage(boom2Image,615,510,50,50,null);
        }
        if (model.getBoomCount()==3){
            g.drawImage(boom3Image,615,510,50,50,null);
        }
    }

    private void tntPaint(Graphics g, MovableObject object) {
        Tnt tnt=(Tnt) object;
        int clipCount=tnt.clipCount;
        if (clipCount%2==0){
            g.drawImage(tntImage,object.getX(),535,20,20,null);
        } else {
            g.drawImage(tntImage1,object.getX(),535,20,20,null);
        }
        object.setClipCount();

    }

    private void diversantPaint(Graphics g, MovableObject object) {
        g.drawImage(diversantImage,object.getX(),object.getY(),15,20,null);
    }

    private void bombPaint(Graphics g, MovableObject object) {
        Bomb bomb=(Bomb)object;
        if (bomb.startSpeed>0) {
            g.drawImage(bombLImage, object.getX(), object.getY(), 10, 10, null);
        } else {
            g.drawImage(bombRImage, object.getX(), object.getY(), 10, 10, null);
        }
    }

    private void planePaint(Graphics g, MovableObject object) {
        //g.setColor(Color.CYAN);
        Plane plane=(Plane) object;
        if(plane.horizontalSpeed>0) {
            g.drawImage(planeLImage, object.getX() - 10, object.getY() - 10, 23, 14, null);
        } else {
            g.drawImage(planeRImage, object.getX() - 10, object.getY() - 10, 23, 14, null);
        }
    }

    private void scorePaint(Graphics g) {
        g.drawString("SCORE: "+model.getScore()+"   LEVEL: "+model.getLevel()+"   RECORDSCORE: "+model.getRecordScore(),10,10);
    }

    private void fallingPaint(Graphics g, MovableObject object) {
        g.drawImage(fallingImage,object.getX(),object.getY()-10,10,20,null);
    }

    private void fragmentPaint(Graphics g, MovableObject object) {
        g.setColor(Color.BLACK);
        Fragment fragment=(Fragment) object;
        int[]xDelta=new int[3];
        int[]yDelta=new int[3];
        for (int i=0;i<3;i++){
            xDelta[i]=fragment.delta[i]+object.getX();
            yDelta[i]=fragment.delta[i+3]+object.getY();
        }
        g.fillPolygon(xDelta,yDelta,3);

    }

    private void shellPaint(Graphics g, MovableObject object) {
        //g.setColor(Color.RED);
        g.fillOval(object.getX(),object.getY(),10,10);
    }

    private void gunPaint(Graphics g) {
        g.drawLine(0,610,1280,610);
        g.fillRect(590,560,100,50);
        if (!isCrush) {
            g.fillOval(620, 540, 40, 40);
            gun.move();
            g.fillPolygon(gun.getX1x(), gun.getY1y(), 4);
        }

    }

    private void parachutistPaint(Graphics g, MovableObject object) {
        g.drawImage(parachutistImage,object.getX()-10,object.getY()-10,18,30,null);
    }

    private void helicopterPaint(Graphics g,MovableObject object) {
        Helicopter helicopter=(Helicopter) object;
        int clipCount=helicopter.clipCount;
        if (helicopter.horizontalSpeed>0){
            if (clipCount%2==0){
                g.drawImage(helicopterL1Image,object.getX(),object.getY(),25,16,null);
            } else {
                g.drawImage(helicopterL2Image,object.getX(),object.getY(),25,16,null);
            }
        } else {
            if (clipCount%2==0){
                g.drawImage(helicopterR1Image,object.getX(),object.getY(),25,16,null);
            } else {
                g.drawImage(helicopterR2Image,object.getX(),object.getY(),25,16,null);
            }
        }
        object.setClipCount();
    }

    public Gun getGun() {
        return gun;
    }

}
