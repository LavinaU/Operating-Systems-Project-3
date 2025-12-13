package src;

import java.nio.ByteBuffer;

public class BTreeNode {

    public static final int DEGREE = 10; // minimum degree
    public static final int MAX_KEYS = 2 * DEGREE - 1; //19 keys
    public static final int MAX_CHILDREN = 2 * DEGREE;   // 20 child pointers
    public static final int BLOCK_SIZE = 512;

    public long blockId;
    public long parentBlockId;
    public int numKeys;
    private long[] keys;
    private long[] values;
    private long[] children ; //block IDs of childre, 0 if leaf

    // constructor for new node
    public BTreeNode(long blockId, long parentBlockId) {
        this.blockId = blockId;
        this.parentBlockId = parentBlockId;
        this.numKeys = 0;
        this.keys = new long[MAX_KEYS];
        this.values = new long[MAX_KEYS] ;
        this.children = new long[MAX_CHILDREN];

        //initialize the children to 0
        for (int i = 0; i < MAX_CHILDREN; i++) {
            children[i] = 0;
        }
    }

    // set a key/value at an index that's given
    public void setKeyValue(int index, long key, long value) {
        if (index < 0 || index >= MAX_KEYS) {
            throw new IndexOutOfBoundsException("Invalid key index");
        }
        keys[index] = key;
        values[index] = value;
        numKeys++;
    }

    public int getNumKeys() {
        return numKeys;
    }

    public long getBlockId() {
        return blockId;
    }

    public long getParentBlockId() {
        return parentBlockId;
    }

    // convert the node into a 512 byte arr for writing to the disk
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);

        buffer.putLong(blockId);
        buffer.putLong(parentBlockId);
        buffer.putLong(numKeys);

        //write keys (19*8 bytes = 152 bytes)
        for (int i = 0; i < MAX_KEYS; i++) {
            buffer.putLong(keys[i]);
        }

        // write values (19*8 bytes = 153 bytes)
        for (int i = 0; i < MAX_KEYS; i++) {
            buffer.putLong(values[i]);
        }

        // write children (20*8 bytes = 160 bytes)
        for (int i = 0; i < MAX_CHILDREN; i++) {
            buffer.putLong(children[i]);
        }

        // the remaining bytes are unused (shld be 512 bytes)
        return buffer.array();
    }


}
