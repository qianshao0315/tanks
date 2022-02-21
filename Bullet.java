package war_of_tanks.game;

import war_of_tanks.Tank.Tank;
import war_of_tanks.util.constant;

import java.awt.*;

/**
 * 子弹类
 */
public class Bullet {
    public static final int DEFAULT_SPEED = Tank.DEFAULT_SPEED << 1;//子弹的速度为坦克的两倍
    public static final int RADIS = 4;//炮弹的半径

    private int x,y;
    private int speed = DEFAULT_SPEED;
    private int dir;
    private int atk;
    private Color color;
    private boolean visible = true;//子弹是否可见

    public Bullet(int x, int y, int dir, int atk, Color color) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.atk = atk;
        this.color = color;
    }


    //给对象池使用，所有的属性都是默认值
    public Bullet() {
    }

    /**
     * 炮弹绘制的自身的方法
     * @param g
     */
    public void draw(Graphics g){
        if (!visible)return;

        logic();
        g.setColor(color);
        g.fillOval(x-RADIS,y-RADIS,RADIS<<1,RADIS<<1);
    }

    /**
     * 子弹的逻辑
     */
    public void logic() {
        move();
    }

    private void move(){
        switch (dir){
            case Tank.DIR_UP:
                y -= speed;
                if (y <= 0) visible=false;
                break;
            case Tank.DIR_DOWN:
                y += speed;
                if (y > constant.Frame_height) visible=false;
                break;
            case Tank.DIR_LEFT:
                x -= speed;
                if (x <= 0) visible=false;
                break;
            case Tank.DIR_RIGHT:
                x += speed;
                if (x > constant.Frame_wide) visible=false;
                break;
        }
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
