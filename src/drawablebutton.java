import java.awt.*;
import java.awt.event.KeyEvent;

public class drawablebutton{
    private int width;
    private int height;
    private int x_location;
    private int y_location;
    private String string;
    private int fontsize;
    private boolean drawBounds;
    private boolean clicked;
    private int xofset;
    private int yofset;
    drawablebutton(int x, int y,int w, int h,int xoff,int yoff, String s,Boolean bounds){
        x_location=x;
        y_location=y;
        width=w;
        height=h;
        string=s;
        fontsize=96;
        drawBounds=bounds;
        xofset=xoff;
        yofset=yoff;
    }
    drawablebutton(int x, int y,int w, int h, String s,Boolean bounds){
        x_location=x;
        y_location=y;
        width=w;
        height=h;
        string=s;
        fontsize=96;
        drawBounds=bounds;
    }
    public boolean wasclicked(){
        boolean temp=clicked;
        clicked=false;
        return temp;
    }
    void draw(Graphics g) {
        new drawablestring(x_location,y_location,width,height,string,drawBounds).draw(g);
    }
    void clicked(int x, int y) {
        clicked=true;
    }
    void updateposition(int x, int y, int w, int h){
        x_location=x+xofset;
        y_location=y+yofset;
        if(xofset<0){
            x_location+=w;
        }
        if(yofset<0){
            y_location+=h;
        }
    }
}
