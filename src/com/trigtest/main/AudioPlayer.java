package com.trigtest.main;

import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;

import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {

    public static Map<String, Sound> soundMap = new HashMap<>();
    public static Map<String, Music> musicMap = new HashMap<>();

    public static void loadMusic(){
        try {
            musicMap.put("backgroundLoop", new Music("assets/sound/backgroundMusic.wav"));

            soundMap.put("pass", new Sound("assets/sound/pass.ogg"));
            soundMap.put("fail", new Sound("assets/sound/fail.ogg"));
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public static Music getMusic(String k){
        return musicMap.get(k);
    }
    public static Sound getSound(String k){
        return soundMap.get(k);
    }
}
