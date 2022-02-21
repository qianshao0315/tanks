package war_of_tanks.util;

import java.awt.*;

/*
* 工具类
* */
public class MyUtil {
    private MyUtil()
    {

    }
    //得到指定区间的随机数

    /**
     * 得到指定区间的随机数
     * @param min 区间最小值 包含
     * @param max 区间最大值 不会包含
     * @return 随机数
     */
    public static final int getRandomNumber(int min, int max)
    {
        return (int)(Math.random()*(max-min)+min);
    }

    /**
     * 得到随机颜色
     * @return RGB
     */
    public  static final Color getRandomColor()
    {
        int red = getRandomNumber(0,256);
        int blue = getRandomNumber(0,256);
        int green = getRandomNumber(0,256);
        return new Color(red,blue,green);

    }

    /**
     * 判断某一个点是否在某一个正方形的内部
     * @param rectX 正方形中心点的x坐标
     * @param rectY
     * @param radius 正方形边长的一半
     * @param pointX 点的X坐标
     * @param pointY 点的Y坐标
     * @return 点在正方形内部返回true，否则false
     */
    public static final boolean isCollide(int rectX,int rectY,int radius,int pointX,int pointY)
    {
        //正方形中心点和点的XY轴的距离
        int disX = Math.abs(rectX - pointX);
        int dISY = Math.abs(rectY - pointY);
        if(disX<radius && dISY<radius)
            return true;
        return false;

    }

    /**
     * 根据图片资源的路径加载图片对象
     * @param path 图片的路径
     * @return
     */
    public static final Image createImage(String path)
    {
        return Toolkit.getDefaultToolkit().createImage(path);
    }

    private static final String[] NAMES = {
            "温蒂","巴巴托斯","迪卢克","卢姥爷","琴团长","可莉","火花骑士","凯亚","丽莎","迪奥娜",
            "芭芭拉","奥兹","雷泽","安博","萤火虫","虎式","豹式","T80","T34-85","T34-76"
    };
    private static final String[] MODIFIY = {
            "可爱","凶猛","傻傻","羞羞","呆呆","聪明","狡猾","懂事","乖巧","淘气",
            "顽劣","调皮","天真","无邪","好奇","懵逼","闲逛","单纯","萌萌","无畏"

    };
    public static final String getRandomName(){
        return MODIFIY[getRandomNumber(0,MODIFIY.length)] + "的" + NAMES[getRandomNumber(0,NAMES.length)];
    }
}
