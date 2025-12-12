package src;

public class BTreeNode {

    public static final int DEGREE = 10;
    public static final int MAX_KEYS = 2 * DEGREE - 1;

    public long blockId;
    public long parentBlockId;
    public int numKeys;

    public long[] keys = new long[MAX_KEYS];
    public long[] values = new long[MAX_KEYS];
    public long[] children = new long[MAX_KEYS + 1];

    public BTreeNode() {
        numKeys = 0;
        blockId = 0;
        parentBlockId = 0;
    }

    // SERIALIZATION TO BE ADDED


}
