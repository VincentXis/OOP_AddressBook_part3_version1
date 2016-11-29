package addressBookRoot.addressBookManager;

import addressBookRoot.addressBookManager.addressBook.ContactList;
import addressBookRoot.addressBookManager.addressBookFileHandler.AddressBookFileHandler;
import addressBookRoot.addressBookManager.contact.Contact;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class AddressBookManager {
    private static final Logger log = Logger.getLogger(ContactList.class.getName());

    private ContactList contactList = new ContactList();
    private AddressBookFileHandler fileHandler = new AddressBookFileHandler();

    /**
     * Constructor: calls loadContactList()
     * I have a question here, is it better to leave code outside of a constructor?
     * or should i have written all the code from the "loadContactList" method here?
     */
    public AddressBookManager(){
        loadContactList();
    }

    /**
     * Method requests a saved Contact List from file:
     * if return is: null, a new ArrayList is created through ContactLists constructor.
     * else the saved Contact List is Loaded
     */
    private void loadContactList() {
        if (fileHandler.loadListFromDisk() != null) {
            contactList = new ContactList(fileHandler.loadListFromDisk());
            log.info("Contact List was loaded from file");
        } else {
            contactList = new ContactList();
            log.info("new Contact List was created");
        }
    }

    /**
     * Command: Add Contact to list
     */
    public void add(String firstName, String lastName, String eMail) {
        contactList.addContactToList(new Contact(firstName, lastName, eMail));
        System.out.printf("New contact: %s %s, was added to your Address Book\n", firstName, lastName);

        log.info("User added new contact to list");
    }

    /**
     * Command: List all contacts
     */
    public void list() {
        int listSize = sortByFirstName().size();
        if (listSize > 0) {
            System.out.println("Listing all contacts in Address Book:\n");
            sortByFirstName().forEach(this::showContact);
        }
        System.out.printf("%s %d %s\n", "There are currently:", listSize, "saved contact/s in your Address Book\n");

        log.info("User requested to see all contacts in list.");
    }

    /**
     * Command: Search Contact/s in list
     */
    public void search(String query) {
        log.info("User requested to search contacts in list.");
        System.out.printf("%s %s\n\n", "Searching for contact with names starting with:", query);
        sortByFirstName().stream().filter(contact -> contact.getFirstName().toLowerCase().startsWith(query) || contact.getLastName().toLowerCase().startsWith(query)
        ).forEach(this::showContact);
    }

    /**
     * Command: Delete Contact from list
     * Cycles through contact list and finds the index of the contact matching the input
     * conditional Feedback
     * match: found match + info, logs contact deleted
     * no match: found no match, logs user tried to delete but found no match
     */
    public void delete(String idStringToMatch) {
        for (int i = 0; i < contactList.getContactList().size(); i++) {
            if (contactList.getContactList().get(i).getUuid().toString().equals(idStringToMatch)) {
                System.out.format("\nMatch found: %s\n%s %s, will be deleted from your Address Book.\n\n",
                        contactList.getContactList().get(i).getUuid().toString(), contactList.getContactList().get(i).getFirstName(), contactList.getContactList().get(i).getLastName()
                );
                contactList.deleteContactFromList(i);
                log.info("User deleted contact from list");
                return;
            }
        }
        System.out.println("No contact matched the provided UUID in your Address Book: No contact was deleted.");

        log.info("User tried to delete a contact from the list, no contact matched provided id string.");
    }

    /**
     * Saves ContactList to file
     */
    public void saveContactList() {
        fileHandler.saveListToDisk(new ArrayList<>(contactList.getContactList()));
    }

    /**
     * Shows formatted Contact information, used in list and search
     */
    private void showContact(Contact contact) {
        System.out.format("Contact UUID: %s\n  First name: %s\n   Last name: %s\n\t  E-mail: %s\n\n",
                contact.getUuid().toString(), contact.getFirstName(), contact.getLastName(), contact.getEmail()
        );
    }
    // Sort copy of ContactList
    private List<Contact> sortByFirstName() {
        List<Contact> sortedContactList = new ArrayList<>(contactList.getContactList());
        sortedContactList.sort(Comparator.comparing(contact -> contact.getFirstName(), String.CASE_INSENSITIVE_ORDER));
        return sortedContactList;
    }
}
