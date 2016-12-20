package com.app1x.djparty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikojpapa on 12/18/16.
 */

public class Party {
    public String password;
    public String host;
    public String nextInLine;
    public String nextUp;
    public Track currentlyPlaying;
    public GuestList guestList;

    public Party() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Party(String password, String host) {
        this.password= password;
        this.host= host;

        GuestList newGuestList= new GuestList();
        newGuestList.put(host, new Node(host));
        this.guestList= newGuestList;
    }

    public Party(String password, String host, String nextInLine, String nextUp, Track
            currentlyPlaying, GuestList guestList) {
        this.password = password;
        this.host = host;
        this.nextInLine = nextInLine;
        this.nextUp = nextUp;
        this.currentlyPlaying= currentlyPlaying;
        this.guestList= guestList;
    }

    public String toString() {
        String str= "{\npassword: "+password+"\nhost: "+host;
        if (nextInLine!=null) str+= "\nnextInLine: "+nextInLine;
        if (nextUp!=null) str+= "\nnextUp: "+nextUp;
        if (currentlyPlaying!=null) str+= "\ncurrentlyPlaying: "+currentlyPlaying.id;
        if (guestList!=null) str+= "\nguestList: "+guestList.toString();
        str+= "\n}";

        return str;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public String getHost() {
//        return host;
//    }
//
//    public String getNextInLine() {
//        return nextInLine;
//    }
//    public String getNextUp() {
//        return nextUp;
//    }
//
//    public Track getCurrentlyPlaying() {
//        return currentlyPlaying;
//    }
//
//    public GuestList getGuestList() {
//        return guestList;
//    }

}
