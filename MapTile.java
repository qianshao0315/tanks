package war_of_tanks.map;

/**
 * Write class comments here User: hasee
 * Date: 2021-3-21 19:51
 * version hasee: MapTile.java, v 0.1 Exp $
 */

import war_of_tanks.game.Bullet;
import war_of_tanks.util.BulletsPool;
import war_of_tanks.util.MyUtil;
import war_of_tanks.util.constant;

import java.awt.*;
import java.util.List;

/**
 * 地图元素块
 */
public class MapTile {

    private static Image[] tileImg;
    public static int tileW = 40;
    public static int radius = tileW >> 1;
    private int type = TYPE_NORMAL;

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOUSE = 1;
    public static final int TYPE_COVER = 2;
    public static final int TYPE_HARD = 3;

    private String name;
    static {
        tileImg = new Image[4];
        tileImg[TYPE_NORMAL] = MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/tile.png");
        tileImg[TYPE_HOUSE] = MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/house.png");
        tileImg[TYPE_COVER] = MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/cover.png");
        tileImg[TYPE_HARD] = MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/hard.png");

        if (tileW <= 0){
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
    }
    //图片资源的左上角的坐标
    private int x,y;
    private boolean visible = true;
    public MapTile() {
    }

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
        if (tileW <= 0){
            tileW =tileImg[TYPE_NORMAL].getWidth(null);
        }
    }

    public void draw(Graphics g)
    {
        if (!visible)
            return;
        if (tileW <= 0){
            tileW =tileImg[TYPE_NORMAL].getWidth(null);
        }
        g.drawImage(tileImg[type],x,y,null);

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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 地图块和若干子弹是否有碰撞
     * @param bullets
     * @return
     */
    public boolean isCollideBullet(List<Bullet> bullets){
        if (!visible || type ==TYPE_COVER)
            return false;
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            boolean collide = MyUtil.isCollide(x + radius, y + radius, radius, bulletX, bulletY);
            if (collide){
                //子弹的销毁 TODO
                bullet.setVisible(false);
                BulletsPool.theReturn(bullet);
                return true;
            }
        }
        return false;
    }

    //判断当前的地图块是否是老巢
    public boolean isHouse(){
        return type == TYPE_HOUSE;
    }
}
