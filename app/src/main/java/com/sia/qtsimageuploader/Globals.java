package com.sia.qtsimageuploader;

import android.widget.Button;

import java.util.LinkedList;
import java.util.List;

public class Globals {

    public static Settings _Settings;

    public static void InitSettings(MainActivity main){
        if(_Settings == null){
            _Settings = new Settings(main);
        }
    }

}
