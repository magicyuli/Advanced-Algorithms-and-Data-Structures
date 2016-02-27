/**
 ******************************************************************************
 *                    HOMEWORK     15-351/650      COMPRESSION
 ******************************************************************************
 *
 *   This implements the Huffman encoding algorithm
 *
 *
 * @author Liruoyang Yu : liruoyay
 * @date Feb. 25, 2016
 *****************************************************************************/
 /*****************************************************************************

                         IMPLEMENT THIS CLASS

 *****************************************************************************/

import java.io.*;
import java.util.*;


public class HuffmanEncode
{
    /** Code bit for a leaf node in file-based tree representation */
   public static final int LEAF = 0;

    /** Code bit for a parent node in file-based tree representation */
   public static final int PARENT = 1;

    /** Code bit for the left child in the file-based tree representation */
   public static final int LEFT = 0;

    /** Code bit for the right child in the file-based tree representation */
   public static final int RIGHT = 1;

    /** the root of the Huffman tree    */
    private HuffmanNode root;

    /** it stores chars and their frequences    */
    private Map<Byte, Integer> freqMap;

    /** it stores bytes and the encoding */
    private Map<Byte, int[]> encoding;

    /**
    *  Reads-in the input file, compresses it and writes to the output file
    */
    public void encode(BitReader reader, BitWriter writer)
    {
        int fileBytes = reader.length();
        if (fileBytes == 0) return;

        freqMap = countFrequencies(reader);

        HuffmanTree(freqMap);
        
        try{ writeHeader(writer); } catch(IOException e) {}
        writer.writeInt(fileBytes);

        reader.reset();

        for (int i = 0; i < fileBytes; i++)
            encode((byte) reader.readByte(), writer);

        writer.flush();
    }


    /**
    * This method takes a item and writes the corresponding codeword.
    * The bits <tt>LEFT</tt> and <tt>RIGHT</tt> are written so that
    * if one takes that path in the Huffman tree they will get to the
    * leaf node representing <tt>item</tt>.
    */
    public void encode(Byte item, BitWriter writer)
    {
        if (encoding == null) {
            encoding = new HashMap<Byte, int[]>();

            int[] code = new int[1000];
            fillEncoding(root, code, 0);
        }

        int[] code = encoding.get(item);

        for (int i : code) {
            writer.writeBit(i);
        }
    }


    /**
    *  Calculates frequences of each character from the ASCII table
    */
    public Map<Byte, Integer> countFrequencies(BitReader reader)
    {
        Map<Byte, Integer> freqMap = new HashMap<Byte, Integer>();
        byte b;

        for (int i = 0; i < reader.length(); i++) {
            b = (byte)reader.readByte();

            if (freqMap.containsKey(b)) {
                freqMap.put(b, freqMap.get(b) + 1);
            }
            else {
                freqMap.put(b, 1);
            }
        }

        return freqMap;
    }

    /**
    * Takes a list of (Byte, Frequency) pairs (here represented as a map)
    * and builds a tree for encoding the data items using the Huffman
    * algorithm.
    */
    public void HuffmanTree(Map<Byte, Integer> map)
    {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<HuffmanNode>(26,
            new Comparator<HuffmanNode>() {
                public int compare(HuffmanNode n1, HuffmanNode n2) {
                    return n1.getFreq() - n2.getFreq();
                }
            });

        for (Map.Entry<Byte, Integer> e : map.entrySet()) {
            pq.offer(new HuffmanNode(e.getKey(), e.getValue()));
        }

        while (pq.size() > 1) {
            pq.offer(new HuffmanNode(pq.poll(), pq.poll()));
        }

        root = pq.poll();
    }

    /**
    * Writes the Huffman tree into a compressed file.
    *
    * The format for the tree is defined recursively. To write
    * the entire tree, you start with the root. When the node
    * is a leaf node, you write the bit <tt>LEAF</tt>
    * and then call the <tt>writeByte</tt> to write the node value.
    * Otherwise, you write the bit <tt>PARENT</tt>, then
    * go to the left and right nodes.
    */
    public void writeHeader(BitWriter writer) throws IOException
    {
        inOrdTrav(root, writer);
    }

    private void inOrdTrav(HuffmanNode cur, BitWriter writer) {
        if (cur.isLeaf()) {
            writer.writeBit(LEAF);
            writer.writeByte(cur.getValue());
        }
        else {
            writer.writeBit(PARENT);
            
            if (cur.getLeft() != null) {
                inOrdTrav(cur.getLeft(), writer);
            }

            if (cur.getRight() != null) {
                inOrdTrav(cur.getRight(), writer);
            }
        }
    }

    private void fillEncoding(HuffmanNode cur, int[] code, int codeLen) {
        if (cur.isLeaf()) {
            encoding.put(cur.getValue(), Arrays.copyOf(code, codeLen));
        }
        else {
            if (cur.getLeft() != null) {
                code[codeLen] = LEFT;

                fillEncoding(cur.getLeft(), code, codeLen + 1);
            }

            if (cur.getRight() != null) {
                code[codeLen] = RIGHT;

                fillEncoding(cur.getRight(), code, codeLen + 1);
            }
        }
    }

    /**
     * For testing purposes
     * DO NOT CHANGE!
     */
    public HuffmanNode getCodeTreeRoot()
    {
        return root;
    }

}
