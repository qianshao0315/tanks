package war_of_tanks.game;

/**
 * Write class comments here User: hasee
 * Date: 2021-4-14 10:58
 * version hasee: LevelInfo.java, v 0.1 Exp $
 */

import war_of_tanks.util.MyUtil;

/**
 * 用来管理当前关卡的信息：单例类
 * 单例设计模式：如果一个类只需要该类且具有唯一的实例，那么可以使用
 *
 */
public class LevelInfo {
    //构造方法私有化
    private LevelInfo(){}
    //定义静态的本类对象的变量，来指向唯一的实例
    private static LevelInfo instance;
    //懒汉模式的单例 第一次使用该实例的时候，创建唯一的实例
    //所有的访问该类的唯一实例，都是通过该方法
    //该方法具有安全隐患，多线程的情况下可能会创建多个实例
    public static LevelInfo getInstance(){
        if (instance==null){
            //创建了唯一的实例
            instance = new LevelInfo();
        }
        return instance;
    }
    //关卡的编号
    private int level;
    //关卡的敌人数量
    private int enemyCount;
    //通关最长时间，-1意味着不限时的
    private int crossTime = -1;
    //敌人类型信息
    private int[] enemyType;
    //游戏难度 >=1
    private int levelType;
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int getCrossTime() {
        return crossTime;
    }

    public void setCrossTime(int crossTime) {
        this.crossTime = crossTime;
    }

    public int[] getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(int[] enemyType) {
        this.enemyType = enemyType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    public int getLevelType() {
        return levelType<=0 ? 1 :levelType;
    }

    //获得敌人类型数组中的随机的元素
    //获得一个随机的敌人类型
    public int getRandomEnemyType(){
        int index = MyUtil.getRandomNumber(0,enemyType.length);
        return enemyType[index];
    }
}
