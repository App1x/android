package com.app1x.djparty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikojpapa on 12/19/16.
 */

public class Node {
    String next= null;
    String previous= null;
    String id;

    public Node(){}

    public Node(String id) {
        this.id= id;
    }

    public static Node findHead(Map<String, Node> linkedList) {
        for (Map.Entry<String, Node> entry : linkedList.entrySet()) {
            String id= entry.getKey();
            Node node= entry.getValue();

            if (node.previous==null) return node;
        }
        return null;
    }

    public static Node findTail(Map<String, Node> linkedList) {
        for (Map.Entry<String, Node> entry : linkedList.entrySet()) {
            String id= entry.getKey();
            Node node= entry.getValue();

            if (node.next==null) return node;
        }
        return null;
    }

    public void insertNode(Map<String, Node> linkedList, int position) {
        if (linkedList.isEmpty()) {
            return;
//            HashMap<String, Node> newList = new HashMap<>();
//            newList.put(node.id, node);
//            return new LinkedList(newList);
        }

        if (position==0) {
            Node headNode= findHead(linkedList);
            this.previous= null;
            headNode.previous= this.id;
            this.next= headNode.id;
        } else if (position==linkedList.size()) {
            Node endNode= findTail(linkedList);
            this.next= null;
            endNode.next= this.id;
            this.previous= endNode.id;
        } else {
            Node currentNode= findHead(linkedList);
            int index= 0;
            while (++index<position) {
                currentNode= linkedList.get(currentNode.next);
            }
            linkedList.get(currentNode.next).previous= this.id;
            this.next= currentNode.next;
            this.previous= currentNode.id;
            currentNode.next= this.id;
        }
        linkedList.put(this.id, this);
    }

    public static void removeNode(Map<String, Node> linkedList, Node node) {
        if (linkedList.isEmpty()) {
            return;
        }

        Node currentNode= findHead(linkedList);
        while (currentNode!=null) {
            if (currentNode.id==node.id) {
                if (node.previous!=null) linkedList.get(node.previous).next= node.next;
                if (node.next!=null) linkedList.get(node.next).previous= node.previous;
                linkedList.remove(node.id);
                break;
            }
            currentNode= linkedList.get(node.next);
        }
    }

    public static void cycleNodes(Map<String, Node> linkedList, String stopAt) {
        if (linkedList.size()>1) {
            Node head= null;
            do {
                head= findHead(linkedList);
                Node tail= findTail(linkedList);
                linkedList.get(head.next).previous= null;
                head.next= null;
                head.previous= tail.id;
                tail.next= head.id;
                if (stopAt==null) break;
            } while (head.id!=stopAt);
        }
    }

}
