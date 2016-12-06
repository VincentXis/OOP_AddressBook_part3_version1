package addressBookRoot.commandLineInterface;

import addressBookRoot.addressBookManager.AddressBookManager;
import addressBookRoot.externalCatalogueManager.ExternalCatalogueManager;
import addressBookRoot.externalCatalogueManager.ExternalCatalogueRequester;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLineInterface {
    // stuff needed to keep the boat afloat
    private static final Logger log = Logger.getLogger(CommandLineInterface.class.getName());
    private boolean run = true;
    private InputCommandHandler ich = new InputCommandHandler();
    private AddressBookManager abm = new AddressBookManager();
    private ExternalCatalogueRequester ecr = new ExternalCatalogueRequester();
    private ExternalCatalogueManager ecm = new ExternalCatalogueManager();

    private Thread requestContactsFromServer = new Thread(() -> {
        ecm.manageDataFromExternalSource();
//        ecr.requestDataFromExternalCatalogue();
//        abm.loadExternalContacts(ecr.getExternalContactList());
//        System.out.println(ecr.getExternalContactList().size() + " contacts have been loaded from an external catalogue");
    });

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

    /**
     * CommandLineInterface starts.
     */
    public CommandLineInterface() {
        runCommandLineInterface();
    }

    private String readUserInput() {
        return new Scanner(System.in).nextLine();
    }

    /**
     * Takes input from user and sends to readInputCommands
     * Runs while run says run.
     */
    private void runCommandLineInterface() {
        log.info("CommandLineInterface started");
        requestContactsFromServer.start();
        autoSave.start();
        String[] input;
        while (run) {
            System.out.print("> ");
            input = readUserInput().split(" ");
            readInputCommands(input);
        }
        abm.saveContactList();
        log.info("CommandLineInterface finished");
    }

    /**
     * userInput decides what actions to take. however, even if the input command is entered correctly
     * the input parameters must be correct as well.
     *
     * @param userInput <-- User put that in
     */
    private void readInputCommands(String[] userInput) {
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
                    log.info("User failed to enter a valid command: " + userInput[0]);
                    System.out.println("Invalid input command: " + userInput[0] +
                            "\nPlease try again, or type: \"help\" for a listContacts of available commands");
                    break;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "User failed to enter parameter requirement for: " + userInput[0], e);
        }
    }

    /**
     * Previously called flipSwitch, it shuts the door, turns off all the lights
     * pops a cap in AddressBooks' ass. kills it dead. Pretty much quits the application
     *
     * @return don't run basically
     */
    private boolean quitAddressBook() {
        log.info("Program shutdown requested by user");
        System.out.println("Shutting down application, this may take a few seconds\nwaiting for active processes to finish:");
        return run = !run;
    }
}
