package com.app1x.djparty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikojpapa on 12/19/16.
 */

public class Playlist {
    public LinkedList tracks;

    public Playlist(){}

    public Playlist(Map<String, Track> tracks){
        HashMap<String, Node> convertedMap= new HashMap<>();
        for (Map.Entry<String, Track> entry : tracks.entrySet()) {
            convertedMap.put(entry.getKey(), entry.getValue());
        }
        this.tracks= (LinkedList) convertedMap;
    }

}
