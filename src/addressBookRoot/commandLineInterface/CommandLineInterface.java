package addressBookRoot.commandLineInterface;

import addressBookRoot.addressBookManager.AddressBookManager;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLineInterface {
    // stuff needed to keep the boat afloat
    private static final Logger log = Logger.getLogger(CommandLineInterface.class.getName());
    private boolean run = true;
    private InputCommandHandler icHandler = new InputCommandHandler();
    private AddressBookManager abm = new AddressBookManager();

    /**
     * CommandLineInterface and the AutoSave function are started here.
     */
    public CommandLineInterface() {
        commandLineInterface();
        autoSave();
    }

    private String readUserInput() {
        return new Scanner(System.in).nextLine();
    }

    /**
     * Runs while run says run.
     */
    private void commandLineInterface() {
        log.info("CommandLineInterface started");
        String[] input;
        while (run) {
            input = readUserInput().split(" ");
            readInputCommands(input);
        }
        abm.saveContactList();
        log.info("CommandLineInterface finished");
    }

    /**
     * Method decides where the wind blows, depending on the user input.
     * @param userInput <-- User put that in
     */
    private void readInputCommands(String[] userInput) {
        try {
            switch (userInput[0]) {
                case "add":
                    if (icHandler.validInputParameters(userInput)) {
                        abm.add(userInput[1], userInput[2], userInput[3]);
                        break;
                    }
                    icHandler.throwInputException(userInput);
                case "list":
                    if (icHandler.validInputParameters(userInput)) {
                        abm.list();
                        break;
                    }
                    icHandler.throwInputException(userInput);
                case "search":
                    if (icHandler.validInputParameters(userInput)) {
                        abm.search(userInput[1]);
                        break;
                    }
                    icHandler.throwInputException(userInput);
                case "delete":
                    if (icHandler.validInputParameters(userInput)) {
                        abm.delete(userInput[1]);
                        break;
                    }
                    icHandler.throwInputException(userInput);
                case "help":
                    if (icHandler.validInputParameters(userInput)) {
                        icHandler.help();
                        break;
                    }
                    icHandler.throwInputException(userInput);
                case "quit":
                    if (icHandler.validInputParameters(userInput)) {
                        quit();
                        break;
                    }
                    icHandler.throwInputException(userInput);
                default:
                    log.info("User failed to enter a valid command: " + userInput[0]);
                    System.out.println("Invalid input command: " + userInput[0] +
                            "\nPlease try again, or type: \"help\" for a list of available commands");
                    break;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "User failed to enter parameter requirement for: " + userInput[0], e);
        }
    }

    /**
     * Previously called flipSwitch, it shuts the door, turns off all the lights
     * pops a cap in AddressBooks' ass. kills it dead. Pretty much quits the application
     * @return don't run basically
     */
    private boolean quit() {
        log.info("Program shutdown requested by user");
        System.out.println("Shutting down application, this may take a few seconds\nwaiting for active processes to finish:");
        return run = !run;
    }

    private void autoSave() {
        new Thread(() -> {
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
        }).start();
    }
}
