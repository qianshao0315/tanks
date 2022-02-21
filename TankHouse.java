package war_of_tanks.map;

import war_of_tanks.util.constant;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Write class comments here User: hasee
 * Date: 2021-3-31 14:33
 * version hasee: TankHouse.java, v 0.1 Exp $
 */
public class TankHouse {
    //老巢的xy坐标
    public static final int HOUSE_X = constant.Frame_wide-3*MapTile.tileW;
    public static final int HOUSE_Y = constant.Frame_height-2*MapTile.tileW;

    private List<MapTile> tiles = new ArrayList<>();
    public TankHouse() {
        tiles.add(new MapTile(HOUSE_X-MapTile.tileW*9,HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X-MapTile.tileW*9,HOUSE_Y+MapTile.tileW));
        tiles.add(new MapTile(HOUSE_X-MapTile.tileW*8,HOUSE_Y));

        tiles.add(new MapTile(HOUSE_X-MapTile.tileW*7,HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X-MapTile.tileW*7,HOUSE_Y+MapTile.tileW));
        //大本营的块
        tiles.add(new MapTile(HOUSE_X-MapTile.tileW*8,HOUSE_Y+MapTile.tileW));
        //设置老巢地图块的类型
        tiles.get(tiles.size()-1).setType(MapTile.TYPE_HOUSE);
    }

    public void draw(Graphics g)
    {
        for (MapTile tile : tiles) {
            tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }
}
