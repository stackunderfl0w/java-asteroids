import java.awt.*;
import java.awt.event.KeyEvent;

public class roundeddrawablebutton extends uielement{
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
    roundeddrawablebutton(int x, int y, int w, int h, int xoff, int yoff, String s, Boolean bounds){
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
    roundeddrawablebutton(int x, int y, int w, int h, String s, Boolean bounds){
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
        x_location=x;
        y_location=y;
        width=w;
        height=h;
    }
    int getwidth() {
        return width;
    }

    int getheight() {
        return height;
    }

    int gety() {
        return y_location;
    }

    int getx() {
        return x_location;
    }
}
