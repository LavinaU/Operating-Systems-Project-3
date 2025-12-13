package src;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets; // for letting me specify character encodings when coverting btwn strings & bytes

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
        buffer.putLong(numKeys); // store as long for alignment

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

    // deserialize node from bytes
    public static BTreeNode fromBytes(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        long blockId = buffer.getLong();
        long parentBlockId = buffer.getLong();
        int numKeys = (int) buffer.getLong();

        BTreeNode node = new BTreeNode(blockId, parentBlockId);
        node.numKeys = numKeys;

        for (int i = 0; i < MAX_KEYS; i++) {
            node.keys[i] = buffer.getLong();
        }
        for (int i = 0; i < MAX_KEYS; i++) {
            node.values[i] = buffer.getLong();
        }
        for (int i = 0; i < MAX_CHILDREN; i++) {
            node.children[i] = buffer.getLong();
        }

        return node;

    }

    public void insertKeyValue(long key, long value) {
        keys[numKeys] = key;
        values[numKeys] = value;
        numKeys++;
    }


}
