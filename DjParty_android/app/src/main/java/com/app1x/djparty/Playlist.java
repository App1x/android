package com.app1x.djparty;

import java.util.Map;

/**
 * Created by nikojpapa on 12/19/16.
 */

public class Playlist {
    public LinkedList tracks;

    public Playlist(){}

    public Playlist(Map<String, Track> tracks){
        tracks= tracks;
    }

}
