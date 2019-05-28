import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Random;



public class Screen extends JPanel {
    private Graphics g;
    private int scale = 1;
    private int width = 1280 * scale;
    private int height = 720 * scale;
    private int screen=0;
    public HashMap<Integer, Boolean> key=new HashMap<>();
    Random rand = new Random();
    private int[][] stars;

    Screen(){
        Main.f.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                key.put(e.getKeyCode(),true);
                if(e.getKeyCode()==KeyEvent.VK_F){System.exit(0);}
            }
            public void keyReleased(KeyEvent e) {
                key.put(e.getKeyCode(),false);
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

            }
        });
    }
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;
        // Draw background
        g.setColor(Color.black);
        g.fillRect(0, 0, width,height);
        paintFullScreen();
    }
    private void paintFullScreen() {
        if(screen==0){
            new drawablestring(width/10,height/20,width/10*8,height/10*2,"Asteroids",Color.white).draw(g);
            System.arraycopy(stars, 0, stars, 1, 59);
            stars[0] =new int[]{rand.nextInt(width),rand.nextInt(height),rand.nextInt(10)};
        }
        //for(int i=0;i++<10;){
        //    g.fillOval(rand.nextInt(width),rand.nextInt(height),rand.nextInt(10),rand.nextInt(10));
        //}

    }
}
