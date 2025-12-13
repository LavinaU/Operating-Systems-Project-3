package src;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Project3 {


    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Usage: java Project3 <command> [args]");
            return;
        }

        String command = args[0].toLowerCase();

        try {
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
                    if (args.length < 3) {
                        System.out.println("Usage: java Project3 search <filename> <key>");
                        return;
                    }
                    try {
                        long keyToFind = Long.parseLong(args[2]);
                        search(args[1], keyToFind); // call the actual search method
                    } catch (Exception e) {
                        System.err.println("Search failed: " + e.getMessage());
                    }
                    break;

                case "load":
                    if (args.length < 3) {
                        System.out.println("Usage: java Project3 load <filename> <input.csv>");
                        return;
                    }
                    load(args[1], args[2]);
                    break;

                case "print":
                    if (args.length < 2) {
                        System.out.println("Usage: java Project3 print <filename>");
                        return;
                    }
                    print(args[1]);
                    break;

                case "extract":
                    if (args.length < 3) {
                        System.out.println("Usage: java Project3 extract <filename> <output.csv>");
                        return;
                    }
                    extract(args[1], args[2]);
                    break;

                default:
                    System.out.println("This is an unknown command!: " + command);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } //COMMENTING OUT TRY CATCH FOR NOW BC ITS CAUSING AN ERROR
    }

    private static void createIndexFile(String filename) throws Exception {
        File f = new File(filename);
        if (f.exists()) {
            System.out.println("Error!! File already exists!");
            return;
        }

        BTreeFile btFile = new BTreeFile(filename , "rw");
        Header header = new Header();
        btFile.writeBlock(0, header.toBytes()); // header is block 0
        btFile.close();
        System.out.println("Index file '" + filename + "'  was created successfully!!");
    }

    private static void insert(String filename, long key, long value) throws Exception {
        BTreeFile btFile = new BTreeFile(filename, "rw");
        Header header = Header.fromFile(btFile);

        long blockId = header.getNextBlockId();
        long rootId = header.getRootBlockId();
        if (rootId == 0) {
            long newId = header.getNextBlockId();
            BTreeNode root = new BTreeNode(newId, 0);
            root.insertIntoLeaf(key, value);
            btFile.writeBlock(newId, root.toBytes());

            header.setRootBlockId(newId);
            header.setNextBlockId(newId + 1);
            btFile.writeBlock(0, header.toBytes());
            btFile.close();
            System.out.println("Inserted key " + key + " as root");
            return;
        }

        BTreeNode root = BTreeNode.fromBytes(btFile.readBlock(rootId));
        // simplified: only leaf splitting for now
        root.insertIntoLeaf(key, value);

        if (!root.isOverflow()) {
            btFile.writeBlock(rootId, root.toBytes());
        } else {
            long rightId = header.getNextBlockId();
            BTreeNode right = root.splitLeaf(rightId);

            long promotedKey = right.keys[0];
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
        }
        btFile.close();
        System.out.println("Inserted key " + key);

    }

    private static void search(String filename, long key) throws Exception {
        BTreeFile btFile = new BTreeFile(filename, "r");
        Header header = Header.fromFile(btFile);
        if (header.getRootBlockId() == 0) {
            System.out.println("Tree is empty!");
            btFile.close();
            return;
        }
        BTreeNode root = BTreeNode.fromBytes(btFile.readBlock(header.getRootBlockId()));
        Long value = root.searchKey(key, btFile);
        if (value != null) System.out.println("Found key " + key + " with value " + value);
        else System.out.println("Key " + key + " not found");
        btFile.close();
    }

    private static void print(String filename) throws Exception {
        BTreeFile btFile = new BTreeFile(filename, "r");
        Header header = Header.fromFile(btFile);
        if (header.getRootBlockId() == 0) {
            System.out.println("Tree is empty!");
            btFile.close();
            return;
        }
        BTreeNode root = BTreeNode.fromBytes(btFile.readBlock(header.getRootBlockId()));
        List<String> output = new ArrayList<>();
        root.inOrderTraversal(btFile, output);
        for (String s : output) System.out.println(s);
        btFile.close();
    }

    private static void extract(String filename, String outFile) throws Exception {
        BTreeFile btFile = new BTreeFile(filename, "r");
        Header header = Header.fromFile(btFile);
        if (header.getRootBlockId() == 0) {
            System.out.println("Tree is empty!");
            btFile.close();
            return;
        }
        BTreeNode root = BTreeNode.fromBytes(btFile.readBlock(header.getRootBlockId()));
        List<String> output = new ArrayList<>();
        root.inOrderTraversal(btFile, output);
        btFile.close();

        FileWriter writer = new FileWriter(outFile);
        for (String s : output) writer.write(s + "\n");
        writer.close();
        System.out.println("Extracted " + output.size() + " key/value pairs to " + outFile);
    }

    private static void load(String filename, String csvFile) throws Exception {
        BTreeFile btFile = new BTreeFile(filename, "rw");
        Scanner scanner = new Scanner(new File(csvFile));
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(",");
            if (parts.length == 2) insert(filename, Long.parseLong(parts[0]), Long.parseLong(parts[1]));
        }
        scanner.close();
        btFile.close();
        System.out.println("Loaded CSV file: " + csvFile);
    }

}
