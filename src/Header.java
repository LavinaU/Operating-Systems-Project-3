package src;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

// HEADER CLASS FOR THE BTREE INDEX FILE
public class Header {

    public static final int BLOCK_SIZE = 512;
    public static final String MAGIC = "CS4348P3"; // 8 bytes as per instructions

    // instance fields for in mem header
    public long rootBlockId;
    public long nextBlockId;

    // cinstructor, default tree empty
    public Header() {
        this.rootBlockId = 0; // tree empty
        this.nextBlockId = 1;  // 1st node will be block 1
    }

    // getters & setters
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

    // STATIC HELPERS FOR READING & WRITING HEADER FROM DISK BYTES

    //read nextBlockId from header bytes
    public static long getNextBlockId(byte[] header) {
        ByteBuffer buffer = ByteBuffer.wrap(header);
        buffer.position(16); // skip MAGIC (8 bytes) + rootBlockId (8bytes)
        return buffer.getLong();
    }

    // write nextBlockId into header bytes
    public static void setNextBlockId(byte[] header, long nextId) {
        ByteBuffer buffer = ByteBuffer.wrap(header);
        buffer.position(16); // skip MAGIC + rootblockID
        buffer.putLong(nextId);
    }

    // read root bock ID from header bytes
    public static long getRootId(byte[] header) {
        ByteBuffer buffer = ByteBuffer.wrap(header);
        buffer.position(8); //skip MAGIC
        return buffer.getLong();
    }

    // write root block ID into header bytes
    public static void setRootId(byte[] header, long rootId) {
        ByteBuffer buffer = ByteBuffer.wrap(header);
        buffer.position(8);
        buffer.putLong(rootId);
    }

}
