package com.app1x.djparty;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikojpapa on 12/19/16.
 */

public class LinkedList extends HashMap<String, Node> {

//    public LinkedList(){}

//    public LinkedList(Map<String, Node> nodes) {
//        super(nodes);
//    }
//
//    public Map<String, Node> getNodes() {
//        return nodes;
//    }
//
//    public String toString() {
//        return nodes.toString();
//    }

//    public String getIds() {
//        String ids= "";
//        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
//            ids+= entry.getKey()+",";
//        }
//        return ids;
//    }

    public int length() {
        return size();
    }

    @Exclude
    public boolean isEmpty() {
        return length()==0;
    }

//    public Node get(String id) {
//        return nodes.get(id);
//    }
//
//    public void put(String id, Node node) {
//        nodes.put(id, node);
//    }

    public Node findHead() {
        for (Map.Entry<String, Node> entry : entrySet()) {
            String id= entry.getKey();
            Node node= entry.getValue();

            if (node.previous==null) return node;
        }
        return null;
    }

    public Node findTail() {
        for (Map.Entry<String, Node> entry : entrySet()) {
            String id= entry.getKey();
            Node node= entry.getValue();

            if (node.next==null) return node;
        }
        return null;
    }

    public void insertNode(Node node, int position) {
        if (isEmpty()) {
            return;
//            HashMap<String, Node> newList = new HashMap<>();
//            newList.put(node.id, node);
//            return new LinkedList(newList);
        }

        if (position==0) {
            Node headNode= findHead();
            node.previous= null;
            headNode.previous= node.id;
            node.next= headNode.id;
        } else if (position==length()) {
            Node endNode= findTail();
            node.next= null;
            endNode.next= node.id;
            node.previous= endNode.id;
        } else {
            Node currentNode= findHead();
            int index= 0;
            while (++index<position) {
                currentNode= get(currentNode.next);
            }
            get(currentNode.next).previous= node.id;
            node.next= currentNode.next;
            node.previous= currentNode.id;
            currentNode.next= node.id;
        }
        put(node.id, node);
    }

    public void removeNode(Node node) {
        if (isEmpty()) {
            return;
        }

        Node currentNode= findHead();
        while (currentNode!=null) {
            if (currentNode.id==node.id) {
                if (node.previous!=null) get(node.previous).next= node.next;
                if (node.next!=null) get(node.next).previous= node.previous;
                remove(node.id);
                break;
            }
            currentNode= get(node.next);
        }
    }

    public void cycleNodes(String stopAt) {
        if (length()>1) {
            Node head= null;
            do {
                head= findHead();
                Node tail= findTail();
                get(head.next).previous= null;
                head.next= null;
                head.previous= tail.id;
                tail.next= head.id;
                if (stopAt==null) break;
            } while (head.id!=stopAt);
        }
    }

}
