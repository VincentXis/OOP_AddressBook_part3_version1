package addressBookRoot.commandLineInterface;

import addressBookRoot.addressBookManager.AddressBookManager;
import addressBookRoot.externalCatalogueManager.ExternalCatalogueManager;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLineInterface {

    private static final Logger log = Logger.getLogger(CommandLineInterface.class.getName());
    private boolean run = true;
    private InputCommandHandler ich = new InputCommandHandler();
    private AddressBookManager abm = new AddressBookManager();
    private ExternalCatalogueManager ecm = new ExternalCatalogueManager();

    /**
     * enter enter a valid ip-address, and port-number.
     * "localhost", 6117    will be
     * "localhost", 1618        initial ports
     * "localhost", 61619           on server project side
     */
    private void requestContactsFromServer() {
        new Thread(() -> {
//        ecm.getDataFromExternalSource("localhost", 61616);
            //                   Enter your target ip address, and the port-number here
            ecm.getDataFromExternalSource("localhost", 6117);
            ecm.getDataFromExternalSource("localhost", 1618);
            ecm.getDataFromExternalSource("localhost", 61619);
            // create contacts from received data
            ecm.createContactsFromExternalData();
            // load external contacts into AddressBookManager from ExternalCatalogueManager
            abm.loadExternalCatalogueContacts(ecm.getContactsFromExternalCatalogue());
            System.out.println(ecm.getContactsFromExternalCatalogue().size() + " contacts have been loaded from an external catalogue");
        }).start();
    }

    private Thread autoSave = new Thread(() -> {
        log.info("AutoSave Thread, started.");
        while (run) {
            try {
                Thread.sleep(5_000);
                abm.saveContactList();
            } catch (Exception e) {
                log.log(Level.SEVERE, "Sleep failed");
            }
        }
        log.info("AutoSave Thread, ended.");
        System.out.println("the last processes has finished running,\nready for the main process to finish.\nGood bye.");
    });

    public CommandLineInterface() {
        runCommandLineInterface();
    }

    private String readUserInput() {
        return new Scanner(System.in).nextLine();
    }

    private void runCommandLineInterface() {
        log.info("CommandLineInterface started");
        requestContactsFromServer();
        autoSave.start();
        String input;
        while (run) {
            System.out.print("> ");
            input = readUserInput();
            readInputCommands(input);
        }
        abm.saveContactList();
        log.info("CommandLineInterface finished");
    }

    private void readInputCommands(String userInputString) {
        log.info("User input equals: " + userInputString);
        String[] userInput = userInputString.split(" ");
        try {
            switch (userInput[0]) {
                case "add":
                    if (ich.validateInputParameters(userInput)) {
                        abm.addContact(userInput[1], userInput[2], userInput[3]);
                        break;
                    }
                    ich.throwInputParameterException(userInput);
                case "list":
                    if (ich.validateInputParameters(userInput)) {
                        abm.listContacts();
                        break;
                    }
                    ich.throwInputParameterException(userInput);
                case "search":
                    if (ich.validateInputParameters(userInput)) {
                        abm.searchContacts(userInput[1]);
                        break;
                    }
                    ich.throwInputParameterException(userInput);
                case "delete":
                    if (ich.validateInputParameters(userInput)) {
                        abm.deleteContact(userInput[1]);
                        break;
                    }
                    ich.throwInputParameterException(userInput);
                case "help":
                    if (ich.validateInputParameters(userInput)) {
                        ich.help();
                        break;
                    }
                    ich.throwInputParameterException(userInput);
                case "quit":
                    if (ich.validateInputParameters(userInput)) {
                        quitAddressBook();
                        break;
                    }
                    ich.throwInputParameterException(userInput);
                default:
                    log.info("User failed to enter a valid command: " + userInputString);
                    System.out.println("Invalid input command: " + userInputString +
                            "\nPlease try again, or type: \"help\" for a listContacts of available commands");
                    break;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "User failed to enter parameter requirement for: " + userInput[0], e);
        }
    }

    private boolean quitAddressBook() {
        log.info("Program shutdown requested by user");
        System.out.println("Shutting down application, this may take a few seconds\nwaiting for active processes to finish:");
        return run = !run;
    }
}
