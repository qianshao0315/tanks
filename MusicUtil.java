package war_of_tanks.util;

import javax.sound.sampled.AudioFileFormat;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;

/**
 * Write class comments here User: hasee
 * Date: 2021-4-14 14:51
 * version hasee: MusicUtil.java, v 0.1 Exp $
 */
public class MusicUtil {
    private static AudioClip start;
    private static AudioClip bomb;
    //装载音乐资源
    static {
        try {
            //引号中的是音乐文件所在绝对路径
            start = Applet.newAudioClip(new File("music/start.wav").toURL());
            bomb = Applet.newAudioClip(new File("music/bomb.wav").toURL());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void playStart(){
        start.play();
    }
    public static void playBomb(){
        bomb.play();
    }
}
