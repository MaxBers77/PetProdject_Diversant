import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ModelGame model=new ModelGame();
        Controller controller=new Controller(model);
        model.setController(controller);
        JFrame game=new JFrame("Диверсант");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(1280,650);
        game.setContentPane(controller.getView());
        model.setView(controller.getView());
        game.setVisible(true);
        model.gameProcess();

        if (model.getScore()> model.getRecordScore()) {
            String recordName = JOptionPane.showInputDialog(game,"Поздравляем, вы побили рекорд "+model.getRecordScore()+" установленный "+model.getChempionName()+", введте ваше имя",model.getChempionName());
            if (recordName==null){
                recordName=model.getChempionName();
            }

            model.saveRecord(recordName);
        }
        JOptionPane.showMessageDialog(game,"ИГРА ОКОНЧЕНА!!!");

        System.exit(0);
    }
}
