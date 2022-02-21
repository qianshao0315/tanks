package war_of_tanks.Tank;

import war_of_tanks.game.Bullet;
import war_of_tanks.game.Explode;
import war_of_tanks.game.GameFrame;
import war_of_tanks.map.MapTile;
import war_of_tanks.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

//坦克类
public abstract class Tank {
    //四个方向
    public static final int DIR_UP=0;
    public static final int DIR_DOWN=1;
    public static final int DIR_LEFT=2;
    public static final int DIR_RIGHT=3;
    //半径
    public static final int RADIUS =20;
    //默认速度 每帧
    public static final int DEFAULT_SPEED = 4;
    //坦克的状态
    public static final int STATE_STAND=0;
    public static final int STATE_MOVE=1;
    public static final int STATE_DIE=2;
    //坦克的初始生命
    public static final int DEFAULT_HP=100;
    private int maxHP = DEFAULT_HP;

    private int x,y;

    private int hp=DEFAULT_HP;
    private String name;
    private int atk;
    public static final int ATK_MAX = 100;
    public static final int ATK_MIN = 20;
    private int def;
    private int speed=DEFAULT_SPEED;
    private int dir;
    private int state=STATE_STAND;
    private Color color;
    private boolean isEnemy=false;
    private BloodBar bar = new BloodBar();
    // 炮弹
    private List<Bullet> bullets = new ArrayList();
    //使用容器保存当前坦克上的所有的爆炸效果
    private List<Explode> explodes = new ArrayList<>();
    public Tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        initTank();
    }
    public Tank()
    {
        initTank();
    }
    private void initTank()
    {
        color = MyUtil.getRandomColor();
        name = MyUtil.getRandomName();
        atk = MyUtil.getRandomNumber(ATK_MIN,ATK_MAX);
    }
    //绘制坦克
    public void draw(Graphics g)
    {
        logic();
        drawImgTank(g);
        drawBullets(g);
        drawName(g);
        bar.draw(g);

    }
    private void drawName(Graphics g)
    {
        g.setColor(color);
        g.setFont(constant.SMALL_FONT);
        g.drawString(name,x-RADIUS,y-30);
    }

    /**
     * 使用图片的方法绘制坦克
     * @param g
     */
   public abstract void drawImgTank(Graphics g);

    /**
     * 使用系统的方法绘制坦克
     * @param g
     */
    private void drawTank(Graphics g){
        g.setColor(color);
        //绘制坦克的圆
        g.fillOval(x-RADIUS,y-RADIUS,RADIUS<<1,RADIUS<<1);  //画圆
        int endX=x;
        int endY=y;
        switch (dir){
            case DIR_UP:endY=y-RADIUS*2;
                break;
            case DIR_DOWN:endY=y+RADIUS*2;
                break;
            case DIR_LEFT:endX=x-2*RADIUS;
                break;
            case DIR_RIGHT:endX=x+2*RADIUS;
                break;
        }
        g.drawLine(x,y,endX,endY);
    }

    //坦克的逻辑处理
    private void logic()
    {
        switch (state)
        {
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                break;
            case STATE_DIE:
                break;
        }
    }
    //坦克移动的功能
    private int oldX =-1,oldY = -1;
    private void move(){
        oldX = x;
        oldY = y;
        switch (dir){
            case DIR_UP:
                y-=speed;
                if (y<RADIUS*2+GameFrame.titleBarH){
                    y=RADIUS*2+GameFrame.titleBarH;
                }
                break;
            case DIR_DOWN:
                y+=speed;
                if (y>constant.Frame_height-RADIUS){
                    y=constant.Frame_height-RADIUS;
                }
                break;
            case DIR_LEFT:
                x-=speed;
                if (x<RADIUS){
                    x=RADIUS;
                }
                break;
            case DIR_RIGHT:
                x+=speed;
                if (x>constant.Frame_wide-RADIUS){
                    x=constant.Frame_wide-RADIUS;
                }
                break;
        }
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private long fireTime;//上一次开火的时间
    public static final int FIRE_INTERVAL=500;//子弹发射的最小间隔
    /**
     * 坦克的功能，坦克开火的方法
     * 创建了一个子弹对象，子弹对象的属性信息通过坦克的信息获得
     * 然后将创建的子弹添加到坦克管理的容器中
     */
    public void fire(){
        if (System.currentTimeMillis() - fireTime > FIRE_INTERVAL) {
            int bulletx = x;
            int bullety = y;
            switch (dir) {
                case DIR_UP:
                    bullety -= RADIUS;
                    break;
                case DIR_DOWN:
                    bullety += RADIUS;
                    break;
                case DIR_LEFT:
                    bulletx -= RADIUS;
                    break;
                case DIR_RIGHT:
                    bulletx += RADIUS;
                    break;
            }
            Bullet bullet = BulletsPool.get();
            bullet.setX(bulletx);
            bullet.setY(bullety);
            bullet.setDir(dir);
            bullet.setAtk(atk);
            bullet.setColor(color);
            bullet.setVisible(true);
            bullets.add(bullet);

            //发射子弹之后，记录本次发射的时间
            fireTime =  System.currentTimeMillis();
            MusicUtil.playBomb();
        }
    }

    /**
     * 将当前坦克的发射所有的子弹绘制出来
     * @param g
     */
    private void drawBullets(Graphics g){
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.draw(g);
        }

        //遍历所有的子弹，将不可见的子弹移除并还原到对象池
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isVisible()){
                Bullet remove = bullets.remove(i);
                i--;
                BulletsPool.theReturn(remove);
            }
        }
        //System.out.println("坦克的子弹的数量："+bullets.size());
    }

    /**
     * 坦克销毁的时候，处理坦克所有的子弹
     */
    public void bulletsReturn(){
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            BulletsPool.theReturn(bullet);
        }
        bullets.clear();
    }

    //坦克和子弹碰撞的方法
    public void collideBullets(List<Bullet> bullets)
    {
        //遍历所有的子弹，依次和当前的坦克进行碰撞的检测
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();

            if (MyUtil.isCollide(this.x,y,RADIUS,bulletX,bulletY))//子弹和坦克碰上了
            {
                //子弹消失
                bullet.setVisible(false);
                //坦克收到伤害
                hurt(bullet);
                //添加爆炸效果
                addExplode(x+RADIUS,y+RADIUS);
            }
        }
    }

    //添加爆炸效果
    private void addExplode(int x,int y){
        //爆炸效果,以当前被击中的坦克为效果
        Explode  explode = ExplodesPool.get();
        explode.setX(x);
        explode.setY(y);
        explode.setVisible(true);
        explode.setIndex(0);
        explodes.add(explode);
    }


    //坦克受到伤害
    private void hurt(Bullet bullet)
    {
        //TODO
        int atk = bullet.getAtk();
        hp -= atk;
        if (hp<0)
        {
            hp=0;
            die();
        }

    }
    //坦克死亡需要处理的内容
    private void die()
    {
        if (isEnemy)
        {
            GameFrame.killEnemyCount++;
            //敌人坦克被消灭了,归还对象池
            EnemyTanksPool.theReturn(this);
            //本关是否结束
            if (GameFrame.isCrossLevel()){
                //判断游戏是否通关
                if (GameFrame.isLastlevel()){
                    //通关
                    GameFrame.setGameState(constant.STATR_WIN);
                }else {
                    //进入下一关
                    GameFrame.startCrossLevel();
                }
            }

        }else{
            delaySecondsToOver(1000);
            //切换游戏状态
            GameFrame.setGameState(constant.STATR_LOST);
        }
    }

    /**
     * 判断当前的坦克是否死亡
     * @return
     */
    public boolean isDie()
    {
        return hp <= 0;
    }

    /**
     * 绘制当前坦克上的所有的爆炸的效果
     * @param g
     */
    public void drawExplodes(Graphics g)
    {
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            explode.draw(g);
        }
        //将不可见的爆炸效果删除，还回对象池
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            if (!explode.isVisible())
            {
                Explode remove = explodes.remove(i);
                ExplodesPool.theReturn(remove);
                i--;
            }

        }
    }

    //内部类表示坦克的血条
    class BloodBar{
        public static final int BAR_LENGTH = 50;
        public static final int BAR_HEIGHT = 3;

        public void draw(Graphics g)
        {
            //填充底色
            g.setColor(Color.YELLOW);
            g.fillRect(x-RADIUS,y-RADIUS-BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);
            //红色的当前血量
            g.setColor(Color.RED);
            g.fillRect(x-RADIUS,y-RADIUS-BAR_HEIGHT*2,hp*BAR_LENGTH/maxHP,BAR_HEIGHT);
            //边框
            g.setColor(Color.BLACK);
            g.drawRect(x-RADIUS,y-RADIUS-BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);

        }
    }
    //坦克的子弹和地图所有的块的碰撞
    public void bulletsCollideMapTiles(List<MapTile> tiles)
    {
        //foreach遍历容器中的元素，在遍历过程中只能使用迭代器的删除方式删除元素
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (tile.isCollideBullet(bullets)){
                //添加爆炸效果
                addExplode(tile.getX()+MapTile.radius*2+5,tile.getY()+MapTile.tileW);
                //地图钢铁块无法击毁
                if (tile.getType() == MapTile.TYPE_HARD)
                    continue;
                //设置地图块销毁
                tile.setVisible(false);
                //归还对象池
                MapTilePool.theReturn(tile);
                //当老巢被击毁后，一秒钟切换到游戏结束的界面
                if (tile.isHouse()){
                    delaySecondsToOver(500);
                }
            }
        }
    }

    /**
     * 延迟若干毫秒切换到游戏结束
     * @param millisSecond
     */
    private void delaySecondsToOver(int millisSecond){
        new Thread(){
            public void run() {
                try {
                    Thread.sleep(millisSecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameFrame.setGameState(constant.STATR_LOST);
            }
        }.start();
    }
    /**
     * 一个地图块和当前坦克碰撞的方法
     * 从提了中提取八个点 来判断8个点是否有任何一个点和当前的坦克有了碰撞
     * 点的顺序从左上角开始，顺时针
     * @param tiles
     * @return
     */
    public boolean isCollideTile(List<MapTile> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (!tile.isVisible() || tile.getType()==MapTile.TYPE_COVER)
                continue;
            //点1 左上点
            int tileX = tile.getX();
            int tileY = tile.getY();
            boolean collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            //如果碰上了就直接返回，否则继续判断下一个点
            if (collide) {
                return true;
            }
            //点2 中上点
            tileX = tileX + MapTile.radius;
            collide = MyUtil.isCollide(x,y,RADIUS,tileX,tileY);
            if (collide){
                return true;
            }
            //点3 右上点
            tileX = tileX + MapTile.radius;
            collide = MyUtil.isCollide(x,y,RADIUS,tileX,tileY);
            if (collide){
                return true;
            }
            //点4 右中点
            tileY = tileY + MapTile.radius;
            collide = MyUtil.isCollide(x,y,RADIUS,tileX,tileY);
            if (collide){
                return true;
            }
            //点5 右下点
            tileY = tileY + MapTile.radius;
            collide = MyUtil.isCollide(x,y,RADIUS,tileX,tileY);
            if (collide){
                return true;
            }
            //点6 下中点
            tileY = tileY - MapTile.radius;
            collide = MyUtil.isCollide(x,y,RADIUS,tileX,tileY);
            if (collide){
                return true;
            }
            //点7 左下点
            tileX = tileX - MapTile.radius;
            collide = MyUtil.isCollide(x,y,RADIUS,tileX,tileY);
            if (collide){
                return true;
            }
            //点8 左中点
            tileY = tileY - MapTile.radius;
            collide = MyUtil.isCollide(x,y,RADIUS,tileX,tileY);
            if (collide){
                return true;
            }
        }
        return false;
    }

    //坦克回退的方法
    public void back(){
        x = oldX;
        y = oldY;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
