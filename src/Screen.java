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
    double playerXVelocity;
    double playerYVelocity;
    double playerOrientaion;
    int frames;
    public HashMap<Integer, Boolean> key=new HashMap<>();
    Random rand = new Random();
    private int[][] stars=new int[120][];
    private static drawablebutton start;
    private static ArrayList<uielement> buttons=new ArrayList<>();
    uielement elementinfocus;
    private int x_location,y_location;
    private boolean debug;

    Screen(){
        Main.f.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                key.put(e.getKeyCode(),true);
                if(e.getKeyCode()==KeyEvent.VK_F){System.exit(0);}
                if(e.getKeyCode()==KeyEvent.VK_ALT){debug=!debug;}
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
        buttons.add(start);
        key.put(KeyEvent.VK_A,false);
        key.put(KeyEvent.VK_W,false);
        key.put(KeyEvent.VK_S,false);
        key.put(KeyEvent.VK_D,false);
        key.put(KeyEvent.VK_SPACE,false);


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
        frames++;
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
            start.updateposition(width/10*5-(int)(width/10*((Math.sin(frames/20.0)/2)+2)/2),height/10*7-(int)(((Math.cos(frames/30.0)/2)+2)/2),(int)((width/10*2)*((Math.sin(frames/20.0)/2)+2)/2),(int)((height/10)*((Math.cos(frames/30.0)/2)+2)/2));
            start.draw(g);
            //new drawablestring(width/10*4,height/10*7,width/10*2,height/10,"Start",true).draw(g);
        }
        if (screen==1){
            if(key.get(KeyEvent.VK_W)){
                playerXVelocity+=Math.sin(playerOrientaion)/3000;
                playerYVelocity+=Math.cos(playerOrientaion)/3000;
            }
            if(key.get(KeyEvent.VK_S)){
                playerXVelocity*=.97;
                playerYVelocity*=.97;
            }if(key.get(KeyEvent.VK_A)){
                playerOrientaion=(playerOrientaion+.1)%360;
            }
            if(key.get(KeyEvent.VK_D)){
                playerOrientaion=(playerOrientaion-.1)%360;
            }
            playerXVelocity=playerXVelocity*.97;
            playerYVelocity=playerYVelocity*.97;
            playerX=(playerX+playerXVelocity+1)%1;
            playerY=(playerY+playerYVelocity+1)%1;

            g.setColor(Color.white);
            //g.drawLine((int)((width*playerX)+50*Math.sin(playerOrientaion)),(int)((height*playerY)+50*Math.cos(playerOrientaion)),(int)((width*playerX)+15*Math.sin(playerOrientaion+2)),(int)((height*playerY)+15*Math.cos(playerOrientaion+2)));
            //g.drawLine((int)((width*playerX)+50*Math.sin(playerOrientaion)),(int)((height*playerY)+50*Math.cos(playerOrientaion)),(int)((width*playerX)+15*Math.sin(playerOrientaion-2)),(int)((height*playerY)+15*Math.cos(playerOrientaion-2)));
            g.drawLine((int)((width*playerX)+15*Math.sin(playerOrientaion)),(int)((height*playerY)+15*Math.cos(playerOrientaion)),(int)((width*playerX)+15*Math.sin(playerOrientaion+2.5)),(int)((height*playerY)+15*Math.cos(playerOrientaion+2.5)));
            g.drawLine((int)((width*playerX)+15*Math.sin(playerOrientaion)),(int)((height*playerY)+15*Math.cos(playerOrientaion)),(int)((width*playerX)+15*Math.sin(playerOrientaion-2.5)),(int)((height*playerY)+15*Math.cos(playerOrientaion-2.5)));
            g.drawLine((int)((width*playerX)+5*Math.sin(playerOrientaion+Math.PI)),(int)((height*playerY)+5*Math.cos(playerOrientaion+Math.PI)),(int)((width*playerX)+15*Math.sin(playerOrientaion+2.5)),(int)((height*playerY)+15*Math.cos(playerOrientaion+2.5)));
            g.drawLine((int)((width*playerX)+5*Math.sin(playerOrientaion+Math.PI)),(int)((height*playerY)+5*Math.cos(playerOrientaion+Math.PI)),(int)((width*playerX)+15*Math.sin(playerOrientaion-2.5)),(int)((height*playerY)+15*Math.cos(playerOrientaion-2.5)));
            if(debug){
                g.drawLine((int)((width*playerX)+15*Math.sin(playerOrientaion)),(int)((height*playerY)+15*Math.cos(playerOrientaion)),(int)((width*playerX)+50*Math.sin(playerOrientaion)),(int)((height*playerY)+50*Math.cos(playerOrientaion)));
                g.drawRect((int)(width*playerX-10),(int)(height*playerY-10),20,20);
                new drawablestring(0,0,100,50,Double.toString(round(playerX,2)),true).draw(g);
                new drawablestring(0,50,100,50,Double.toString(round(playerY,2)),true).draw(g);
                new drawablestring(0,100,100,50,Double.toString(round(playerXVelocity*100,2)),true).draw(g);
                new drawablestring(0,150,100,50,Double.toString(round(playerYVelocity*100,2)),true).draw(g);
                new drawablestring(0,200,100,50,Double.toString(round(playerOrientaion,2)),true).draw(g);
            }
        }
        //for(int i=0;i++<10;){
        //    g.fillOval(rand.nextInt(width),rand.nextInt(height),rand.nextInt(10),rand.nextInt(10));
        //}

    }
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
class missile{
    double missileX;
    double missileY;
    double missileXVelocity;
    double missileYVelocity;
    double missileOrientaion;
    missile(double x, double y, double direction){
        missileX=x;
        missileY=y;
        missileXVelocity=Math.sin(direction);
        missileYVelocity=Math.cos(direction);
        missileOrientaion=direction;
    }
}