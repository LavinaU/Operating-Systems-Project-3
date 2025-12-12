package src;

import java.io.IOException;
import java.io.RandomAccessFile; //

public class BTreeFile {

    private RandomAccessFile file;

    public BTreeFile(String filename, String mode) throws IOException {
        file = new RandomAccessFile(filename, mode);
    }

    public void writeBlock(long blockId, byte[] data) throws IOException {
        file.seek(blockId * Header.BLOCK_SIZE);
        file.write(data);
    }

    public byte[] readBlock(long blockId) throws IOException {
        byte[] data = new byte[Header.BLOCK_SIZE];
        file.seek(blockId * Header.BLOCK_SIZE);
        file.readFully(data);
        return data;
    }

    public void close() throws IOException {
        file.close();
    }


}
