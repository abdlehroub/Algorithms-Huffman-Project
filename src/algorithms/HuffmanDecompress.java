package algorithms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HuffmanDecompress {

	public File decompress(File compressed) throws IOException {
		File newFile = null;
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(compressed))) {
			/*
			 * De-compress Function That Take .huf Files and return it to the original First
			 * read the header has this
			 * format:[ExtensionLength][ExtensionChars][HeaderSize][DataSize][HuffmanTree]
			 * Huffman Tree Written in the .huf file with PreOrder (Root->left->Right) if
			 * Leaf -> 1 else -> 0 After Build The Tree Read The Data And Translate The
			 * Codes By Track The Tree
			 */

			// Extract ExtensionLength In Bits as int
			int extensionLength = readIntFromStream(in);

			// Extract ExtensionChars as bytes and then convert it to String to use it in
			// the path
			byte[] extensionBytes = in.readNBytes(extensionLength);
			String extension = new String(extensionBytes);

			// Extract HeaderLength & DataLength In Bits as int
			int headerSize = readIntFromStream(in);
			int dataSize = readIntFromStream(in);

			// Extract The Tree Bits bit by bit or byte by by depends on the bit is flag or
			// char
			BitReader br = new BitReader(in);
			TNode tree = buildTreePreOrder(br);

			// Create The File And Its Path with The Original Extension
			String filePath = (compressed.getAbsolutePath().substring(0,
					compressed.getAbsolutePath().lastIndexOf("\\") + 1));
			newFile = new File(filePath + compressed.getName().substring(0, compressed.getName().lastIndexOf('.')) + "."
					+ extension);

			// Compress The File Using The Uploaded Header
			decompressData(br, tree, dataSize, newFile);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Error: File Not Found");
		} catch (IOException e) {
			throw e;
		}

		return newFile;
	}

	/*
	 * Function To Build Back The Huffman Tree From The Header Of The Hufman File
	 * Functionality: First Read The Flag Bit (isLeaf), Then If Leaf Read The Byte
	 * Of Its Char and Return The Leaf, Else Go To The Left Then To The Right And
	 * Complete Build The Tree Recursively When Reach The Last 9 bits Thats mean its
	 * the last Leaf in the tree then the tree stop reading by itself EXAMPLE: 0 1a
	 * 0 2c 2d Start with 0 -> Branch then Call The left a and right is branch so
	 * call its left c and right d -> recursion stop 
	 * br 
	 * /\ 
	 * a br
	 *   /\ 
	 *   c d
	 */
	TNode buildTreePreOrder(BitReader br) throws IOException {
		int flag = br.readBit();

		// Base Case "Leaf"
		if (flag == 1) {
			char c = (char) br.readByte();
			return new TNode(c);
		} else {
			// Go to right and left of the root
			TNode left = buildTreePreOrder(br);
			TNode right = buildTreePreOrder(br);
			return new TNode(left, right);
		}
	}

	/*
	 * De-compress Function: After Extract Header Apply this function using the
	 * header data to decode the payload of the .huf file by read the payload bit by
	 * bit and track the tree based on the value of the bit and if reach to leaf
	 * print its char in the new file
	 */
	private void decompressData(BitReader br, TNode tree, int dataSize, File outputFile) throws IOException {
		// Using BufferOutputStream, Write The Data Buffer By Buffer Using 8KB Buffer
		// Size
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile), 8 * 1024)) {
			// Start With The Main Root
			TNode current = tree;
			for (int i = 0; i < dataSize; i++) {
				// Read one Bit From The Buffer then check if 0 -> left, 1 -> right
				int bit = br.readBit();
				if (bit == -1) {
					throw new IOException("Unexpected end of file at bit " + i + " of " + dataSize);
				}
				if (bit == 0) {
					current = current.getLeft();
				} else {
					current = current.getRight();
				}
				// If we on leaf Node write its Char on The File
				if (current.isLeaf()) {
					out.write(current.getCharacter());
					current = tree;
				}
			}
			if (current != tree) {
				throw new IOException("Error: incomplete Huffman code");
			}
		}
	}

	/*
	 * Function To Read int From The Header, Since The BufferedInputStream Read byte
	 * by byte We read 4 bytes (size of int) and in each byte add it to int with
	 * shift right 8 bits and Apply (or) bitwise operation.
	 */
	private int readIntFromStream(BufferedInputStream in) throws IOException {
		byte[] bytes = in.readNBytes(4);
		int result = 0;
		for (int i = 0; i < bytes.length; i++) {
			// 0xFF == 0b11111111 and if int -> 24 0's then 8 1's Anding it with byte read
			// to Avoid Sign issue since we store results in a frequency array.
			result = (result << 8) | (bytes[i] & 0xFF);
		}
		return result;
	}

}
