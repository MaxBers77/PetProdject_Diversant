import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModelGame {
    private  List<MovableObject> objects;
    private List<MovableObject> diversantsOnTheLeft;
    private List<MovableObject> diversantsOnTheRight;
    private MovableObject[] finalDiversantList=new MovableObject[4];
    private int score;
    private int level;
    private int maxScore=10;
    private boolean isFinish=false;
    private View view;
    private Controller controller;
    private volatile int checkLevel;
    private List<Timer>timers=new ArrayList<>();
    private boolean isTransportStop=false;
    Date stopTransport=new Date();
    private Timer timerMain;
    private boolean isAllArrived=false;
    private int boomCount=0;
    private boolean isBombBoom=false;
    private Date boomStart;
    private  boolean isStopShell=false;
    private String chempionName;
    private int recordScore;
    private Properties properties=new Properties();
    private Path pathToRecord= Path.of("src/main/resources/record.properties").toAbsolutePath();
    private File recordFile=pathToRecord.toFile();


    public ModelGame() {
        this.objects=new CopyOnWriteArrayList<>();
        diversantsOnTheLeft=new CopyOnWriteArrayList<>();
        diversantsOnTheRight=new CopyOnWriteArrayList<>();
        this.score=0;
        this.level=0;
        try {
            properties.load(new FileReader(recordFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.chempionName=properties.getProperty("name");
        this.recordScore=Integer.parseInt(properties.getProperty("record"));
        

    }
    public void setView(View view){
        this.view=view;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }


    public void incrementScore(int incr){
        score+=incr;
        if(score>1000 && score<2000){
            level=1;
        } else if(score>=2000 && score<4000){
            level=2;
        } else if (score>=4000 & score<7000){
            level =3;
        } else if (score>=7000){
            level=4;
        }
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void gameProcess(){
        generatTransport(100);
        //generatePlane();
        moveObject();






        //Запуск дополнительных потоков генерации транспорта в зависимости от уровня
        while (!isFinish){
           // System.out.println("пасем уровни");
            if (level!=checkLevel){
                if (level==1){
                    generatTransport(85);
                    checkLevel++;
                } else if (level==2){
                    generatTransport(135);
                    checkLevel++;
                } else if (level==3){
                    generatePlane();
                    checkLevel++;
                } else if (level==4){
                    generatTransport(160);
                    checkLevel++;
                }
            }
        }

    }


    //главный игровой поток
    private void moveObject() {
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
  //              System.out.println("поток не остановлен");
                for (MovableObject object: objects){
                    // Для всех объектов кроме диверсантов перерасчет координат
                    // Старт движения диверсантов после проверки после цикла
                    if (!(object instanceof Diversant)){
                        object.move();
                    }
                    //Удаление объектов при выходе за пределы экрана
                    if ((object.getY()<=0||object.getX()<=0||object.getX()>=1280||object.getY()>=611)&&!(object instanceof Gun)&&!(object instanceof Tnt)){
                        objects.remove(object);
                    }
                    //проверка на попадание разрушающих объектов в цели
                    if (object instanceof Shell || object instanceof Fragment || object instanceof Falling) {
                        checkHit(object);
                    }
                    //При достижении бомбой орудия объект Bomb удаляем, создаем взрывчатку без
                    //фазы горения  (isBombBoom = true), запрещаем отрисовку орудия (view.setCrush(true)
                    //отключаем листенеры, останавливаем потоки генерации транспорта и снарядов
                    if (object instanceof Bomb && object.getY()>=560) {
                        isBombBoom = true;
                        objects.remove(object);
                        objects.add(new Tnt(640));
                        view.setCrush(true);
                        boomCount = 1;
                        boomStart = new Date();
                        view.removeMouseListener(controller);
                        view.removeMouseMotionListener(controller);
                        if (controller.isShellGenerated()) {
                            controller.getShellTimer().cancel();
                        }
                        if (!isTransportStop){
                            stopTransport();
                            isTransportStop=true;
                            stopTransport=new Date();
                        }

                    }
                    //Для объекта Tnt рассчитываем время показа для каждого из трех кадров,
                    //после объект удаляем
                    if (object instanceof Tnt) {
                        long deltData = new Date().getTime() - boomStart.getTime();
                        if (deltData > 200 && deltData <= 400) {
                            boomCount = 2;
                        }
                        if (deltData > 400 && deltData <= 600) {
                            boomCount = 3;
                        }
                        if (deltData > 600) {
                            boomCount = 0;
                            objects.remove(object);
                            isFinish=true;
                        }
                    }
                    //Проверка на остановку mainTimer (в зависимости от причины уничтожения - бомба
                    //или диверсанты
                    if (isFinish){
                        if (!isBombBoom){
                            timerMain.cancel();
                        } else if (new Date().getTime()-boomStart.getTime()>=15000){
                            timerMain.cancel();
                        }
                    }

                    //Если парашютист достиг поверхности - удаляем его из objects, добавляем вместо него
                    //диверсанта, помещаем ссылку на нового диверсанта в лист для правой либо левой стороны
                    if (object instanceof Parachutist && object.getY()>=600){
                        Diversant diversant=new Diversant(object.getX()-10);
                        objects.add(diversant);
                        if (object.getX()<640){
                            diversantsOnTheLeft.add( diversant);
                        }
                        if ((object.getX()>640)){
                            diversantsOnTheRight.add( diversant);
                        }
                        objects.remove(object);
                    }
                }
                //перерисовываем View
                view.notifyAboutMove();
                //Останавливаем потоки генерации транспорта при успешном приземлении четырех диверсантов
                //с любой стороны. Засекаем время остановки.
                if ((diversantsOnTheRight.size()>=4 || diversantsOnTheLeft.size()>=4) && !isTransportStop){
                    stopTransport();
                    isTransportStop=true;
                    stopTransport=new Date();
                }


                if (new Date().getTime()-stopTransport.getTime()>20000 && isTransportStop && !isStopShell){

                    //Проверяем, что количество диверсантов попрежнему не меньше четырех
                    if (diversantsOnTheRight.size()>=4 || diversantsOnTheLeft.size()>=4) {
                        // Убираем возможность генерации снарядов, чтобы они не зависли на экране после остановки timerMain
                        isStopShell = true;
                    } else {
                        // запускаем потоки генерации транспорта
                        // при этом проверяем, не уничтожено ли орудие бомбой
                        if (!isBombBoom) {
                            isTransportStop = false;
                            restartTransport();
                        }
                    }
                }

                //Ждем 23 секунды после остановки потоков генерации транспорта, чтобы убедиться, что
                // на земле имеется четыре диверсанта
                if (new Date().getTime()-stopTransport.getTime()>23000 && isTransportStop && !isBombBoom){

                    if (diversantsOnTheRight.size()>=4) {
                        Collections.sort(diversantsOnTheRight, new Comparator<MovableObject>() {
                            @Override
                            public int compare(MovableObject o1, MovableObject o2) {
                                return o1.getX()-o2.getX();
                            }
                        });
                        for (int i = 0; i < 4; i++) {
                            Diversant diversant=(Diversant) diversantsOnTheRight.get(i);
                            diversant.setCountOfDivers(i);
                            finalDiversantList[i]=diversant;
                        }
                    } else if (diversantsOnTheLeft.size()>=4){
                        Collections.sort(diversantsOnTheLeft, new Comparator<MovableObject>() {
                            @Override
                            public int compare(MovableObject o1, MovableObject o2) {
                                return o2.getX()- o1.getX();
                            }
                        });
                        for (int i=0;i<4;i++){
                            Diversant diversant=(Diversant) diversantsOnTheLeft.get(i);
                            diversant.setCountOfDivers(i);
                            finalDiversantList[i]=diversant;
                        }
                    }

                    if(!isBombBoom) {
                        moveDiversant();
                    }
                }

            }
        };
        timerMain=new Timer();
        timerMain.schedule(task,0,10);
    }

    private void restartTransport() {
        timers.clear();
        switch (level){
            case 0:
                generatTransport(100);
                break;
            case 1:
                generatTransport(100);
                generatTransport(85);
                break;
            case 2:
                generatTransport(100);
                generatTransport(85);
                generatTransport(135);
                break;
            case 3:
                generatTransport(100);
                generatTransport(85);
                generatTransport(135);
                generatePlane();
                break;
            case 4:
                generatTransport(100);
                generatTransport(85);
                generatTransport(135);
                generatePlane();
                generatTransport(160);
                break;
        }
    }


    //Запуск движения диверсантов
    private void moveDiversant() {
        timerMain.cancel();
        timers.clear();

        TimerTask diversantTask=new TimerTask() {

            @Override
            public void run() {
                MovableObject object=finalDiversantList[0];
                for (int i=0;i<4;i++) {
                    object=finalDiversantList[i];
                    Diversant diversant = (Diversant) object;
                    if (diversant.isArrived) {
                        if (i==3){

                            if (object.getX()<640){
                                objects.add(new Tnt(591));
                            }
                            if (object.getX()>640){
                                objects.add(new Tnt(669));
                            }
                            isAllArrived=true;
                        }
                        continue;
                    }
                    break;
                }
                object.move();
                view.notifyAboutMove();

            }
        };
        Timer diversantTimer=new Timer();
        diversantTimer.schedule(diversantTask,0,100);
        while (!isAllArrived){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        diversantTimer.cancel();
        if (controller.isShellGenerated()){
            controller.getShellTimer().cancel();
        }
        tntStart();


    }

    //метод уничтожения орудия, счетчик кадров взрыва для метода boomPaint во View
    private void boom() {
        view.setCrush(true);
        for (int i=1;i<=4;i++){
            boomCount=i;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public int getBoomCount() {
        return boomCount;
    }

    //Метод закладки взрывчатки, паузы с горением, старт уничтожения орудия
    private void tntStart() {
        TimerTask tntTask=new TimerTask() {
            @Override
            public void run() {
                view.notifyAboutMove();
            }
        };
        Timer tntTimer=new Timer();
        tntTimer.schedule(tntTask,0,500);
        if (!isBombBoom) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        boom();
        tntTimer.cancel();
        isFinish=true;

    }


    public void saveRecord(String chName){
        properties.setProperty("name",chName);
        properties.setProperty("record",score+"");

        try {
            OutputStream out=new FileOutputStream(recordFile);
            OutputStream outputStream = new FileOutputStream(recordFile);
            properties.store(outputStream," ");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void stopTransport() {
        // Закрываем все потоки генерации транспорта
        for (Timer timer : timers) {
            timer.cancel();
        }
    }


    //Проверка попаданий
    private void checkHit(MovableObject object) {
        for (MovableObject obj:objects){
            if  (!(obj instanceof Fragment) && !(obj instanceof Shell)&&!(obj instanceof Falling)){
                if ((!(obj instanceof Parachutist)) && object.getX()>obj.getX()-20 && object.getX()<obj.getX()+20 && object.getY()>obj.getY()-20 && object.getY()<obj.getY()+20){
                        obj.crush();
                        objects.remove(obj);
                            objects.remove(object);
                            if (obj instanceof Diversant){
                                if (obj.getX()<640){
                                    diversantsOnTheLeft.remove(obj);
                                }
                                if (obj.getX()>640){
                                    diversantsOnTheRight.remove(obj);
                                }
                            }
                }
            }
            //Проверка попадания в парашют
            if (obj instanceof Parachutist && object.getX()>obj.getX()-20 && object.getX()<obj.getX()+20 && object.getY()>obj.getY()-20 && object.getY()<obj.getY()+20){
                ((Parachutist) obj).crush(object);
                objects.remove(obj);
            }
        }
    }
    //Запуск потока генерации самолетов
    public void generatePlane(){
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                if (!isFinish){
                    objects.add(new Plane(helicopterDirection(),ModelGame.this));
                } else {
                    // Завершение игры
                }
            }
        };
        Timer timer=new Timer();
        timers.add(timer);
        timer.schedule(task,500,12000);
    }


    //Запуск потоков генерации вертолетов
    public void generatTransport(int y){
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                if (!isFinish){
                    objects.add(new Helicopter(helicopterDirection(),ModelGame.this,y));
                } else {
                    // Завершение игры
                }
            }
        };
        Timer timer=new Timer();
        timers.add(timer);
        timer.schedule(task,1000,6000);

}
//Метод определения направления движения транспорта
public int helicopterDirection(){
    return (int)Math.round( Math.random());
}

    public List<MovableObject> getObjects() {
        return objects;
    }

    //Метод генерации снарядов
    public void generatShell(int direction){
        if (!isStopShell) {
            objects.add(new Shell(640, 560, direction, 3));
        }

    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public boolean isStopShell() {
        return isStopShell;
    }

    public String getChempionName() {
        return chempionName;
    }

    public int getRecordScore() {
        return recordScore;
    }
}
