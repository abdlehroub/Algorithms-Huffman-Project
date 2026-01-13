package algorithms;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitWriter {
	File file;
	int currByte = 0;
	byte filledBits = 0;
	BufferedOutputStream out;

	public BitWriter(File file) throws FileNotFoundException {
	    this.file = file;
	    out = new BufferedOutputStream(new FileOutputStream(file));
	}

	public void writeInt(int number) throws IOException {
	    if (filledBits > 0) {
	        throw new IllegalStateException(
	            "Cannot write integer with unflushed bits");
	    }
	    out.write((number >> 24) & 0xFF);
	    out.write((number >> 16) & 0xFF);
	    out.write((number >> 8) & 0xFF);
	    out.write(number & 0xFF);
	}

	public void write(char s) throws IOException {
		flushBits();
		out.write(s);
	}

	public void write(String s) throws IOException {
		flushBits();
		for (int i = 0; i < s.length(); i++) {
			out.write(s.charAt(i));

		}
	}

	public void writeCode(int bits, int length) throws IOException {
	    for (int i = length - 1; i >= 0; i--) {
	        int bit = (bits >> i) & 1;
	        currByte = (currByte << 1) | bit;
	        filledBits++;
	        if (filledBits == 8) {
	            out.write(currByte);
	            currByte = 0;
	            filledBits = 0;
	        }
	    }
	}


	public void flushBits() throws IOException {
		if (filledBits > 0) {
			currByte <<= (8 - filledBits);
			out.write(currByte);
			currByte = 0;
			filledBits = 0;
		}
	}

	public void close() {
		try {
			if (filledBits > 0) {
				currByte = currByte << (8 - filledBits);
				out.write(currByte);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
