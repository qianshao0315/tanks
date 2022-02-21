package war_of_tanks.map;

/**
 * Write class comments here User: hasee
 * Date: 2021-3-21 20:02
 * version hasee: Map.java, v 0.1 Exp $
 */

import war_of_tanks.Tank.Tank;
import war_of_tanks.game.GameFrame;
import war_of_tanks.game.LevelInfo;
import war_of_tanks.game.LevelInfo;
import war_of_tanks.util.MapTilePool;
import war_of_tanks.util.MyUtil;
import war_of_tanks.util.constant;

import java.awt.*;
import java.io.FileInputStream;
import java.util.*;
import java.util.List;

/**
 * 游戏地图类
 */
public class GameMap {
    //坐标
    private static final int MAP_X = Tank.RADIUS*3;
    private static final int MAP_Y = Tank.RADIUS*3 + GameFrame.titleBarH;
    private static final int MAP_WIDTH = constant.Frame_wide - Tank.RADIUS*6;
    private static final int MAP_HEIGHT = constant.Frame_height - Tank.RADIUS*8 - GameFrame.titleBarH;
    private int height;
    //地图元素块的容器
    private List<MapTile> tiles = new ArrayList<>();

    //大本营
    private TankHouse house;

    public GameMap(){}

    /**
     * 初始化地图元素块
     * level 关卡
     */
    public void initMap(int level){
        tiles.clear();
        try {
            loadLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化大本营
        house = new TankHouse();
        addHouse();
    }

    /**
     * 加载关卡信息
     * @param level
     */
    private void loadLevel(int level) throws Exception{
        //获得关卡信息类的唯一实例对象
        LevelInfo levelInfo = LevelInfo.getInstance();
        levelInfo.setLevel(level);

        Properties prop = new Properties();
        prop.load(new FileInputStream("level/lv_"+level));
        //将所有的地图信息都加载进来
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        //设置敌人数量
        levelInfo.setEnemyCount(enemyCount);
        //0,1 对敌人类型解析
        String[] enemyType = prop.getProperty("enemyType").split(",");
        //
        int[] type = new int[enemyType.length];
        for (int i = 0; i < type.length; i++) {
            type[i] = Integer.parseInt(enemyType[i]);
        }
        //设置敌人类型
        levelInfo.setEnemyType(type);
        //关卡难度，默认难度为1
        String levelType = prop.getProperty("levelType");
        levelInfo.setLevelType(Integer.parseInt(levelType==null?"1":levelType));

        String methodName = prop.getProperty("method");
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));
        //把实参都读取到数组中来
        String[] params = new String[invokeCount];
        for (int i = 1; i <= invokeCount; i++) {
            params[i-1] = prop.getProperty("param"+i);
        }
        //使用读取到的参数，调用对应的方法
        //TODO
        invokeMethod(methodName,params);
    }
    //根据方法的名字和参数调用对应的方法
    private void invokeMethod(String name, String[] params) {
        for (String param : params) {
            //获得每一行的方法的参数，解析
            String[] split = param.split(",");
            //使用一个int数组保存解析后的内容
            int[] arr = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }
            //块之间的间隔是地图的倍数
            final int DIS = MapTile.tileW;
            switch (name){
                case "addRow":
                    addRow(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,MAP_X+MAP_WIDTH-arr[2]*DIS,arr[3],DIS*arr[4]);
                    break;
                case "addCol":
                    addCol(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,MAP_Y+MAP_HEIGHT-arr[2]*DIS,arr[3],DIS);
                    break;
                case "addRect":
                    addRect(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,MAP_X+MAP_WIDTH-arr[2]*DIS,MAP_Y+MAP_HEIGHT-arr[3]*DIS,arr[4],DIS);
                    break;
            }
        }
    }

    //将老巢的所有的元素块添加到地图的容器中
    private void addHouse(){
        tiles.addAll(house.getTiles());
    }

    /**
     * 某一个点确定的地图块是否和 tiles集合中所有的块有重叠的
     * @param tiles
     * @param x
     * @param y
     * @return 有重叠饭盒truw，否则false
     */
    private boolean isCollide(List<MapTile>tiles,int x,int y)
    {
        for (MapTile tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            if (Math.abs(tileX-x) < MapTile.tileW && Math.abs(tileY-y)<MapTile.tileW)
                return true;
        }
        return false;
    }

    /**
     * 只对没有遮挡效果的块进行绘制
     * @param g
     */
    public void drawBk(Graphics g)
    {
        for (MapTile tile : tiles) {
            if (tile.getType() != MapTile.TYPE_COVER) tile.draw(g);
        }
    }

    /**
     * 只绘制有遮挡效果的块
     * @param g
     */
    public void drawCover(Graphics g)
    {
        for (MapTile tile : tiles) {
            if (tile.getType() == MapTile.TYPE_COVER) tile.draw(g);
        }
    }
    public List<MapTile> getTiles() {
        return tiles;
    }

    /**
     * 将所有的不可见的地图块从容器中移除
     */
    public void clearDestoryTile(){
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (!tile.isVisible()){
                tiles.remove(i);
            }
        }
    }

    /**
     * 往地图块容器中添加一行指定类型地图块
     * @param startX 地图块的起始x坐标
     * @param startY y坐标
     * @param endX 地图块的结束x坐标
     * @param type 地图块的类型
     * @param DIS 地图块的间隔，如果间隔是块的宽度，意味着是连续的。大于宽度，意味着是不连续的
     */
    public void addRow(int startX,int startY,int endX,int type,final int DIS){
        int count = (endX - startX)/(MapTile.tileW+DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX + i * (MapTile.tileW+DIS));
            tile.setY(startY);
            tiles.add(tile);
        }
    }

    /**
     * 往地图块容器中添加一列指定类型地图块
     * @param startX 该列的X起始X坐标
     * @param startY 该列的Y起始Y坐标
     * @param endY 改了的结束y坐标
     * @param type 元素类型
     * @param DIS 相邻元素中心点的间距
     */
    public void addCol(int startX,int startY,int endY,int type,final int DIS){
        int count = (endY - startX)/(MapTile.tileW+DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX);
            tile.setY(startY + i * (MapTile.tileW+DIS));
            tiles.add(tile);
        }
    }

    /**
     * 对指定的矩形区域添加元素块
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param type
     * @param DIS
     */
    public void addRect(int startX,int startY,int endX,int endY,int type,final int DIS){
        int rows = (endY - startY)/(MapTile.tileW + DIS);
        for (int i = 0; i < rows; i++) {
            addRow(startX,startY+i*(MapTile.tileW + DIS),endX,type,DIS);
        }
        int cols = (endX - startX)/(MapTile.tileW + DIS);
    }
}
