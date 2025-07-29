import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private JButton startButton;
    private boolean inMenu;
    private BufferedImage background;

    public MainMenu(String imgDir){
        setSize(CONST.FRAME_WIDTH, CONST.FRAME_HEIGHT);
        initComponents();
        try {
            background = ImageIO.read(new File(imgDir));
        }catch (IOException e) {System.out.println("Background image not found at " + imgDir);}
        inMenu = true;
    }

    private void initComponents(){
        startButton = new JButton();
        startButton.setOpaque(true);
        startButton.setBackground(new java.awt.Color(26, 101, 158));
        startButton.setForeground(new java.awt.Color(239, 239, 208));
        startButton.setText("Start Game");
        startButton.setAlignmentY(0.0F);
        startButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(277, 277, 277)
                                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(277, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(382, Short.MAX_VALUE)
                                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(210, 210, 210))
        );
    }

    public boolean isInMenu(){
        return inMenu;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, CONST.FRAME_WIDTH, CONST.FRAME_HEIGHT);
        g.drawImage(background, 0, 0, null);
    }

    private void startButtonActionPerformed(ActionEvent evt){
        inMenu = false;
    }

}
