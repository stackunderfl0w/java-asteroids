import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



public class Screen extends JPanel {
    private Graphics g;
    private int scale = 1;
    private int width = 900 * scale;
    private int height = 900 * scale;
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
    private static roundeddrawablebutton start;
    private static ArrayList<uielement> buttons=new ArrayList<>();
    uielement elementinfocus;
    private int x_location,y_location;
    private ArrayList<missile>missiles=new ArrayList<>();
    private ArrayList<asteroid>asteroids=new ArrayList<>();
    private int cooldown;
    int starting_asteroids=3;
    private int score;
    private int highscore;
    Font customFont;

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
        start=new roundeddrawablebutton(width/10*4,height/10*7,width/10*2,height/10,"Start",true);
        buttons.add(start);
        key.put(KeyEvent.VK_A,false);
        key.put(KeyEvent.VK_W,false);
        key.put(KeyEvent.VK_S,false);
        key.put(KeyEvent.VK_D,false);
        key.put(KeyEvent.VK_SPACE,false);
        for( int i =0;i++<3;) {
            asteroids.add(new asteroid(rand.nextDouble(), rand.nextDouble(), rand.nextDouble() * Math.PI, 3));
            asteroids.add(new asteroid(rand.nextDouble(), rand.nextDouble(), rand.nextDouble() * Math.PI, 2));
            asteroids.add(new asteroid(rand.nextDouble(), rand.nextDouble(), rand.nextDouble() * Math.PI, 1));
            asteroids.add(new asteroid(rand.nextDouble(), rand.nextDouble(), rand.nextDouble() * Math.PI, 1));
        }
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\fonts\\PressStart2P.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        asteroids=new ArrayList<>();
        playerX=.5;
        playerY=.5;
        playerXVelocity=0;
        playerYVelocity=0;
        playerOrientaion=0;
        score=0;
        for(int i=0; i<starting_asteroids; i++) {
            asteroids.add(new asteroid(playerX + 100.0/width*Math.sin(2*Math.PI/starting_asteroids*i), playerY+ 100.0/height*Math.cos(2*Math.PI/starting_asteroids*i),2*Math.PI/starting_asteroids*i-.5+Math.random(),3));
        }
        buttons.remove(start);
    }
    private void paintFullScreen() {
        width=getWidth();
        height=getHeight();
        if (start.wasclicked()){
            screen=1;
            asteroids.clear();
            startgame();
        }
        g.setColor(Color.white);
        new drawablestring(width/10,0,width/6,height/10,Integer.toString(score),customFont,height/20).draw(g);

        new drawablestring(width-width/4,0,width/6,height/10,Integer.toString(highscore),customFont,height/20).draw(g);
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
        if(screen==0){
            new drawablestring(width/10,height/20,width/10*8,height/10*2,"Asteroids",customFont,256).draw(g);

            start.updateposition(width/10*5-(int)(width/10*((Math.sin(frames/20.0)/2)+2)/2),height/10*7-(int)(((Math.cos(frames/30.0)/2)+2)/2),(int)((width/10*2)*((Math.sin(frames/20.0)/2)+2)/2),(int)((height/10)*((Math.cos(frames/30.0)/2)+2)/2));
            start.draw(g);
            //new drawablestring(width/10*4,height/10*7,width/10*2,height/10,"Start",true).draw(g);
            for (int i=0;i<asteroids.size();i++){
                asteroids.get(i).draw(g,width,height);
            }
            new drawablestring(width/10*4,height/40*39,width/5,height/40,"© 2019 Stackunderfl0w",customFont,48).draw(g);
            if(score!=0){
                new drawablestring(width/3,height/3,width/3,height/3,Integer.toString(score),customFont,80).draw(g);
            }
        }
        if (screen==1){
            g.setColor(Color.white);
            if(asteroids.size()==0){
                startgame();
                starting_asteroids++;
            }
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
            }
            ArrayList<asteroid> addasteroids=new ArrayList<>();
            for (int i=asteroids.size();i-->0;){
                if(!Main.debug) {
                    if (distance(playerX * width, playerY * height, asteroids.get(i).asteroidX * width, asteroids.get(i).asteroidY * height) < 20 * asteroids.get(i).size) {
                        //System.exit(0);
                        if (score>highscore){
                            highscore=score;
                        }
                        screen=0;
                        buttons.add(start);
                        missiles.clear();
                    }
                }
                for (int o=missiles.size();o-->0;){
                    if(i>=0) {
                        if (distance(missiles.get(o).missileX * width, missiles.get(o).missileY * height, asteroids.get(i).asteroidX * width, asteroids.get(i).asteroidY * height) < 20*asteroids.get(i).size) {
                            if(asteroids.get(i).size>1){
                                for(int p=0; p<starting_asteroids; p++) {
                                    addasteroids.add(new asteroid(asteroids.get(i).asteroidX, asteroids.get(i).asteroidY,2*Math.PI* rand.nextDouble() ,asteroids.get(i).size-1));
                                }
                            }
                            score+=10*asteroids.get(i).size;
                            asteroids.remove(i);
                            missiles.remove(o);
                            i--;
                            o--;
                        }
                    }
                }
            }
            asteroids.addAll(addasteroids);
            if(key.get(KeyEvent.VK_W)) {
                g.drawLine((int) ((width * playerX) + 9.0 * Math.sin(-2.55 + playerOrientaion)), (int) ((height * playerY) + 9.0 * Math.cos(-2.55 + playerOrientaion)), (int) ((width * playerX) + 15.0 * Math.sin(3.12 + playerOrientaion)), (int) ((height * playerY) + 15.0 * Math.cos(3.12 + playerOrientaion)));
                g.drawLine((int) ((width * playerX) + 15.0 * Math.sin(3.14 + playerOrientaion)), (int) ((height * playerY) + 15.0 * Math.cos(3.14 + playerOrientaion)), (int) ((width * playerX) + 9.0 * Math.sin(2.55 + playerOrientaion)), (int) ((height * playerY) + 9.0 * Math.cos(2.55 + playerOrientaion)));
            }
            if(Main.debug){
                g.drawLine((int)((width*playerX)+15*Math.sin(playerOrientaion)),(int)((height*playerY)+15*Math.cos(playerOrientaion)),(int)((width*playerX)+50*Math.sin(playerOrientaion)),(int)((height*playerY)+50*Math.cos(playerOrientaion)));
                g.drawRect((int)(width*playerX-10),(int)(height*playerY-10),20,20);
                new drawablestring(0,0,100,50,Integer.toString((int)(playerX*width)),true).draw(g);
                new drawablestring(0,50,100,50,Integer.toString((int)(playerY*height)),true).draw(g);
                new drawablestring(0,100,100,50,Double.toString(round(playerXVelocity*100,2)),true).draw(g);
                new drawablestring(0,150,100,50,Double.toString(round(playerYVelocity*100,2)),true).draw(g);
                new drawablestring(0,200,100,50,Double.toString(round(playerOrientaion,2)),true).draw(g);
                new drawablestring(0,0,50,50,Integer.toString(asteroids.size()),Color.BLUE).draw(g);
                for(asteroid asteroid:asteroids){
                    new drawablestring((int)(width*asteroid.asteroidX)+10*asteroid.size,(int)(height*asteroid.asteroidY)-10*asteroid.size-40,50,20,Double.toString(round(width*asteroid.asteroidX,0)),true).draw(g);
                    new drawablestring((int)(width*asteroid.asteroidX)+10*asteroid.size,(int)(height*asteroid.asteroidY)-10*asteroid.size-20,50,20,Double.toString(round(height*asteroid.asteroidY,0)),true).draw(g);
                }
            }
        }
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
    void drawArr(Graphics g,double[][] arr){
        for(int i=0;i<arr.length-1;i++){
            g.drawLine((int)((width*playerX)+arr[i][0]*Math.sin(arr[i][1]+playerOrientaion)),
                    (int)((height*playerY)+ arr[i][0]*Math.cos(arr[i][1]+playerOrientaion)),
                    (int)((width*playerX)+ arr[i+1][0]*Math.sin(arr[i+1][1]+playerOrientaion)),
                    (int)((height*playerY)+ arr[i+1][0]*Math.cos(arr[i+1][1]+playerOrientaion)));
        }
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
        g.drawLine((int)((w*missileX)+8*Math.sin(missileOrientaion)),(int)((h*missileY)+10*Math.cos(missileOrientaion)),(int)((w*missileX)+5*Math.sin(missileOrientaion+2.5)),(int)((h*missileY)+5*Math.cos(missileOrientaion+2.5)));
        g.drawLine((int)((w*missileX)+8*Math.sin(missileOrientaion)),(int)((h*missileY)+10*Math.cos(missileOrientaion)),(int)((w*missileX)+5*Math.sin(missileOrientaion-2.5)),(int)((h*missileY)+5*Math.cos(missileOrientaion-2.5)));
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
    int style;
    Random rand=new Random();
    asteroid(double x, double y, double direction,int s){

        asteroidX=x;
        asteroidY=y;
        asteroidXVelocity=Math.sin(direction)/500;
        asteroidYVelocity=Math.cos(direction)/500;
        asteroidOrientaion=direction;
        size=s;
        ofset=rand.nextDouble()*2*Math.PI;
        //style=rand.nextInt(4);
        style=(int)(Math.random()*4);
    }
    void draw(Graphics g,int w, int h){
        asteroidX=(asteroidX+asteroidXVelocity+1)%1;
        asteroidY=(asteroidY+asteroidYVelocity+1)%1;
        //draw_circular(g,(int)(w*asteroidX),(int)(h*asteroidY),15*size,6+size);
        if (Main.debug){
            g.drawRect((int)(w*asteroidX)-10*size,(int)(h*asteroidY)-10*size,20*size,20*size);
            g.drawLine((int)((w*asteroidX)+15*Math.sin(asteroidOrientaion)),(int)((h*asteroidY)+15*Math.cos(asteroidOrientaion)),(int)((w*asteroidX)+50*Math.sin(asteroidOrientaion)),(int)((h*asteroidY)+50*Math.cos(asteroidOrientaion)));
        }
        switch (style){
            case 0:drawArr(g,w,h,new double[][]{{7.0, 2.82}, {12.0, 2.75}, {11.0, 2.04}, {11.0, 1.07}, {10.0, 0.85}, {2.0, 0.49}, {12.0, 0.11}, {13.0, -0.49}, {9.0, -0.68}, {12.0, -1.45}, {12.0, -2.31}, {13.0, -2.96}, {7.0, 2.87}});
                break;
            case 1:drawArr(g,w,h,new double[][]{{9.0, 3.02}, {13.0, 2.59}, {12.0, 1.97}, {6.0, 1.98}, {11.0, 1.26}, {12.0, 0.33}, {8.0, -0.44}, {12.0, -0.57}, {11.0, -1.26}, {8.0, -1.75}, {13.0, -2.24}, {13.0, -2.86}, {9.0, 3.02}});
                break;
            case 2:drawArr(g,w,h,new double[][]{{4.0, 2.53}, {12.0, 2.18}, {11.0, 1.32}, {6.0, 1.08}, {11.0, 0.81}, {11.0, -0.19}, {11.0, -0.92}, {10.0, -1.9}, {11.0, -2.41}, {12.0, 3.07}, {11.0, 2.65}, {4.0, 2.62}});
                break;
            case 3:drawArr(g,w,h,new double[][]{{11.0, -2.85}, {11.0, 2.58}, {11.0, 1.94}, {11.0, 0.92}, {11.0, 0.29}, {11.0, -0.61}, {11.0, -1.26}, {11.0, -2.23}, {11.0, -2.87}});
                break;
        }
    }
    void draw_circular(Graphics g,int x,int y,int d, int s){
        for (int i=0;i<s;i++){
            g.drawLine((int)((x)+d*Math.sin((2*Math.PI/s*i)+ofset)),(int)((y)+d*Math.cos((2*Math.PI/s*i)+ofset)),(int)((x)+d*Math.sin((2*Math.PI/s*(i+1)+ofset))),(int)((y)+d*Math.cos((2*Math.PI/s*(i+1)+ofset))));
        }
    }
    void drawArr(Graphics g,int w,int h,double[][] arr){
        for(int i=0;i<arr.length-1;i++){
            g.drawLine((int)((w*asteroidX)+arr[i][0]*size*Math.sin(arr[i][1]+ofset)),
                    (int)((h*asteroidY)+ arr[i][0]*size*Math.cos(arr[i][1]+ofset)),
                    (int)((w*asteroidX)+ arr[i+1][0]*size*Math.sin(arr[i+1][1]+ofset)),
                    (int)((h*asteroidY)+ arr[i+1][0]*size*Math.cos(arr[i+1][1]+ofset)));
        }
    }
}