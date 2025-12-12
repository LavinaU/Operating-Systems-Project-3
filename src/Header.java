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
        rootBlockId = 0;
        nextBlockId = 1;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);

        buffer.put(MAGIC.getBytes(StandardCharsets.US_ASCII));
        buffer.putLong(rootBlockId);
        buffer.putLong(nextBlockId);

        return buffer.array();
    }

    public static Header fromBytes(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        byte[] magicBytes = new byte[8];
        buffer.get(magicBytes);

        String magic = new String(magicBytes, StandardCharsets.US_ASCII);
        if (!MAGIC.equals(magic)) {
            throw new IllegalArgumentException("Invalid index file");
        }

        Header h = new Header();
        h.rootBlockId = buffer.getLong();
        h.nextBlockId = buffer.getLong();

        return h;
    }


}
