/**
 ******************************************************************************
 *                    HOMEWORK     115-351/650       COMPRESSION
 ******************************************************************************
 *
 *   This implements the LZW algorithm
 *
 *
 * @author Liruoyang Yu : liruoyay
 * @date Feb. 26, 2016
 *****************************************************************************/
 /*****************************************************************************

                         IMPLEMENT THIS CLASS

 *****************************************************************************/

import java.io.*;
import java.util.*;


public class LZW
{

    /** The number of bits in each LZW code */
    public static final int BIT_WIDTH = 16;
    public static final int HALF_WIDTH_MASK = (1 << (BIT_WIDTH / 2)) - 1;

    /** The maximum number of codes in the dictionary */
    public static final int MAX_SIZE = (1 << BIT_WIDTH) - 1;

    public static final int ALPHABET_SIZE = 256;

    /**
    *  Reads-in the input file, compresses it and writes to the output file
    */
    public void encode(BitReader in, BitWriter out)
    {
        DictionaryTrie dict = new DictionaryTrie();
        int dictSize = ALPHABET_SIZE;
        int len = in.length();
        int prevVal = 0;
        boolean inDict;
        byte b;

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            dict.travel((byte)i);
            dict.end(i);
        }

        for (int i = 0; i < len; i++) {
            b = (byte)in.readByte();

            inDict = dict.travel(b);

            if (!inDict) {
                dict.end(dictSize++);

                out.writeBits(prevVal, BIT_WIDTH);

                dict.travel(b);
            }
            
            prevVal = dict.getValue();
        }

        out.writeBits(prevVal, BIT_WIDTH);

        out.flush();
    }

    /**
    *  Reads-in the input file, decodes it and writes to the output file
    */
    public void decode(BitReader in, BitWriter out)
    {
        Map<Integer, List<Byte>> dict = new HashMap<Integer, List<Byte>>();
        List<Byte> l = null;
        int len = in.length();
        int key;
        int dictSize = ALPHABET_SIZE;

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            l = new ArrayList<Byte>();
            l.add((byte)i);

            dict.put(i, l);
        }

        l = null;

        for (int i = 0; i < len / 2; i++) {
            key = in.readByte() & HALF_WIDTH_MASK;
            key <<= 8;
            key |= in.readByte() & HALF_WIDTH_MASK;

            // System.out.printf("%x %s\n", key, dict.containsKey(key));

            if (!dict.containsKey(key)) {
                l = new ArrayList<Byte>(l);
                l.add(l.get(0));
                dict.put(dictSize++, l);
                // System.out.printf("%x %d\n", key, i);
            }
            else if (l != null) {
                l = new ArrayList<Byte>(l);
                l.add(dict.get(key).get(0));
                System.out.printf("%x %d ", dictSize, i);
                for (byte b : l) {
                    System.out.printf("%s", (char)b);
                }
                System.out.printf("\n");
                dict.put(dictSize++, l);
            }

            l = dict.get(key);

            for (byte b : l) {
                out.writeByte(b);
            }
        }        

        out.flush();
    }

   public static void main(String[] args) throws FileNotFoundException
   {
      BitReader reader = new BitReader("LZW.java");
      BitWriter writer = new BitWriter("LZW.dat");
      LZW lzw = new LZW();
      lzw.encode(reader, writer);

      reader = new BitReader("LZW.dat");
      writer = new BitWriter("LZW.txt");
      lzw = new LZW();
      lzw.decode(reader, writer);
   }
}
