package war_of_tanks.game;

import war_of_tanks.util.MyUtil;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: hasee
 * Date: 2021-3-7
 * Time: 22:04
 * Description: No Description  用来控制爆炸效果
 */
public class Explode {
    public static final int EXPLODE_FRAME_COUNT = 12;
    //导入资源
    private static Image[] img;
    //爆炸效果图片的宽度和高度
    private static int explodeWidth;
    private static int explodeHeight;
    static {
        img = new Image[EXPLODE_FRAME_COUNT/3];
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.createImage("img/boom_"+i+".png");
        }
        int explodeWidth = img[0].getWidth(null);
        int explodeHeight = img[0].getHeight(null);
    }
    //爆炸效果的属性
    private int x,y;
    //当前播放帧的下标0-11
    private int index;
    //
    private boolean visible = true;

    public Explode() {
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        index = 0;
    }
    public void draw(Graphics g)
    {
        //对爆炸效果图片的宽高的确定
        if (explodeHeight <= 0)
        {
            explodeHeight = img[0].getHeight(null);
            explodeWidth = img[0].getWidth(null)>>1;
        }
        if (!visible)return;
        g.drawImage(img[index/3],x-explodeHeight,y-explodeHeight,null);
        index++;
        //播放完最后一帧，设置为不可见
        if (index >= EXPLODE_FRAME_COUNT) visible=false;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Explode{" +
                "x=" + x +
                ", y=" + y +
                ", index=" + index +
                ", visible=" + visible +
                '}';
    }
}
