package war_of_tanks.util;

import war_of_tanks.game.Bullet;
import war_of_tanks.game.Explode;

import java.util.ArrayList;
import java.util.List;

/**
 * Write class comments here User: hasee
 * Date: 2021-3-14 16:34
 * version hasee: ExplodesPool.java, v 0.1 Exp $
 */

/**
 * 爆炸效果对象池
 */
public class ExplodesPool {
    public static final int DEFAULT_POOL_SIZE =20;
    public static final int POOL_MAX_SIZE =20;

    //用于保存所有b爆炸效果的容器
    private static List<Explode> pool = new ArrayList<>();
    //在类加载的时候，创建200个子弹对象添加到容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Explode());
        }
    }

    /**
     * 从池塘中获取一个爆炸对象
     * @return
     */
    public static Explode get(){
        Explode explode = null;
        if (pool.size()==0){   //池塘被掏空
            explode = new Explode();
        }
        else{                   //池塘中还有对象，拿走第一个位置的对象
            explode = pool.remove(0);
        }
        //System.out.println("对象池中剩余对象："+pool.size() );
        return explode;
    }
    //子弹被销毁的时候，归还到池塘中来
    public static void theReturn(Explode explode)
    {
        //池塘中子弹的个数到达最大值，就不在归还
        if (pool.size() == POOL_MAX_SIZE)
        {
            return;
        }
        pool.add(explode);
        //System.out.println("对象池中归还一个子弹对象，当前数量为："+pool.size() );
    }
}
