import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class drawablestring extends JPanel {
    private int width;
    private int height;
    private int x_location;
    private int y_location;
    private int string_size;
    private String string;
    private int fontsize;
    private Font font=new Font("Arial", Font.BOLD, 96);
    private Color backgroundColor;
    private boolean drawBounds;
    private Color fontColor=Color.black;
    drawablestring(int x, int y,int w, int h, String s,Boolean bounds){
        x_location=x;
        y_location=y;
        width=w;
        height=h;
        string=s;
        fontsize=96;
        drawBounds=bounds;
        int string_height=3000;
        while(string_height>height){
            fontsize--;
            font=new Font("Arial", Font.BOLD, fontsize);
            FontMetrics fm=getFontMetrics(font);
            string_height=fm.getHeight();
        }
        int string_width=3000;
        while(string_width>width-3){
            fontsize--;
            font=new Font("Arial", Font.PLAIN, fontsize);
            FontMetrics fm=getFontMetrics(font);
            string_width=fm.stringWidth(s);
        }
    }
    drawablestring(int x, int y,int w, int h, String s){
        x_location=x;
        y_location=y;
        width=w;
        height=h;
        string=s;
        fontsize=96;
        int string_height=3000;
        while(string_height>height){
            fontsize--;
            font=new Font("Arial", Font.BOLD, fontsize);
            FontMetrics fm=getFontMetrics(font);
            string_height=fm.getHeight();
        }
        int string_width=3000;
        while(string_width>width){
            fontsize--;
            font=new Font("Arial", Font.BOLD, fontsize);
            FontMetrics fm=getFontMetrics(font);
            string_width=fm.stringWidth(s);
        }
    }
    drawablestring(int x, int y,int w, int h, String s,Color fontc,Color background){
        backgroundColor=background;
        x_location=x;
        y_location=y;
        width=w;
        height=h;
        string=s;
        fontsize=96;
        fontColor=fontc;
        int string_height=3000;
        while(string_height>height){
            fontsize--;
            font=new Font("Arial", Font.BOLD, fontsize);
            FontMetrics fm=getFontMetrics(font);
            string_height=fm.getHeight();
        }
        int string_width=3000;
        while(string_width>width){
            fontsize--;
            font=new Font("Arial", Font.BOLD, fontsize);
            FontMetrics fm=getFontMetrics(font);
            string_width=fm.stringWidth(s);
        }
    }
    drawablestring(int x, int y,int w, int h, String s,Color fontc){
        x_location=x;
        y_location=y;
        width=w;
        height=h;
        string=s;
        fontsize=96;
        fontColor=fontc;
        int string_height=3000;
        while(string_height>height){
            fontsize--;
            font=new Font("Arial", Font.ITALIC, fontsize);
            FontMetrics fm=getFontMetrics(font);
            string_height=fm.getHeight();
        }
        int string_width=3000;
        while(string_width>width){
            fontsize--;
            font=new Font("Arial", Font.BOLD, fontsize);
            FontMetrics fm=getFontMetrics(font);
            string_width=fm.stringWidth(s);
        }
    }
    drawablestring(int x, int y,int w, int h, String s,Font f){
        x_location=x;
        y_location=y;
        width=w;
        height=h;
        string=s;
        font=f;
    }
    void draw(Graphics g){
        if (backgroundColor!=null){
            g.setColor(backgroundColor);
            g.fillRect(x_location,y_location,width,height);
        }
        g.setFont(font);
        g.setColor(Color.black);
        if(drawBounds){
            g.drawRect(x_location, y_location, width, height);
        }
        g.setColor(fontColor);
        paint_string(g,string,x_location+width/2,y_location+height/2);
    }
    private void paint_string(Graphics g,String s, int x, int y){
        //create new font with new size
        g.setFont(font);
        final FontMetrics fm = getFontMetrics(font);
        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];
        g.setColor(fontColor);
        g.drawString(s,x-w/2,y+h/2);
        //System.out.println("string" +s+" at " +x+","+y);
    }
}
