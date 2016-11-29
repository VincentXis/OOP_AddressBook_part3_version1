package addressBookRoot;

import addressBookRoot.commandLineInterface.CommandLineInterface;

import java.util.logging.Logger;

public class AddressBookRoot {
    private static final Logger log = Logger.getLogger(AddressBookRoot.class.getName());
    CommandLineInterface cli;

    public AddressBookRoot() {
        runAddressBookApplication();
    }

    private void runAddressBookApplication() {
        log.info("AddressBook application started");
        System.out.println("Welcome to the Address Book application");
        cli = new CommandLineInterface();
        log.info("AddressBook application finished");
    }
}
