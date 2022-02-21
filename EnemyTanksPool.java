package war_of_tanks.util;

import war_of_tanks.Tank.EnemyTank;
import war_of_tanks.Tank.Tank;

import java.util.ArrayList;
import java.util.List;

/**
 * Write class comments here User: hasee
 * Date: 2021-3-14 18:23
 * version hasee: TanksPool.java, v 0.1 Exp $
 */

/**
 * 敌人坦克对象池
 */
public class EnemyTanksPool {
    public static final int DEFAULT_POOL_SIZE =20;
    public static final int POOL_MAX_SIZE =20;

    private static List<Tank> pool = new ArrayList<>();
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new EnemyTank());
        }
    }

    /**
     * 从池塘中获取一个坦克对象
     * @return
     */
    public static Tank get(){
        Tank tank = null;
        if (pool.size()==0){   //池塘被掏空
            tank = new EnemyTank();
        }
        else{                   //池塘中还有对象，拿走第一个位置的对象
            tank = pool.remove(0);
        }
        return tank;
    }
    //子弹被销毁的时候，归还到池塘中来
    public static void theReturn(Tank tank)
    {
        //池塘中子弹的个数到达最大值，就不在归还
        if (pool.size() == POOL_MAX_SIZE)
        {
            return;
        }
        pool.add(tank);
    }
}
