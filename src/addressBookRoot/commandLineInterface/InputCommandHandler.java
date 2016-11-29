package addressBookRoot.commandLineInterface;

import java.security.InvalidParameterException;
import java.util.logging.Logger;

public class InputCommandHandler {
    private static final Logger log = Logger.getLogger(InputCommandHandler.class.getName());

    /**
     * Command: Help
     */
    public void help() {
        System.out.format("%s\n%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n", "The input for a command has to be lowercase to register",
                "List of all available commands:",
                "add:    add a new contact to list",
                "list:   show all contacts in list",
                "delete: remove a contact from list",
                "search: find contact/s in list ",
                "help:   to get here, lists all available commands",
                "quit:   exit the application"
        );
    }

    /* ********************************************************************************* *\
     * These two methods help keep the CLI.readInputCommands() clean and easy to read.   *
     * the switch tree got messy very quickly before i decided to move most of the logic *
     * out of it.                                                                        *
    \* ********************************************************************************* */
    /**
     * Method throws a bunch of exceptions depending, one for each case in CLI.readInputCommands()
     * @param invalidParameter - Array element[0] decides which exception to throw
     *                         the rest are to show why the parameters doesn't match
     */
    public void throwInputException(String[] invalidParameter) {
        String errorMessage;
        switch (invalidParameter[0]) {
            case "add":
                errorMessage = "add requires 4 parameters, received: " + invalidParameter.length;
                System.out.println(errorMessage);
                throw new InvalidParameterException(errorMessage);
            case "list":
                errorMessage = "list requires 1 parameters, received: " + invalidParameter.length;
                System.out.println(errorMessage);
                throw new InvalidParameterException(errorMessage);
            case "search":
                errorMessage = "search requires 2 parameters, received: " + invalidParameter.length;
                System.out.println(errorMessage);
                throw new InvalidParameterException(errorMessage);
            case "delete":
                errorMessage = "delete requires 2 parameters, received: " + invalidParameter.length;
                System.out.println(errorMessage);
                throw new InvalidParameterException(errorMessage);
            case "help":
                errorMessage = "help requires 1 parameters, received: " + invalidParameter.length;
                System.out.println(errorMessage);
                throw new InvalidParameterException(errorMessage);
            case "quit":
                errorMessage = "add requires 4 parameters, received: " + invalidParameter.length;
                System.out.println(errorMessage);
                throw new InvalidParameterException(errorMessage);
        }
    }

    /**
     * This method checks if the input parameters are correct for each case
     * in CLI.readInputCommands()'s switch cases.
     * @param input - string to validate input parameters
     * @return true/false depending
     */
    public boolean validInputParameters(String[] input) {
        boolean inputIsPassedValidation;
        if (input[0].equals("add") && input.length != 4) {
            inputIsPassedValidation = false;
        } else if ((input[0].equals("search") || input[0].equals("delete")) && input.length != 2) {
            inputIsPassedValidation = false;
        } else if ((input[0].equals("list") || input[0].equals("help") || input[0].equals("quit")) && input.length != 1) {
            inputIsPassedValidation = false;
        } else {
            inputIsPassedValidation = true;
        }
        return inputIsPassedValidation;
    }


}
