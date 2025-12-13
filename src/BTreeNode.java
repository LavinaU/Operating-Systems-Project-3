package src;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BTreeNode {

    public static final int DEGREE = 10; // minimum degree
    public static final int MAX_KEYS = 2 * DEGREE - 1; //19 keys
    public static final int MAX_CHILDREN = 2 * DEGREE;   // 20 child pointers
    public static final int BLOCK_SIZE = 512;

    public long blockId;
    public long parentBlockId;
    public int numKeys;

    public long[] keys;
    public long[] values;
    public long[] children ; //block IDs of childre, 0 if leaf

    // constructor for new node
    public BTreeNode(long blockId, long parentBlockId) {
        this.blockId = blockId;
        this.parentBlockId = parentBlockId;
        this.numKeys = 0;
        this.keys = new long[MAX_KEYS];
        this.values = new long[MAX_KEYS] ;
        this.children = new long[MAX_CHILDREN];
        Arrays.fill(children, 0);
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

    public boolean isOverflow() {
        return numKeys > MAX_KEYS;
    }

    // INSERT INTO LEAF SAFELY
    public void insertIntoLeaf(long key, long value) {
        if (numKeys >= MAX_KEYS) {
            // should split instead of inserting directly
            return;
        }

        int i = numKeys - 1;
        while (i >= 0 && keys[i] > key) {
            keys[i + 1] = keys[i];
            values[i + 1] = values[i];
            i--;
        }
        keys[i + 1] = key;
        values[i + 1] = value;
        numKeys++;

    }

    //SPLITS THIS LEAF NODE INTO 2 NODES, RETURNS THE NEW RIGHT NODE
    public BTreeNode splitLeaf(long newBlockId) {
        int mid = (numKeys + 1) / 2; // middle index
        int rightCount = numKeys - mid; // # of keys for the new right node

        BTreeNode right = new BTreeNode(newBlockId, this.parentBlockId);

        System.arraycopy(keys, mid, right.keys, 0, rightCount);
        System.arraycopy(values, mid, right.values, 0, rightCount);
        right.numKeys = rightCount;

        numKeys = mid; // left keeps first half
        return right;
    }

    // helper, finds child index for a key
    public int findChildIndex(long key) {
        for (int i = 0; i < numKeys; i++) {
            if (key < keys[i]) return i;
        }
        return numKeys;
    }

    // search in this node ro descend
    public Long searchKey(long key, BTreeFile file) throws Exception {
        for (int i = 0; i < numKeys; i++) {
            if (keys[i] == key) return values[i];
        }
        // descend if not leaf
        for (int i = 0; i <= numKeys; i++) {
            if (children[i] != 0) {
                BTreeNode child = BTreeNode.fromBytes(file.readBlock(children[i]));
                Long res = child.searchKey(key, file);
                if (res != null) return res;
            }
        }
        return null;
    }

    // in order traversal for printing/extracting
    public void inOrderTraversal(BTreeFile file, List<String> output) throws Exception {
        for (int i = 0; i < numKeys; i++) {
            if (children[i] != 0) {
                BTreeNode child = BTreeNode.fromBytes(file.readBlock(children[i]));
                child.inOrderTraversal(file, output);
            }
            output.add(keys[i] + "," + values[i]);
        }
        if (children[numKeys] != 0) {
            BTreeNode child = BTreeNode.fromBytes(file.readBlock(children[numKeys]));
            child.inOrderTraversal(file, output);
        }
    }


}
