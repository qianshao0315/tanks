package war_of_tanks.util;

import war_of_tanks.game.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * 子弹对象池类
 */
public class BulletsPool {
    public static final int DEFAULT_POOL_SIZE =200;
    public static final int POOL_MAX_SIZE =300;

    //用于保存所有子弹的容器
    private static List<Bullet> pool = new ArrayList<>();
    //在类加载的时候，创建200个子弹对象添加到容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Bullet());
        }
    }

    /**
     * 从池塘中获取一个子弹对象
     * @return
     */
    public static Bullet get(){
        Bullet bullet = null;
        if (pool.size()==0){   //池塘被掏空
            bullet = new Bullet();
        }
        else{                   //池塘中还有对象，拿走第一个位置的对象
            bullet = pool.remove(0);
        }
        //System.out.println("对象池中剩余对象："+pool.size() );
        return bullet;
    }
    //子弹被销毁的时候，归还到池塘中来
    public static void theReturn(Bullet bullet)
    {
        //池塘中子弹的个数到达最大值，就不在归还
        if (pool.size() == POOL_MAX_SIZE)
        {
            return;
        }
        pool.add(bullet);
        //System.out.println("对象池中归还一个子弹对象，当前数量为："+pool.size() );
    }
}
