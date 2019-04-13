package com.trigtest.main;

import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;

import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {

    private static Map<String, Sound> soundMap = new HashMap<>();
    private static Map<String, Music> musicMap = new HashMap<>();

    public static void loadMusic(){
        try {
            musicMap.put("backgroundLoop", new Music("assets/sound/backgroundMusic.wav"));
            musicMap.put("gameLoop", new Music("assets/sound/gameLoop.wav"));

            soundMap.put("pass", new Sound("assets/sound/pass.ogg"));
            soundMap.put("fail", new Sound("assets/sound/fail.ogg"));
            soundMap.put("win", new Sound("assets/sound/applause.ogg"));
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

    public static void pause(String s){
        musicMap.get(s).pause();
    }

    public static void stop(String s){
        musicMap.get(s).stop();
    }

    public static void setVolume(float vol){
        for(int i = 0; i < musicMap.size(); ++i)
            musicMap.get((musicMap.keySet().toArray())[i]).setVolume(vol);
    }
    public static void resetSound(){
        musicMap.clear();
        soundMap.clear();
    }
}
