/**
 ******************************************************************************
 *                    HOMEWORK  15-351/650
 ******************************************************************************
 *
 * This class implements the dictionary trie for the Boggle
 * application.  Objects of this class are used by the GUI
 * to handle text input.  The responses include returning
 * whether or not a word is valid.
 *
 *
 * @author Liruoyang Yu. Andrew Id: liruoyay
 * @date 02/05/2016
 *****************************************************************************/
 /*****************************************************************************

               IMPLEMENT THIS CLASS

 *****************************************************************************/


import java.util.*;
import java.io.*;


public class DictionaryTrie implements TrieInterface
{
    private static final char DUMMY = (char)0;
    private static final int BOARD_SIZE = 4;

    private int dictSize;
    private TrieNode root;
    private Set<String> solutions;
    /**
    *  Creates a Trie object with the given dictionary.
    *
    *  @param dictionaryName the name of the dictionary file
    */
    public DictionaryTrie(String dictionaryName)
    {
        dictSize = 0;
        root = new TrieNode();
        solutions = new HashSet<String>();

        Scanner in = null;
        try {
            in = new Scanner(new FileInputStream(new File(dictionaryName)));
            while (in.hasNext()) {
                add(in.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
    *  Prunes a the set of strings possible on a given
    *  board by using BFS on the dictionary trie.
    *
    *  @param board the current game board
    */
    public void boardBFS(String[] board)
    {
        solutions.clear();
        char[][] realBoard = new char[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < board.length; i++) {
            realBoard[i / BOARD_SIZE][i % BOARD_SIZE] = board[i].toUpperCase().charAt(0);
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                findSolutions(realBoard, i, j, root.next(realBoard[i][j]), realBoard[i][j] + "");
            }
        }
    }

    private void findSolutions(char[][] board, int curI, int curJ, TrieNode cur, String res) {
        if (cur == null) {
            return;
        }

        if (cur.end && res.length() >= 3) {
            solutions.add(res);
        }

        char mem = board[curI][curJ];
        board[curI][curJ] = DUMMY;

        int nextI;
        int nextJ;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                nextI = curI + i;
                nextJ = curJ + j;

                if (nextI >= 0 && nextI < BOARD_SIZE 
                    && nextJ >= 0 && nextJ < BOARD_SIZE
                    && board[nextI][nextJ] != DUMMY) {
                    findSolutions(board, nextI, nextJ, cur.next(board[nextI][nextJ]), 
                        res + board[nextI][nextJ]);
                }
            }
        }

        board[curI][curJ] = mem;
    }


    /**
    * Inserts a string into the trie.
    *
    * @param s the string to check
    */
    public void add(String s)
    {
        TrieNode cur = root;
        TrieNode next;

        s = s.toUpperCase();

        for (char c : s.toCharArray()) {
            if ((next = cur.next(c)) == null) {
                next = cur.grow(c);
            }
            cur = next;
        }

        // if (!cur.end) {
            cur.end = true;
            dictSize++;
        // }
    }

    /**
    *  Checks to see if a string is in the trie.  Also marks this
    *  string for later use by the valid method.
    *
    *  @param s the string to check
    *  @return true if the string is in the trie, false otherwise
    */
    public boolean inDictionary(String s)
    {
        TrieNode cur = root;

        s = s.toUpperCase();
        
        for (char c : s.toCharArray()) {
            cur = cur.next(c);
            if (cur == null) {
                return false;
            }
        }

        return cur.end;
    }

    /**
    *  Checks to see if a string is both in the trie
    *  and a valid word on the game board.  This method is
    *  only guaranteed to work after calling boardBFS.
    *
    *  @param s the string to check
    *  @return true if the string is valid, false otherwise
    */
    public boolean valid(String s)
    {
        s = s.toUpperCase();

        return solutions.contains(s);
    }

    /**
    *  Returns a collection of all the solutions for this
    *  given board.
    *  Only guaranteed to work after calling boardBFS.
    *
    *  @return all the solutions for this board
    */
    public ArrayList<String> allSolutions()
    {
        return new ArrayList<String>(solutions);
    }

    /**
    *  Returns the number of elements read into the trie from
    *  the intial dictionary.
    *
    *  @return the size
    */
    public int size()
    {
        return dictSize;
    }

    private static class TrieNode {
        private static final char FIRST_CHAR = 'A';

        private TrieNode[] chl;
        private boolean end;

        private TrieNode() {
            chl = new TrieNode[26];
            end = false;
        }

        private TrieNode next(char c) {
            return chl[c - FIRST_CHAR];
        }

        private TrieNode grow(char c) {
            TrieNode newChl = new TrieNode();
            chl[c - FIRST_CHAR] = newChl;
            return newChl;
        }
    }
}
