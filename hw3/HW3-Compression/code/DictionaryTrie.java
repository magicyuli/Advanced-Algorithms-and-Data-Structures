/**
 ******************************************************************************
 *                    HOMEWORK  15-351/650
 ******************************************************************************
 *
 *       This class implements the dictionary trie.
 *       The trie supports all 256 ASCII characters
 *       We suggest to use a HashMap in the trie nodes to store the children
 *
 *
 * @author
 * @date
 *****************************************************************************/
 /*****************************************************************************

               DESIGN and IMPLEMENT THIS CLASS

 *****************************************************************************/


import java.util.*;
import java.io.*;


public class DictionaryTrie {
    private Node root = new Node();
    private Node cur = root;

    public void reset() {
        cur = root;
    }

    public boolean travel(byte key) {
        boolean found = cur.hasNext(key);
        cur = cur.next(key);

        return found;
    }

    public void end(int value) {
        cur.value = value;
        reset();
    }

    public int getValue() {
        return cur.value;
    }

    private static class Node {
        private Map<Byte, Node> cld;
        private int value;

        private Node() {
            cld = new HashMap<Byte, Node>();
        }

        private boolean hasNext(byte k) {
            return cld.containsKey(k);
        }

        private Node next(byte k) {
            if (!cld.containsKey(k)) {
                cld.put(k, new Node());
            }
            
            return cld.get(k);
        }
    }
}
