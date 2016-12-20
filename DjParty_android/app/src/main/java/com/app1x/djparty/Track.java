package com.app1x.djparty;

/**
 * Created by nikojpapa on 12/19/16.
 */

public class Track extends Node {
    public String trackUri;
    public String trackName;
    public String trackArtist;
    public String trackDuration;

    public Track(String trackUri, String trackName, String trackArtist, String trackDuration) {
        super(trackUri);
        this.trackUri= trackUri;
        this.trackName= trackName;
        this.trackArtist= trackArtist;
        this.trackDuration= trackDuration;
    }
}
