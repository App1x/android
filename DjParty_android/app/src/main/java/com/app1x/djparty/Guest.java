package com.app1x.djparty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikojpapa on 12/19/16.
 */

public class Guest extends Node {
    public Map<String, Node> playlist;

    public Guest(String guestName) {
        super(guestName);
        this.playlist= new HashMap<>();
    }

}
