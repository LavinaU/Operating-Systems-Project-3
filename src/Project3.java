package src;

import java.io.File;
import java.io.IOException;

public class Project3 {


    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Usage: java Project3 <command> [args]");
            return;
        }

        String command = args[0].toLowerCase();

        //try {
            switch (command) {
                case "create":
                    if (args.length < 2) {
                        System.out.println("Usage: java Project3 create <insertFile>");
                        return;
                    }
                    createIndexFile(args[1]);
                    break;

                case "insert":
                    if (args.length < 4) {
                        System.out.println("Usage: java Project3 insert <insertFile> <key> <value>");
                        return;
                    }

                    String indexFile = args[1];
                    long key = Long.parseLong(args[2]);
                    long value = Long.parseLong(args[3]);

                    try {
                        BTreeFile btFile = new BTreeFile(indexFile, "rw");
                        Header header = Header.fromFile(btFile);

                        // CASE 1 - EMPTY TREE
                        if (header.getRootBlockId() == 0) {
                            long rootId = header.getNextBlockId();
                            BTreeNode root = new BTreeNode(rootId, 0);
                            root.insertIntoLeaf(key, value);

                            btFile.writeBlock(rootId, root.toBytes());

                            header.setRootBlockId(rootId);
                            header.setNextBlockId(rootId + 1);
                            btFile.writeBlock(0, header.toBytes());

                            System.out.println("Inserted key " + key + " as root");
                            btFile.close();
                            return;
                        }

                        // CASE 2 - INSERT INTO EXISTING ROOT, LEAF ONLY TREE
                        long rootId = header.getRootBlockId();
                        BTreeNode root = BTreeNode.fromBytes(btFile.readBlock(rootId));
                        root.insertIntoLeaf(key, value);

                        // NO OVERFLOW SO JUST WRITE BACK
                        if (!root.isOverflow()) {
                            btFile.writeBlock(rootId, root.toBytes());
                            System.out.println("Inserted key " + key + " into root");
                            btFile.close();
                            return;
                        }

                        // OVERFLOW SO SPLIT LEAF
                        long rightId = header.getNextBlockId();
                        BTreeNode right = root.splitLeaf(rightId);

                        long promotedKey = right.keys[0];

                        // NEW ROOT
                        long newRootId = rightId + 1;
                        BTreeNode newRoot = new BTreeNode(newRootId, 0);

                        newRoot.keys[0] = promotedKey;
                        newRoot.numKeys = 1;
                        newRoot.children[0] = root.getBlockId();
                        newRoot.children[1] = right.getBlockId();

                        root.parentBlockId = newRootId;
                        right.parentBlockId = newRootId;

                        btFile.writeBlock(root.getBlockId(), root.toBytes());
                        btFile.writeBlock(right.getBlockId(), right.toBytes());
                        btFile.writeBlock(newRootId, newRoot.toBytes());

                        header.setRootBlockId(newRootId);
                        header.setNextBlockId(newRootId + 1);
                        btFile.writeBlock(0, header.toBytes());

                        System.out.println("Inserted key " + key + " with leaf split!");
                        btFile.close();

                    } catch (Exception e) {
                        System.err.println("Insert failed: " + e.getMessage());
                    }

                    break;



                case "search":
                    System.out.println("Search command (TO BE IMPLEMENTED!!)");
                    break;

                case "load":
                    System.out.println("Load command (TO BE IMPLEMENTED!!)");
                    break;

                case "print":
                    System.out.println("Print command (TO BE IMPLEMENTED!!)");
                    break;

                case "extract":
                    System.out.println("Extract command (TO BE IMPLEMENTED!!)");
                    break;

                default:
                    System.out.println("This is an unknown command!: " + command);
            }
        //} catch (IOException e) {
        //    System.err.println("I/O Error: " + e.getMessage());
        //} COMMENTING OUT TRY CATCH FOR NOW BC ITS CAUSING AN ERROR
    }

    private static void createIndexFile(String filename) {
        File f = new File(filename);
        if (f.exists()) {
            System.out.println("Error!! File already exists!");
            return;
        }

        try {
            BTreeFile btFile = new BTreeFile(filename , "rw");
            Header header = new Header();
            btFile.writeBlock(0, header.toBytes()); // header is block 0
            btFile.close();
            System.out.println("Index file '" + filename + "'  was created successfully!!");
        } catch (IOException e) {
            System.err.println("Error creating index file: " + e.getMessage());
        }

    }

    private static void insert(String filename, long key, long value) {

        try {
            BTreeFile btFile = new BTreeFile(filename, "rw");

            Header header = Header.fromFile(btFile);

            long blockId = header.getNextBlockId();

            BTreeNode node = new BTreeNode(blockId, 0);
            node.insertKeyValue(key, value);

            btFile.writeBlock(blockId, node.toBytes());

            if (header.getRootBlockId() == 0) {
                header.setRootBlockId(blockId);
            }

            header.setNextBlockId(blockId + 1);
            btFile.writeBlock(0, header.toBytes());

            btFile.close();

            System.out.println(
                    "Inserted key " + key +
                            " with value " + value +
                            " into block " + blockId
            );

        } catch (Exception e) {
            System.err.println("Insert failed!!: "+ e.getMessage());
        }





    }




}
