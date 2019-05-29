import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;



public class Screen extends JPanel {
    private Graphics g;
    private int scale = 1;
    private int width = 1280 * scale;
    private int height = 720 * scale;
    private int screen=0;
    double playerX;
    double playerY;
    int playerXVelocity;
    int playerYVelocity;
    public HashMap<Integer, Boolean> key=new HashMap<>();
    Random rand = new Random();
    private int[][] stars=new int[120][];
    private static drawablebutton start;
    private static ArrayList<uielement> buttons=new ArrayList<>();
    uielement elementinfocus;
    private int x_location,y_location;

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
                click(e.getX(),e.getY());
            }
        });
        for(int i=0; i<stars.length;i++){
            stars[i] =new int[]{rand.nextInt(width),rand.nextInt(height),20+rand.nextInt(80)-i,0,0};
        }
        start=new drawablebutton(width/10*4,height/10*7,width/10*2,height/10,"Start",true);
    }
    private void click(int x, int y){
        for(uielement button:buttons){
            if((button.getx()-x_location < x && x < button.getx()-x_location + button.getwidth()) && (button.gety()-y_location < y && y < button.gety()-y_location + button.getheight())) {
                elementinfocus=button;
                elementinfocus.clicked(x+x_location-elementinfocus.getx(),+y_location-elementinfocus.gety());
                break;
            }
            else {
                elementinfocus=null;
            }
        }
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
    private void startgame(){
        playerX=.5;
        playerY=.5;
    }
    private void paintFullScreen() {
        if (start.wasclicked()){
            screen=1;
            startgame();
        }
        if(screen==0){
            new drawablestring(width/10,height/20,width/10*8,height/10*2,"Asteroids").draw(g);
            System.arraycopy(stars, 0, stars, 1, 39);
            int x=20+rand.nextInt(80);
            stars[0] =new int[]{rand.nextInt(width),rand.nextInt(height),x,0,x/2};
            for(int[] star: stars){
                if (star[2] > 0) {
                    g.fillOval(star[0], star[1], (int)(star[3]*Math.sin(star[3]/100.0))/2,(int)(star[3]*Math.sin(star[3]/100.0))/2);
                    star[2]--;
                    star[3]++;
                }
            }
            start.updateposition(width/10*4,height/10*7,width/10*2,height/10);
            start.draw(g);
            //new drawablestring(width/10*4,height/10*7,width/10*2,height/10,"Start",true).draw(g);
        }
        if (screen==1){
            if(key.get(KeyEvent.VK_A));
        }
        //for(int i=0;i++<10;){
        //    g.fillOval(rand.nextInt(width),rand.nextInt(height),rand.nextInt(10),rand.nextInt(10));
        //}

    }
}
