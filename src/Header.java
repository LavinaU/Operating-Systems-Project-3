package src;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

// SKELETON FOR THE PROJ

public class Header {

    public static final int BLOCK_SIZE = 512;
    public static final String MAGIC = "CS4348P3"; // 8 bytes as per instructions

    public long rootBlockId;
    public long nextBlockId;

    public Header() {
        this.rootBlockId = 0; // tree empty
        this.nextBlockId = 1;  // 1st node will be block 1
    }

    public long getRootBlockId() {
        return rootBlockId;
    }

    public void setRootBlockId(long rootBlockId) {
        this.rootBlockId = rootBlockId;
    }

    public long getNextBlockId() {
        return nextBlockId;
    }

    public void setNextBlockId(long nextBlockId) {
        this.nextBlockId = nextBlockId;
    }

    // serialize header to 512 bytes bc im from austin
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);

        buffer.put(MAGIC.getBytes(StandardCharsets.US_ASCII)); // 8 bytes
        buffer.putLong(rootBlockId); // 8 bytes
        buffer.putLong(nextBlockId); // 8 bytes

        //remaining bytes r unused
        return buffer.array();
    }


}
