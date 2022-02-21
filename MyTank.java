package war_of_tanks.Tank;

import war_of_tanks.util.MyUtil;

import java.awt.*;

public class MyTank extends Tank{

    //坦克的图片数组
    private static Image[] tankImg;
    //静态代码块中对它进行初始化
    static
    {
        tankImg=new Image[4];
        tankImg[0]= MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/u.png");
        tankImg[1]= MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/d.png");
        tankImg[2]= MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/l.png");
        tankImg[3]= MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/r.png");
    }
    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
    }

    @Override
    public void drawImgTank(Graphics g) {
        g.drawImage(tankImg[getDir()],getX()-RADIUS,getY()-RADIUS,null);
    }
}
