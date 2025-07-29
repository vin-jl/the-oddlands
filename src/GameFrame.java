import javax.swing.*;

public class GameFrame extends JFrame {

    private final MainMenu menu;
    private final Overworld overworld;

    public GameFrame(){
        menu = new MainMenu("resources/oddlands_title.png");
        overworld = new Overworld();
        add(menu);
        add(overworld);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(CONST.FRAME_WIDTH, CONST.FRAME_HEIGHT);
        setResizable(false);
        setVisible(true);
    }

    public void menu(){
        menu.setVisible(true);
        while(menu.isInMenu()){
            repaint();
        }
    }

    public void run(){
        menu.setVisible(false);
        overworld.setVisible(true);
        while(true) {
            overworld.update();
            repaint();
            try{Thread.sleep(CONST.FRAME_TIME);} catch(Exception e){System.out.println("didn't sleep :(");}
        }
    }

    public static void main(String[] args){
        GameFrame frame = new GameFrame();
        frame.menu();
        frame.run();
    }

}
