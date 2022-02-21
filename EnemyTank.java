package war_of_tanks.Tank;

import war_of_tanks.game.GameFrame;
import war_of_tanks.game.LevelInfo;
import war_of_tanks.util.EnemyTanksPool;
import war_of_tanks.util.MyUtil;
import war_of_tanks.util.constant;

import java.awt.*;

public class EnemyTank extends Tank{
    public static final int TYPE_GREEN = 0 ;
    public static final int TYPE_YELLOW = 1;
    private int type = TYPE_GREEN;
    //记录五秒开始的时间
    private long aiTime;
    //坦克的图片数组
    private static Image[] greenImg;
    private static Image[] yellowImg;
    //静态代码块中对它进行初始化
    static {
        greenImg = new Image[4];
        greenImg[0]=MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/ul.png");
        greenImg[1]=MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/dl.png");
        greenImg[2]=MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/ll.png");
        greenImg[3]=MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/rl.png");

        yellowImg = new Image[4];
        yellowImg[0]=MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/ul1.png");
        yellowImg[1]=MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/dl1.png");
        yellowImg[2]=MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/ll1.png");
        yellowImg[3]=MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/rl1.png");
    }
    private EnemyTank(int x, int y, int dir) {
        super(x, y, dir);
        //敌人一旦创建就计时
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0,2);
    }

    public EnemyTank()
    {
        type = MyUtil.getRandomNumber(0,2);
        aiTime = System.currentTimeMillis();
    }
    //用于创建一个敌人的坦克
    public static Tank createEnemy()
    {
        int x =MyUtil.getRandomNumber(0,2)==0 ? RADIUS: constant.Frame_wide-RADIUS;
        int y = GameFrame.titleBarH + RADIUS;
        int dir = DIR_DOWN;
        EnemyTank enemy = (EnemyTank)EnemyTanksPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setEnemy(true);
        enemy.setState(STATE_MOVE);
        //根据游戏的难度设置敌人的血量
        int maxHp = (Tank.DEFAULT_HP*LevelInfo.getInstance().getLevelType());
        enemy.setHp(maxHp);
        enemy.setMaxHP(maxHp);
        //通过关卡信息中的敌人类型来设置当前出生的敌人的类型
        int enemyType = LevelInfo.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);
        return enemy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void drawImgTank(Graphics g) {

        ai();
        g.drawImage(type==TYPE_GREEN ? greenImg[getDir()] :
                yellowImg[getDir()], getX()-RADIUS,getY()-RADIUS,null);
    }

    private void ai()
    {
        if (System.currentTimeMillis() - aiTime > constant.ENEMY_AI_INTERVAL)
        {
            //间隔五秒，随机切换状态
            setDir(MyUtil.getRandomNumber(DIR_UP,DIR_RIGHT+1));
            setState(MyUtil.getRandomNumber(0,2) == 0?STATE_STAND:STATE_MOVE);
            aiTime = System.currentTimeMillis();
        }
        if (Math.random()<constant.ENEMY_FIRE_PERCENT)
        {
            fire();
        }
    }
}
