/**
 ******************************************************************************
 *                    HOMEWORK, 15-351/650
 ******************************************************************************
 *                    Skip List
 ******************************************************************************
 *
 * Implementation of a skip list, introduced by William Pugh in his 1990 paper.
 *
 *
 * User ID(s):
 *
 *****************************************************************************/

import java.util.*;

@SuppressWarnings("unchecked")
public class Dictionary<E extends Comparable<E>> implements DictionaryInterface<E>
{
    private static final boolean DEBUG = false;
    private Node<E> head;
    private int maxLevel;
    private int size;
    private Random rand;

    public static final boolean HEAD = true;

    private static void debug(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }

    /**
     * Creates an empty dictionary and starts a random generator.
     */
    public Dictionary()
    {
        size = 0;
        maxLevel = 0;
        rand = new Random();
        head = new Node<E>(null);
    }
    /**
     * Creates an empty dictionary and starts a random generator.
     */
    public Dictionary(int seed)
    {
        size = 0;
        maxLevel = 0;
        rand = new Random(seed);
        head = new Node<E>(null);
    }

    /**
     * Adds an item to the dictionary.
     * The method does not allow duplicates.
     */
    public void insert(E item) {
        Stack<Node<E>> stk = search(item);

        if (nextIsTarget(stk, item)) {
            return;
        }

        int lvl = -1;
        Node<E> newNode = new Node<E>(item);
        Node<E> cur;
        double coin;

        debug("[insert] stk size: " + stk.size());

        do {
            lvl++;
            cur = stk.pop();

            if (cur.nexts.size() > lvl) {
                newNode.nexts.add(cur.nexts.get(lvl));
                cur.nexts.set(lvl, newNode);
            }
            else {
                cur.nexts.add(newNode);
                newNode.nexts.add(null);
            }

            coin = rand.nextFloat();
            debug("[insert] coin: " + coin);
            
        } while (!stk.empty() && coin >= 0.5);

        while (coin >= 0.5) {
            head.nexts.add(newNode);
            newNode.nexts.add(null);
            lvl++;
            coin = rand.nextFloat();
            debug("[insert] extra coin: " + coin);
        }

        size++;
    }

    private Stack<Node<E>> search(E tar) {
        Node<E> cur = head;
        int lvl = head.nexts.size() - 1;
        Stack<Node<E>> stk = new Stack<Node<E>>();

        while (lvl >= 0) {
            if (cur.nexts.size() > lvl 
                && cur.nexts.get(lvl) != null 
                && cur.nexts.get(lvl).val.compareTo(tar) < 0) {

                cur = cur.nexts.get(lvl);
            }
            // next on this lvl is: null or >= tar
            else {
                stk.push(cur);
                lvl--;
            }
        }

        if (stk.empty()) {
            stk.push(head);
        }
        
        return stk;
    }


    /**
     * Returns true if the dictionary contains an element,
     * otherwise - false;
     */
    public boolean contains(E item) {
        Stack<Node<E>> stk = search(item);

        return nextIsTarget(stk, item);
    }


    /**
     * Deletes an item from the dictionary and returns true
     */
    public boolean delete(E item) {
        Stack<Node<E>> stk = search(item);

        if (!nextIsTarget(stk, item)) {
            return false;
        }

        debug("[delete] stk size: " + stk.size());

        Node<E> cur;
        Node<E> next;
        int lvl = 0;

        while (nextIsTarget(stk, item, lvl)) {
            cur = stk.pop();

            next = cur.nexts.get(lvl).nexts.size() > lvl ? cur.nexts.get(lvl).nexts.get(lvl) : null;
            cur.nexts.set(lvl, next);

            lvl++;
        }

        size--;

        return true;
    }

    private boolean nextIsTarget(Stack<Node<E>> stk, E target) {
        return nextIsTarget(stk, target, 0);
    }

    private boolean nextIsTarget(Stack<Node<E>> stk, E target, int lvl) {
        return !stk.empty() && stk.peek().nexts.size() > lvl &&
            stk.peek().nexts.get(lvl) != null && stk.peek().nexts.get(lvl).val.compareTo(target) == 0;
    }



    /**
     * Returns the size of the dictionary.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the max level of the dictionary, counting the original one.
     */
    public int maxLevel() {
        int lvl = head.nexts.size();

        while (head.nexts.get(lvl - 1) == null) {
            lvl--;
        }

        return lvl;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> cur = head.nexts.get(0);

        sb.append("head val: ");
        sb.append(head.nexts.get(head.nexts.size() - 1) == null ? null : head.nexts.get(head.nexts.size() - 1).val).append('\n');

        if (cur != null) {
            sb.append('(').append(cur.val.toString()).append(',').append(cur.nexts.size()).append(')');
            sb.append(' ').append(cur.nexts.get(cur.nexts.size() - 1) == null ? null : cur.nexts.get(cur.nexts.size() - 1).val).append('\n');
            cur = cur.nexts.get(0);
        }

        while (cur != null) {
            sb.append(',');
            sb.append('(').append(cur.val.toString()).append(',').append(cur.nexts.size()).append(')');
            sb.append(' ').append(cur.nexts.get(cur.nexts.size() - 1) == null ? null : cur.nexts.get(cur.nexts.size() - 1).val).append('\n');
            cur = cur.nexts.get(0);
        }

        return sb.toString();
    }

    /******************************************************************************
    * Testing                                                                     *
    ******************************************************************************/

    public static void main(String[] args)
    {
        /* This will print the same dictionary on each run */
        Dictionary<Integer> testList = new Dictionary<Integer>(15);
        for(int k = 1; k <=10;k ++) {
            testList.insert(k);
            debug("maxLevel: " + testList.maxLevel());
            debug(testList.toString());
        }

        debug("size: " + testList.size());
        debug("==========================================================");
        debug("contains 11: " + testList.contains(11));
        debug("contains 1: " + testList.contains(1));

        debug("delete 11: " + testList.delete(11));
        debug("delete 1: " + testList.delete(1));


        debug(testList.toString());

        debug("delete 10: " + testList.delete(10));
        debug(testList.toString());
        debug("contains 10: " + testList.contains(10));
        debug("maxLevel: " + testList.maxLevel());// print (value, level)
/*
10
8
(1,3)(2,1)(3,2)(4,1)(5,1)(6,2)(7,2)(8,3)(9,1)(10,8)
*/
    }

    private static class Node<E> {
        private E val;
        private List<Node<E>> nexts;

        private Node(E val) {
            this.val = val;
            this.nexts = new ArrayList<Node<E>>();
        }
    }

}
