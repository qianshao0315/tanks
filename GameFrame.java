package war_of_tanks.game;
import war_of_tanks.Tank.EnemyTank;
import war_of_tanks.Tank.MyTank;
import war_of_tanks.Tank.Tank;
import war_of_tanks.map.GameMap;
import war_of_tanks.map.MapTile;
import war_of_tanks.util.MusicUtil;
import war_of_tanks.util.MyUtil;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static  war_of_tanks.util.constant.*;


public class GameFrame extends Frame implements Runnable{
    private Image overImg = null;//第一次使用的时候加载，而不是类加载的时候加载
    //定义一张和屏幕大小一致的图片
    private BufferedImage bufImg = new BufferedImage(Frame_wide,Frame_height,BufferedImage.TYPE_4BYTE_ABGR);

    public static int gameState;//游戏状态
    private static int menuIndex;//菜单指向
    public static int titleBarH;//标题栏的高度

    private static Tank myTank;
    //敌人的坦克容器
    private static List<Tank> enemies = new ArrayList<>();

    //用来记录本关卡产生敌人数量
    private static int bornEnemyCount;
    //
    public static int killEnemyCount;
    //定义地图相关的内容
    private static GameMap gameMap = new GameMap();

    public GameFrame()
    {
        Initframe();
        InitEventlister();
        //启动用于刷新窗口的线程
        new Thread(this).start();
    }

    /**
     * 进入下一关
     */
    public static void nextLevel() {
        startGame(LevelInfo.getInstance().getLevel()+1);
    }

