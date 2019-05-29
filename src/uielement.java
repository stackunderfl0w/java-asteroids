import javax.swing.*;

abstract class uielement extends JPanel {
    abstract void updateposition(int x, int y, int w,int h);
    abstract int getwidth();
    abstract int getheight();
    abstract int gety();
    abstract int getx();
    abstract void clicked(int x, int y);
}
