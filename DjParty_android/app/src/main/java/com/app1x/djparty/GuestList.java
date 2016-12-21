package com.app1x.djparty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikojpapa on 12/18/16.
 */

public class GuestList {
    public LinkedList guests;

    public GuestList(){}

    public GuestList(Map<String, Node> guests){
//        HashMap<String, Node> convertedMap= new HashMap<>();
//        for (Map.Entry<String, Guest> entry : guests.entrySet()) {
//            convertedMap.put(entry.getKey(), entry.getValue());
//        }
        this.guests= (LinkedList) guests;
    }

//    public String toString() {
//        return getIds();
//    }
}