    //开始过关动画
    public static int flashTime;
    public static final int RECT_WIDTH =40;
    public static final int RECT_COUNT =Frame_wide/RECT_WIDTH+1;
    public static boolean isOpen = false;
    public static void startCrossLevel(){
        gameState = STATR_CROSS;
        flashTime = 0;
        isOpen = false;
    }
    //绘制过关动画
    public void drawCross(Graphics g){
        gameMap.drawBk(g);
        myTank.draw(g);
        gameMap.drawCover(g);

        g.setColor(Color.BLACK);
        //关闭百叶窗的效果
        if (!isOpen){
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i*RECT_WIDTH,0,flashTime,Frame_height);
            }
            //所有的叶片都关闭
            if (flashTime++ - RECT_WIDTH > 5){
                isOpen = true;
                //初始化下一个地图
                gameMap.initMap(LevelInfo.getInstance().getLevel()+1);
            }
        }else {
            //开百叶窗的效果
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i*RECT_WIDTH,0,flashTime,Frame_height);
            }
            if (flashTime-- == 0){
                startGame(LevelInfo.getInstance().getLevel());
            }
        }
    }

    //对游戏进行初始化
    private void initGame()
    {
        gameState=STATR_MENU;
    }
    //对窗口进行初始化
    private void Initframe()
    {
        setTitle(Game_title);//设置标题
        setSize(Frame_wide,Frame_height);
        setLocation(Frame_x,Frame_y);
        setResizable(false);
        setVisible(true);
        //求标题栏的高度
        int top =getInsets().top;


    }

    public void update(Graphics g1) {
        //系统画笔g1，图片画笔g
        //得到图片的画笔
        Graphics g = bufImg.getGraphics();

        //使用图片画笔将所有内容绘制到图片上 快捷创建函数 alt+enter
        g.setFont(GAME_FONT);
        switch (gameState)
        {
            case STATR_MENU:
                drawMenu(g);
                break;
            case STATR_HELP:
                drawHelp(g);
                break;
            case STATR_ABOUT: 
                drawAbout(g);
                break;
            case STATR_RUN:
                drawRun(g);
                break;
            case STATR_LOST:
                drawLost(g,"大侠请从新来过");
                break;
            case STATR_WIN:
                drawWin(g);
                break;
            case STATR_CROSS:
                drawCross(g);
                break;
        }
        //使用系统画笔，将图片绘制到frame上来
        g1.drawImage(bufImg,0,0,null);
    }

    /**
     * 绘制游戏结束的代码
     * @param g
     */
    private void drawLost(Graphics g,String str) {
        //保证只加载一次
        if (overImg == null)
        {
            overImg = MyUtil.createImage("E:/IdeaProjects/war_of_tanks/img/over.jpg");
        }
        //背景黑色填充
        g.setColor(Color.BLACK);
        g.fillRect(0,0, Frame_wide,Frame_height);

        int imgw = overImg.getWidth(null);
        int imgh = overImg.getHeight(null);
        g.drawImage(overImg,Frame_wide-imgw >> 1,Frame_height-imgh >> 1,null);

        //添加按键提示信息
        g.setColor(Color.white);
        g.drawString(OVER_STR0,10,Frame_height-30);
        g.drawString(OVER_STR1,Frame_wide-200,Frame_height-30);

        //失败文字
        g.setColor(Color.white);
        g.drawString(str,Frame_wide/2-30,50);
    }



    //游戏运行状态的绘制内容
    private void drawRun(Graphics g) {
        //绘制背景颜色
        g.setColor(Color.black);
        g.fillRect(0,0,Frame_wide,Frame_height);
        //绘制地图
        gameMap.drawBk(g);

        drawEnemies(g);
        myTank.draw(g);

        //绘制地图的遮挡层
        gameMap.drawCover(g);
        //临时检验
        //System.out.println(myTank.getX()+" "+myTank.getY()+" "+myTank.getHp()+" "+myTank.getSpeed()+" "+myTank.getState());
        drawExplodes(g);
        //子弹和坦克的碰撞方法
        bulletCollideTank();
        //子弹和所有地图块的碰撞
        bulletAndTanksCollideMapTile();
    }

    /**
     * 绘制游戏胜利的界面
     * @param g
     */
    private void drawWin(Graphics g){
        drawLost(g,"游戏通关！！！");
    }
    //绘制所有的敌人，如果敌人已经死亡，从容器中移除
    private void drawEnemies(Graphics g)
    {
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if (enemy.isDie())
            {
                enemies.remove(i);
                i--;//容器中做移除，要做i--，保证新位置i上的元素能重新被遍历
                continue;
            }
            enemy.draw(g);
        }
    }
    private void drawAbout(Graphics g) {
    }

    private void drawHelp(Graphics g) {
    }

    //绘制菜单状态下的内容
    private void drawMenu(Graphics g)
    {
        //绘制黑色的背景
        g.setColor(Color.black);
        g.fillRect(0,0,Frame_wide,Frame_height);

        final int STR_WIDTH=80;
        int x=Frame_wide-STR_WIDTH>>1;
        int y=Frame_height / 3;

        //行间距
        final int DIS = 40;
        //白色的字
        g.setColor(Color.white);
        for (int i = 0; i < MENUS.length; i++)
        {
            if(i==menuIndex)//选中的菜单项设置为红色
            {
                g.setColor(Color.RED);
            }
            else {
                g.setColor(Color.white);
            }
            g.drawString(MENUS[i],x,y+DIS*i);
        }
    }
    //监听
    private void InitEventlister()
    {
        //初始化窗口监听
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //按键监听
        addKeyListener(new KeyAdapter() {
            //按键被按下的时候回调到方法
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode =e.getKeyCode();
                //不同的游戏状态给出不同处理方法
                switch (gameState){
                    case STATR_MENU:
                        keyEventMenu (keyCode);
                        break;
                    case STATR_HELP:
                        keyEventHelp (keyCode);
                        break;
                    case STATR_ABOUT:
                        keyEventAbout (keyCode);
                        break;
                    case STATR_RUN:
                        keyEventRun (keyCode);
                        break;
                    case STATR_LOST:
                        keyEventLost (keyCode);
                        break;
                    case STATR_WIN:
                        keyEventWin (keyCode);
                        break;

                }
            }

            //按键松开的时候回调的内容
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode =e.getKeyCode();
                //不同的游戏状态给出不同处理方法
               if (gameState==STATR_RUN){
                   keyReleasedEventRun (keyCode);

               }
            }
            //按键松开的时候，游戏中的处理方法
            private void keyReleasedEventRun(int keyCode) {
                switch (keyCode){
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        myTank.setState(Tank.STATE_STAND);
                        break;
                }
            }
        });
    }
    //游戏通关的按键处理
    private void keyEventWin(int keyCode) {
        keyEventLost(keyCode);
    }

    /**
     * 游戏结束的按键处理
     * @param keyCode
     */
    private void keyEventLost(int keyCode) {
        //结束游戏
        if (keyCode == KeyEvent.VK_ESCAPE)
        {
            System.exit(0);
        }else if (keyCode == KeyEvent.VK_ENTER)
        {
            setGameState(STATR_MENU);
            //很多的游戏操作需要关闭，有些的某些属性需要重置
            resetGame();
        }
    }
    //重置游戏状态
    private void resetGame()
    {
        killEnemyCount = 0;
        menuIndex = 0;
        //先让子弹还回对象池
        myTank.bulletsReturn();
        //销毁自己坦克
        myTank = null;
        //清空敌人相关资源
        for (Tank enemy : enemies) {
            enemy.bulletsReturn();
        }
        enemies.clear();
        //清空地图资源
        gameMap = null;


    }

    //游戏运行中的按键处理方法
    private void keyEventRun(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myTank.setDir(Tank.DIR_UP);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myTank.setDir(Tank.DIR_DOWN);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myTank.setDir(Tank.DIR_LEFT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setDir(Tank.DIR_RIGHT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_SPACE:
                myTank.fire();
                break;

        }
    }

    private void keyEventAbout(int keyCode) {
    }

    private void keyEventHelp(int keyCode) {
    }
    //菜单状态下的按键的处理
    private void keyEventMenu(int keyCode) {
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (--menuIndex<0)menuIndex=MENUS.length-1;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (++menuIndex>MENUS.length-1)menuIndex=0;
                break;
            case KeyEvent.VK_ENTER:
                switch (menuIndex){
                    case 0:
                        startGame(1);
                        break;
                    case 1:
                        //继续游戏，进入选关界面 TODO
                        System.exit(0);
                        break;
                    case 2:
                        setGameState(STATR_HELP);
                        break;
                    case 3:
                        setGameState(STATR_ABOUT);
                        break;
                    case 4:
                        System.exit(0);
                        break;

                }
                break;

        }

    }

    /**
     * 开始游戏的方法
     * @param level 第几关
     */
    private static void startGame(int level) {
        enemies.clear();
        if (gameMap == null){
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        MusicUtil.playStart();
        killEnemyCount=0;
        bornEnemyCount=0;
        gameState= STATR_RUN;
        //创建坦克对象，敌人的坦克对象
        myTank= new MyTank(Frame_wide/3,Frame_height-Tank.RADIUS,Tank.DIR_UP);

        //使用一个单独的线程用于控制生产敌人的坦克
        new Thread(){
            @Override
            public void run()
            {
                while (true)
                {
                    if (LevelInfo.getInstance().getEnemyCount() > bornEnemyCount && enemies.size() < ENEMY_MAX_COUNT )
                    {
                        Tank enemy =EnemyTank.createEnemy();
                        enemies.add(enemy);
                        bornEnemyCount++;
                    }
                    try
                    {
                        Thread.sleep(ENEMY_BORN_INTERVAL);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    //游戏只有在run状态才创建敌人
                    if(gameState !=STATR_RUN){
                        break;
                    }
                }
            }
        }.start();

    }

    @Override
    public void run() {
        while (true)
        {
            repaint();
            try {
                Thread.sleep(REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //是Frame类的方法，继承下来的方法
    //该方法负责了所有的绘制内容，所有需要在屏幕中显示的
    //内容都要的在给方法内调用，该方法不能主动调用
    //repaint（）回调该方法



    private void bulletCollideTank()
    {
        //我的坦克子弹和敌人的坦克碰撞
//        for (Tank enemy : enemies) {
//            enemy.collideBullets(myTank.getBullets());
//        }
//        //敌人的坦克子弹和我的坦克碰撞
//        for (Tank enemy : enemies) {
//            myTank.collideBullets(enemy.getBullets());
//        }
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.collideBullets(myTank.getBullets());
        }
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            myTank.collideBullets(enemy.getBullets());
        }
    }

    //所有的子弹和地图块的碰撞
    private void bulletAndTanksCollideMapTile(){
        //自己的坦克的子弹和地图块的碰撞
        myTank.bulletsCollideMapTiles(gameMap.getTiles());
        //敌人所有的坦克的炮弹和地图块的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.bulletsCollideMapTiles(gameMap.getTiles());
        }
        //我的坦克和地图的碰撞
        if (myTank.isCollideTile(gameMap.getTiles())){
            myTank.back();
        }
        //敌人坦克和地图的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if (enemy.isCollideTile(gameMap.getTiles())){
                enemy.back();
            }
        }
        //清理的所有的被销毁的地图块
        gameMap.clearDestoryTile();
    }

    //所有的坦克上的爆炸效果
    private void drawExplodes(Graphics g)
    {
        for (Tank enemy : enemies) {
            enemy.drawExplodes(g);
        }
        myTank.drawExplodes(g);
    }
    //获得游戏状态和修改游戏状态
    public static int getGameState() {
        return gameState;
    }
    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }

    /**
     * 游戏是否为最后一关
     * @return
     */
    public static boolean isLastlevel()
    {
        //当前关卡和总关卡一致
        int currLevel = LevelInfo.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
        return (currLevel==levelCount);
    }

    /**
     * 判断是否过关
     * @return
     */
    public static boolean isCrossLevel(){
        //消灭的敌人数量和关卡中的敌人数量一致
        return killEnemyCount==LevelInfo.getInstance().getEnemyCount();
    }
}

