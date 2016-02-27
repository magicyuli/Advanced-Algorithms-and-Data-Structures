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
		HuffmanEncode hc = new HuffmanEncode();
		BitReader reader = new BitReader("HuffmanMainDriver.java");
		BitWriter writer = new BitWriter("HuffmanMainDriver.dat");

		hc.encode(reader, writer);

		HuffmanDecode hd = new HuffmanDecode();
		reader = new BitReader("HuffmanMainDriver.dat");
		writer = new BitWriter("HuffmanMainDriver.txt");

		hd.decode(reader, writer);
	}
}