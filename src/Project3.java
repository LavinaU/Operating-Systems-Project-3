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
                        System.out.println("Usage: java Project3 create <filename>");
                        return;
                    }
                    createIndexFile(args[1]);
                    break;

                case "insert":
                    System.out.println("Insert command (TO BE IMPLEMENTED!!t)");
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



}
