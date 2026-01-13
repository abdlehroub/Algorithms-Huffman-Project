package algorithms;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HuffmanCompress {
	private int freq[];
	private MinHeap<TNode> hufHeap;
	private TNode tree;
	private String codes[];
	private String fileExtension;
	private int notEmptyChar;
	private File compressed;
	private BitWriter bw;
	private int headerSize;
	private int dataSize;
	private int []codeBits;
	private byte []codeLen;

	public HuffmanCompress() {
		freq = new int[256];
		codes = new String[256];
		codeBits = new int[256];
		codeLen = new byte[256];

	}

	public File compress(File file) throws IOException {
		if (!file.exists()) {
			throw new FileNotFoundException("Error: File Not Found!");
		}

		
		// Extract the file name and validate that the name is valid
		String fileName = file.getName();
		int indexOFDot = fileName.lastIndexOf('.');
		if (indexOFDot == 0 || indexOFDot == -1 || indexOFDot == fileName.length() - 1) {
			throw new IOException("Error: Invalid File Extention");
		}
		// Extract the file extension from the file name
		fileExtension = fileName.substring(indexOFDot + 1);
		String filePath = (file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\") + 1));
		compressed = new File(filePath + file.getName().substring(0, indexOFDot) + ".huf");
		// Create BitWriter to write bits on it 
		bw = new BitWriter(compressed);

		/* Call The Functions That Create The compressed File:
		[Read Freq] -> [BuildTree] -> [Generate Codes From Tree] -> [Build Header] -> [Compress]
		*/
		readFreq(file);
		buildTree();
		if (hufHeap.getSize() == 0) {
			throw new IOException("Error: Empty file cannot be compressed!");
		}
		tree = hufHeap.extractMin();
		generateCodes(tree, 0,0);
		generateCodes(tree, "");
		buildHeader(tree);
		readForCompress(file);

		return compressed;

	}

	private void buildHeader(TNode tree) throws IOException {
		// Header format:
		// [extLength][extension][headerBits][dataBits][tree]
		int lengthOFExtention = fileExtension.length();
		dataSize = calculateFileBits();
		headerSize = fileExtension.length() * 8 // Length of Extension
				+ 32 // Length of Length of Extension
				+ calculateTreeBits(tree) // Length of tree bits
				+ 32 // Length of Length of Header
				+ 32; // Length of Length of Data
		// Write The Header Data Using Buffer Writer
		bw.writeInt(lengthOFExtention); 
		bw.write(fileExtension);
		bw.writeInt(headerSize);
		bw.writeInt(dataSize);
		treeInHeader(tree);

	}

	/*
	 * Calculate The File Size Depends On The  of each byte 
	 */
	private int calculateFileBits() {
		int size = 0;
		for (int i = 0; i < codes.length; i++) {
			if (codes[i] != null) {
				size += codes[i].length() * freq[i];
			}
		}
		return size;
	}
	
	/*
	 * Calculate The Tree Bits Length in The header
	 * if Leaf -> 1 for flag and 8 for byte
	 * if Branch -> 1 for flag
	 * Complete all the tree recursively
	 */
	private int calculateTreeBits(TNode tree) {
		if (tree == null)
			return 0;
		if (tree.getLeft() == null && tree.getRight() == null) {
			return 1 + 8;
		}
		return 1 + calculateTreeBits(tree.getLeft()) + calculateTreeBits(tree.getRight());
	}

	/*
	 * Write The tree in header using PreOrder traversal 
	 */
	private void treeInHeader(TNode tree) throws IOException {
	    if (tree == null)
	        return;
	    if (tree.isLeaf()) {
	    	// if leaf then write the flag then char and stop
	        bw.writeCode(1, 1);  // flag = 1
	        bw.writeCode(tree.getCharacter(), 8);  
	    } else {
	    	// if branch then write the flag only and go to the children
	        bw.writeCode(0, 1);  // flag = 0
	        treeInHeader(tree.getLeft());
	        treeInHeader(tree.getRight());
	    }
	}

	// Build Tree In Order Traversal to Display it in readable format
	private StringBuilder treePreOrder(TNode tree) throws IOException {
		if (tree == null)
			return new StringBuilder();
		StringBuilder res = new StringBuilder();
		char c = tree.getCharacter();
		String s = c + "";
		if (tree.getLeft() == null && tree.getRight() == null) {
			if ((byte) c == 32)
				s = "Space";
			if ((byte) c == 9)
				s = "HT";
			if ((byte) c == 10)
				s = "LF";
			if ((byte) c == 13)
				s = "CR";
			res.append("1" + s);
		} else {
			res.append("0");
			res.append(treePreOrder(tree.getLeft()));
			res.append(treePreOrder(tree.getRight()));

		}
		return res;
	}

	// Generate Codes In Readable Format
	private void generateCodes(TNode node, String code) {
		if (node == null)
			return;
		if (node.getLeft() == null && node.getRight() == null) {
			codes[node.getCharacter()] = code.isEmpty() ? "0" : code;
		}
		generateCodes(node.getLeft(), code + "0");
		generateCodes(node.getRight(), code + "1");

	}
	
	// Generate Codes in integers and select the length to know who's bits represent the code
	private void generateCodes(TNode node, int bits, int length) {
	    if (node == null) return;

	    if (node.isLeaf()) {
	        codeBits[node.getCharacter()] = bits;
	        codeLen[node.getCharacter()] = (byte) length;
	        return;
	    }
	    // In generate code each transform from parent to children increase the length by one and shift on bit to right 
	    generateCodes(node.getLeft(),  bits << 1,     length + 1);
	    generateCodes(node.getRight(), (bits << 1) | 1, length + 1);
	}
	private void readForCompress(File file) {
	    int bufferSize = 8 *1024;
	    /*
	     * JVM 8KB in Ram -> OS Read 4KB From The Hardisk To The Ram Using Direct Memory Access
	     * We Dont Have Control on the bus OS and DMA have the control 
	     */
	    try (BufferedInputStream in =
	            new BufferedInputStream(new FileInputStream(file), bufferSize)) {

	        byte[] buffer = new byte[bufferSize];
	        int bytesRead;

	        while ((bytesRead = in.read(buffer)) != -1) {
	            for (int i = 0; i < bytesRead; i++) {
	                int b = buffer[i] & 0b11111111;
	                bw.writeCode(codeBits[b], codeLen[b]);
	            }
	        }

	        bw.close();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	public String ReadableHeader() throws IOException {
		String s = "File Extension Length: " + fileExtension.length() + "\nFile Extension: " + fileExtension
				+ "\nHeader Size: " + headerSize + "\nData Size: " + dataSize
				+ "\nTree In Pre Order (1 and char if leaf ): "+treePreOrder(tree) ;
		return s;
	}

	private void buildTree() {
		hufHeap = new MinHeap<TNode>(notEmptyChar);
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0) {
				hufHeap.insert(new TNode((char) i, freq[i]));
			}
		}

		while (hufHeap.getSize() > 1) {
			TNode f = hufHeap.extractMin();
			TNode s = hufHeap.extractMin();
			TNode merge = new TNode('+', f.getFreq() + s.getFreq(), f, s);
			hufHeap.insert(merge);
		}
	}

	private void readFreq(File file) {
		try {
			int buffSize = 8 *1024;
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), buffSize);
			int byteReaded = 0;
			while ((byteReaded = in.read()) != -1) {
				if (freq[byteReaded] == 0) {
					//To Initialize Heap
					notEmptyChar++;
				}
				freq[byteReaded]++;
			}
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int[] getFreq() {
		return freq;
	}

	public void setFreq(int[] freq) {
		this.freq = freq;
	}

	public String[] getCodes() {
		return codes;
	}

	public void setCodes(String[] codes) {
		this.codes = codes;
	}

	public int getHeaderSize() {
		return headerSize;
	}

	public void setHeaderSize(int headerSize) {
		this.headerSize = headerSize;
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

}
