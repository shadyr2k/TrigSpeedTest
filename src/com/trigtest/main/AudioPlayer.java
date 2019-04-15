package com.trigtest.main;

import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;

import java.util.HashMap;
import java.util.Map;

class AudioPlayer {

    private static Map<String, Sound> soundMap = new HashMap<>();
    private static Map<String, Music> musicMap = new HashMap<>();

    static void loadMusic(){
        try {
            musicMap.put("backgroundLoop", new Music("assets/sound/backgroundMusic.wav"));
            musicMap.put("gameLoop", new Music("assets/sound/gameLoop.wav"));

            soundMap.put("pass", new Sound("assets/sound/pass.ogg"));
            soundMap.put("fail", new Sound("assets/sound/fail.ogg"));
            soundMap.put("win", new Sound("assets/sound/applause.ogg"));
            soundMap.put("press", new Sound("assets/sound/buttonPress.ogg"));
            soundMap.put("menuPress", new Sound("assets/sound/menuPress.ogg"));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    static Music getMusic(String k){
        return musicMap.get(k);
    }
    static Sound getSound(String k){
        return soundMap.get(k);
    }

    static void pause(String s){
        musicMap.get(s).pause();
    }

    static void stop(String s){
        musicMap.get(s).stop();
    }

    static void setVolume(float vol){
        for(int i = 0; i < musicMap.size(); ++i)
            musicMap.get((musicMap.keySet().toArray())[i]).setVolume(vol);
    }
}
