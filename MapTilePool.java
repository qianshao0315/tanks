package war_of_tanks.util;

import war_of_tanks.Tank.EnemyTank;
import war_of_tanks.Tank.Tank;
import war_of_tanks.map.MapTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Write class comments here User: hasee
 * Date: 2021-3-21 20:11
 * version hasee: MapTilePool.java, v 0.1 Exp $
 */
public class MapTilePool {
    public static final int DEFAULT_POOL_SIZE =50;
    public static final int POOL_MAX_SIZE =70;

    private static List<MapTile> pool = new ArrayList<>();
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new MapTile());
        }
    }

    /**
     * 从池塘中获取一个坦克对象
     * @return
     */
    public static MapTile get(){
        MapTile tile = null;
        if (pool.size()==0){   //池塘被掏空
            tile = new MapTile();
        }
        else{                   //池塘中还有对象，拿走第一个位置的对象
            tile = pool.remove(0);
        }
        return tile;
    }

    public static <T> void theReturn(MapTile tile)
    {
        if (pool.size() == POOL_MAX_SIZE)
        {
            return;
        }
        pool.add(tile);
    }
}
