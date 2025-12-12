package src;

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
                    System.out.println("Create command (TO BE IMPLEMENTED!!)");
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

}
