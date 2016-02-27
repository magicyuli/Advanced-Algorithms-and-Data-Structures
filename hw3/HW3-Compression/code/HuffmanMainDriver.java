import java.io.FileNotFoundException;

/**
 ******************************************************************************
 *                    HOMEWORK     15-351/650       COMPRESSION
 ******************************************************************************
 *
 *   The main class for the Huffman compression algorithm
 *
 *
 *****************************************************************************/


public class HuffmanMainDriver
{
	public static void main(String[] args) throws FileNotFoundException
	{
		String inFile = args[0];
		String outFile = inFile + ".txt";
		String interFile = inFile + ".dat";

		HuffmanEncode hc = new HuffmanEncode();
		BitReader reader = new BitReader(inFile);
		BitWriter writer = new BitWriter(interFile);

		hc.encode(reader, writer);

		HuffmanDecode hd = new HuffmanDecode();
		reader = new BitReader(interFile);
		writer = new BitWriter(outFile);

		hd.decode(reader, writer);
	}
}