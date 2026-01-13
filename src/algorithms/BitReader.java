package algorithms;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BitReader {

    private BufferedInputStream in;
    private int currByte;
    private int bitsRemaining;

    public BitReader(File file) throws IOException {
        in = new BufferedInputStream(new FileInputStream(file));
        currByte = 0;
        bitsRemaining = 0;
    }

    public BitReader(BufferedInputStream in) throws IOException {
        this.in = in;
        currByte = 0;
        bitsRemaining = 0;
    }

    public int readBit() throws IOException {
        if (bitsRemaining == 0) {
            currByte = in.read();
            if (currByte == -1)
                return -1;
            bitsRemaining = 8;
        }
        bitsRemaining--;
        return (currByte >> bitsRemaining) & 1;
    }


    public int readByte() throws IOException {
        int value = 0;
        for (int i = 0; i < 8; i++) {
            int bit = readBit();
            if (bit == -1)
                return -1;
            value = (value << 1) | bit;
        }
        return value;
    }


    public int readInt() throws IOException {
        int value = 0;
        for (int i = 0; i < 32; i++) {
            int bit = readBit();
            if (bit == -1)
                throw new IOException("Error: There is no Integer at The Last Of The File");
            value = (value << 1) | bit;
        }
        return value;
    }

    public void close() throws IOException {
        in.close();
    }
}
