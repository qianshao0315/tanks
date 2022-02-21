package war_of_tanks.util;

import java.awt.*;

//游戏中的常量都在该类中维护，方便后期
public class constant {
        /*************************游戏窗口******************************/
        public static final String Game_title ="坦克大战";

        public static final int Frame_wide=800;
        public static final int Frame_height=600;

        //动态的获得系统的宽和高
        public static final int SCREEN_W = Toolkit.getDefaultToolkit().getScreenSize().width;
        public static final int SCREEN_H = Toolkit.getDefaultToolkit().getScreenSize().height;

        public static final int Frame_x=SCREEN_W-Frame_wide>>1;
        public static final int Frame_y=SCREEN_H-Frame_height>>1;

        /**************************游戏菜单**************************/

        public static final int STATR_MENU=0;
        public static final int STATR_HELP=1;
        public static final int STATR_ABOUT=2;
        public static final int STATR_RUN=3;
        public static final int STATR_LOST=4;
        public static final int STATR_WIN=5;
        public static final int STATR_CROSS=6;

        public static final String[] MENUS={
                "开始游戏",
                "继续游戏",
                "游戏帮助",
                "游戏关于",
                "退出游戏"
        };
        public static final String OVER_STR0 = "ESC键退出游戏";
        public static final String OVER_STR1 = "ENTER键退回主菜单";


        //字体
        public static final Font GAME_FONT= new Font("宋体",Font.BOLD,24);//字体的设置
        public static final Font SMALL_FONT= new Font("宋体",Font.BOLD,12);//字体的设置
        //窗口重绘时间间隔30ms
        public static final int REPAINT_INTERVAL = 30;
        //最大敌人数量
        public static final int ENEMY_MAX_COUNT = 10;
        public static final int ENEMY_BORN_INTERVAL = 5000;
        //
        public static final int ENEMY_AI_INTERVAL = 3000;
        public static final double ENEMY_FIRE_PERCENT =0.03;



    }
