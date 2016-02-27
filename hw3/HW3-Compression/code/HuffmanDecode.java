/**
 ******************************************************************************
 *                    HOMEWORK     15-351/650      COMPRESSION
 ******************************************************************************
 *
 *   This implements the Huffman decoding algorithm
 *
 *
 * @author
 * @date
 *****************************************************************************/
 /*****************************************************************************

                         IMPLEMENT THIS CLASS

 *****************************************************************************/

import java.io.*;
import java.util.*;


public class HuffmanDecode
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

	/**
	*  Reads-in the input file, decodes it and writes to the output file
	*/
	public void decode(BitReader reader, BitWriter writer)
	{
		if (reader.length() == 0) return;

		HuffmanTree(reader);
		int fileBytes = 0;
		try{ fileBytes = reader.readInt(); } catch(EOFException e) {}

		for (int i = 0; i < fileBytes; i++)
			writer.writeByte((byte) decode(reader));

		writer.flush();
	}


	/**
	* Reads the header from a compressed file and builds the Huffman tree for decoding.
	*
	*/
	public void HuffmanTree(BitReader br)
	{
		root = buildTree(br);
	}

	private HuffmanNode buildTree(BitReader br) {
		// if I'm a leaf
		boolean isLeaf = br.readBit() == LEAF;

		// if I am, return a leaf node
		if (isLeaf) {
			return new HuffmanNode((byte)br.readByte(), 0);
		}

		// if not, I must have 2 children
		HuffmanNode l = buildTree(br);
		HuffmanNode r = buildTree(br);

		return new HuffmanNode(l, r);
	}

	/**
	 * This method reads bits from the reader and traverse the Huffman tree
	 * to get the value stored in a leaf.
	 */
	public Byte decode (BitReader r)
	{
		boolean downLeft;
		HuffmanNode cur = root;

		while (!cur.isLeaf()) {
			downLeft = r.readBit() == 0;

			if (downLeft) {
				cur = cur.getLeft();
			}
			else {
				cur = cur.getRight();
			}
		}

		return cur.getValue();
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
