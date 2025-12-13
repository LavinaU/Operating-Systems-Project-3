package src;

import java.nio.ByteBuffer;

public class BTreeNode {

    public static final int DEGREE = 10; // minimum degree
    public static final int MAX_KEYS = 2 * DEGREE - 1; //19 keys
    public static final int MAX_CHILDREN = 2 * DEGREE;   // 20 child pointers

    public long blockId;
    public long parentBlockId;
    public int numKeys;
    private long[] keys;
    private long[] values;
    private long[] children ; //block IDs of childre, 0 if leaf

    public BTreeNode(long blockId, long parentBlockId) {
        this.blockId = blockId;
        this.parentBlockId = parentBlockId;
        this.numKeys = 0;
        this.keys = new long[MAX_KEYS];
        this.values = new long[MAX_KEYS] ;
        this.children = new long[MAX_CHILDREN];
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(Header.BLOCK_SIZE);
        buffer.putLong(blockId);
        buffer.putLong(parentBlockId);
        buffer.putLong(numKeys);
        for (int i = 0; i < MAX_KEYS; i++) buffer.putLong(keys[i]);
        for (int i = 0; i < MAX_KEYS; i++) buffer.putLong(values[i]);
        for (int i = 0; i < MAX_CHILDREN; i++) buffer.putLong(children[i]);
        return buffer.array();
    }


}
