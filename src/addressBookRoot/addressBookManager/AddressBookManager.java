package addressBookRoot.addressBookManager;

import addressBookRoot.addressBookManager.addressBookFileHandler.AddressBookFileHandler;
import addressBookRoot.addressBookManager.contact.Contact;
import addressBookRoot.addressBookManager.contactList.ContactList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddressBookManager {
    private static final Logger log = Logger.getLogger(ContactList.class.getName());

    private ContactList contactList = new ContactList();
    private AddressBookFileHandler fileHandler = new AddressBookFileHandler();
    private List<Contact> externalContacts;

    public void loadExternalCatalogueContacts(List<Contact> externalList) {
        externalContacts = externalList;
    }

    public AddressBookManager() {
        loadContactList();
    }


    private void loadContactList() {
        if (fileHandler.loadListFromDisk() != null) {
            contactList = new ContactList(fileHandler.loadListFromDisk());
            log.info("Contact List was loaded from file");
        } else {
            contactList = new ContactList();
            log.info("new Contact List was created");
        }
    }

    public void addContact(String firstName, String lastName, String eMail) {
        contactList.addContactToList(new Contact(firstName, lastName, eMail));
        System.out.printf("New contact: %s %s, was added to your Address Book\n", firstName, lastName);
        log.info("User added new contact to listContacts");
    }

    public void listContacts() {
        if (mergedSortedList().size() > 0) {
            System.out.println("Listing all contacts in the Address Book:\n");
            mergedSortedList().forEach(this::showContact);
        }
        System.out.printf("Number of contacts available\nExternal:\t%d\nLocal:\t\t%d\n",
                externalContacts.size(), contactList.getContactList().size());
        log.info("User requested to see all contacts in listContacts.");
    }

    public void searchContacts(String query) {
        log.info("User requested to searchContacts contacts in listContacts.");
        System.out.printf("%s %s\n%s\n\n", "Searching for contact with names starting with:", query, "All matches if any will be shown below:");
        mergedSortedList().stream()
                .filter(contact -> contact.getFirstName().toLowerCase().startsWith(query) || contact.getLastName().toLowerCase().startsWith(query))
                .forEach(this::showContact);
    }

    public void deleteContact(String idStringToMatch) {
        for (int i = 0; i < contactList.getContactList().size(); i++) {
            if (contactList.getContactList().get(i).getUuid().toString().equals(idStringToMatch)) {
                System.out.format("\nMatch found: %s\n%s %s, will be deleted from your Address Book.\n\n", contactList.getContactList().get(i).getUuid().toString(),
                        contactList.getContactList().get(i).getFirstName(), contactList.getContactList().get(i).getLastName());
                contactList.deleteContactFromList(i);
                log.info("User deleted contact from listContacts");
                return;
            }
        }
        System.out.println("No contact matched the provided UUID in your local Address Book: No contact was deleted.");

        log.info("User tried to deleteContact a contact from the listContacts, no contact matched provided id string.");
    }

    public void saveContactList() {
        fileHandler.saveListToDisk(new ArrayList<>(contactList.getContactList()));
    }

    private void showContact(Contact contact) {
        System.out.format("Contact UUID: %s\n  First name: %s\n   Last name: %s\n\t  E-mail: %s\n\n",
                contact.getUuid().toString(), contact.getFirstName(), contact.getLastName(), contact.getEmail()
        );
    }

    private List<Contact> mergedSortedList() {
        List<Contact> sortedContactList = new ArrayList<>(contactList.getContactList());
        try {
            if (!externalContacts.isEmpty()) {
                sortedContactList.addAll(externalContacts);
            } else throw new Exception("External Catalogue is not available to merge, no data contacts in list.");
        } catch (Exception e) {
            log.log(Level.WARNING, "An error occurred when trying to merge lists", e);
        }
        sortedContactList.sort(Comparator.comparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER));
        return sortedContactList;
    }
}
