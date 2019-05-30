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
    public int frames;
    public HashMap<Integer, Boolean> key=new HashMap<>();
    Random rand = new Random();
    private int[][] stars=new int[120][];
    private static drawablebutton start;
    private static ArrayList<uielement> buttons=new ArrayList<>();
    uielement elementinfocus;
    private int x_location,y_location;
    private ArrayList<missile>missiles=new ArrayList<>();
    private ArrayList<asteroid>asteroids=new ArrayList<>();
    private int cooldown;
    int starting_asteroids=3;

    Screen(){
        Main.f.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                key.put(e.getKeyCode(),true);
                if(e.getKeyCode()==KeyEvent.VK_F){System.exit(0);}
                if(e.getKeyCode()==KeyEvent.VK_ALT){Main.debug=!Main.debug;}
                if(e.getKeyCode()==KeyEvent.VK_Q){Main.framerate=300;}
                if(e.getKeyCode()==KeyEvent.VK_0){Main.framerate=3000;}
                if(e.getKeyCode()==KeyEvent.VK_9){Main.framerate=30000;}
            }
            public void keyReleased(KeyEvent e) {
                key.put(e.getKeyCode(),false);
                if(e.getKeyCode()==KeyEvent.VK_Q){Main.framerate=60;}
                if(e.getKeyCode()==KeyEvent.VK_0){Main.framerate=60;}
                if(e.getKeyCode()==KeyEvent.VK_9){Main.framerate=60;}

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
        for(int i=0; i<starting_asteroids; i++) {
            asteroids.add(new asteroid(playerX + .1*Math.sin(2*Math.PI/starting_asteroids*i), playerY+ .1*Math.cos(2*Math.PI/starting_asteroids*i),2*Math.PI/starting_asteroids*i,3));
        }
        buttons.remove(start);
    }
    private void paintFullScreen() {
        width=getWidth();
        height=getHeight();
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
            if(cooldown>0){
                cooldown--;
            }
            if(key.get(KeyEvent.VK_W)){
                playerXVelocity+=Math.sin(playerOrientaion)/6000;
                playerYVelocity+=Math.cos(playerOrientaion)/6000;
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
            if(key.get(KeyEvent.VK_SPACE)&&cooldown==0){
                missiles.add(new missile(playerX,playerY,playerOrientaion,300));
                cooldown=30;
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
            for (int i=0;i<missiles.size();i++){
                missiles.get(i).draw(g,width,height);
                if(missiles.get(i).getTime()<1){
                    missiles.remove(i);
                    i--;
                }
            }
            for (int i=0;i<asteroids.size();i++){
                asteroids.get(i).draw(g,width,height);
                //if(missiles.get(i).getTime()<1){
                 //   missiles.remove(i);
                //    i--;
                //}
            }
            ArrayList<asteroid> addasteroids=new ArrayList<>();
            for (int i=asteroids.size();i-->0;){
                if (distance(playerX * width, playerY * height, asteroids.get(i).asteroidX * width, asteroids.get(i).asteroidY * height) < 20*asteroids.get(i).size) {
                    System.exit(0);
                }
                for (int o=missiles.size();o-->0;){
                    if(i>=0) {
                        if (distance(missiles.get(o).missileX * width, missiles.get(o).missileY * height, asteroids.get(i).asteroidX * width, asteroids.get(i).asteroidY * height) < 20*asteroids.get(i).size) {
                            if(asteroids.get(i).size>1){
                                for(int p=0; p<starting_asteroids; p++) {
                                    addasteroids.add(new asteroid(asteroids.get(i).asteroidX, asteroids.get(i).asteroidY,2*Math.PI/starting_asteroids*p,asteroids.get(i).size-1));
                                }
                            }
                            asteroids.remove(i);
                            missiles.remove(o);
                            i--;
                            o--;
                        }
                    }
                }
            }
            asteroids.addAll(addasteroids);
            if(Main.debug){
                g.drawLine((int)((width*playerX)+15*Math.sin(playerOrientaion)),(int)((height*playerY)+15*Math.cos(playerOrientaion)),(int)((width*playerX)+50*Math.sin(playerOrientaion)),(int)((height*playerY)+50*Math.cos(playerOrientaion)));
                g.drawRect((int)(width*playerX-10),(int)(height*playerY-10),20,20);
                new drawablestring(0,0,100,50,Double.toString(round(playerX,2)),true).draw(g);
                new drawablestring(0,50,100,50,Double.toString(round(playerY,2)),true).draw(g);
                new drawablestring(0,100,100,50,Double.toString(round(playerXVelocity*100,2)),true).draw(g);
                new drawablestring(0,150,100,50,Double.toString(round(playerYVelocity*100,2)),true).draw(g);
                new drawablestring(0,200,100,50,Double.toString(round(playerOrientaion,2)),true).draw(g);
                new drawablestring(0,0,50,50,Integer.toString(asteroids.size()),Color.BLUE).draw(g);
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
    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
}
class missile{
    double missileX;
    double missileY;
    double missileXVelocity;
    double missileYVelocity;
    double missileOrientaion;
    int time;
    missile(double x, double y, double direction,int durration){
        missileX=x;
        missileY=y;
        missileXVelocity=Math.sin(direction)/180;
        missileYVelocity=Math.cos(direction)/180;
        missileOrientaion=direction;
        time=durration;
    }
    void draw(Graphics g,int w, int h){
        missileX=(missileX+missileXVelocity+1)%1;
        missileY=(missileY+missileYVelocity+1)%1;
        time--;
        g.drawLine((int)((w*missileX)+10*Math.sin(missileOrientaion)),(int)((h*missileY)+10*Math.cos(missileOrientaion)),(int)((w*missileX)+10*Math.sin(missileOrientaion+2.5)),(int)((h*missileY)+10*Math.cos(missileOrientaion+2.5)));
        g.drawLine((int)((w*missileX)+10*Math.sin(missileOrientaion)),(int)((h*missileY)+10*Math.cos(missileOrientaion)),(int)((w*missileX)+10*Math.sin(missileOrientaion-2.5)),(int)((h*missileY)+10*Math.cos(missileOrientaion-2.5)));
        if (Main.debug){
            g.drawRect((int)(w*missileX)-5,(int)(h*missileY)-5,10,10);
            g.drawLine((int)((w*missileX)+15*Math.sin(missileOrientaion)),(int)((h*missileY)+15*Math.cos(missileOrientaion)),(int)((w*missileX)+50*Math.sin(missileOrientaion)),(int)((h*missileY)+50*Math.cos(missileOrientaion)));
        }
    }
    int getTime(){
        return time;
    }
}
class asteroid{
    double asteroidX;
    double asteroidY;
    double asteroidXVelocity;
    double asteroidYVelocity;
    double asteroidOrientaion;
    int size;
    double ofset;
    Random rand=new Random();
    asteroid(double x, double y, double direction,int s){

        asteroidX=x;
        asteroidY=y;
        asteroidXVelocity=Math.sin(direction)/500;
        asteroidYVelocity=Math.cos(direction)/500;
        asteroidOrientaion=direction;
        size=s;
        ofset=rand.nextDouble()*2*Math.PI;
    }
    void draw(Graphics g,int w, int h){
        asteroidX=(asteroidX+asteroidXVelocity+1)%1;
        asteroidY=(asteroidY+asteroidYVelocity+1)%1;
        draw_circular(g,(int)(w*asteroidX),(int)(h*asteroidY),15*size,6+size);
        /*g.drawLine((int)((w*asteroidX)+15*Math.sin(asteroidOrientaion)),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion)),(int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(1*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(1*Math.PI/3))));
        g.drawLine((int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(1*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(1*Math.PI/3))),(int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(2*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(2*Math.PI/3))));
        g.drawLine((int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(2*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(2*Math.PI/3))),(int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(3*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(3*Math.PI/3))));
        g.drawLine((int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(3*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(3*Math.PI/3))),(int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(4*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(4*Math.PI/3))));
        g.drawLine((int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(4*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(4*Math.PI/3))),(int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(5*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(5*Math.PI/3))));
        g.drawLine((int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(5*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(5*Math.PI/3))),(int)((w*asteroidX)+15*Math.sin(asteroidOrientaion+(6*Math.PI/3))),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion+(6*Math.PI/3))));
        */
        if (Main.debug){
            g.drawRect((int)(w*asteroidX)-10*size,(int)(h*asteroidY)-10*size,20*size,20*size);
            g.drawLine((int)((w*asteroidX)+15*Math.sin(asteroidOrientaion)),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion)),(int)((w*asteroidX)+50*Math.sin(asteroidOrientaion)),(int)((h*asteroidY)+50*Math.cos(asteroidOrientaion)));
        }
    }
    int getS(){
        return size;
    }
    void draw_circular(Graphics g,int x,int y,int d, int s){
        for (int i=0;i<s;i++){
            g.drawLine((int)((x)+d*Math.sin((2*Math.PI/s*i)+ofset)),(int)((y)+d*Math.cos((2*Math.PI/s*i)+ofset)),(int)((x)+d*Math.sin((2*Math.PI/s*(i+1)+ofset))),(int)((y)+d*Math.cos((2*Math.PI/s*(i+1)+ofset))));
        }
    }
}