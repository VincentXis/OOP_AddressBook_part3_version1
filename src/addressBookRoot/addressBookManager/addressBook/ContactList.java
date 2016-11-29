package addressBookRoot.addressBookManager.addressBook;

import addressBookRoot.addressBookManager.contact.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactList {
    private List<Contact> contactList;

    // Load Contact List from file
    public ContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    // Create empty Contact List
    public ContactList() {
        this.contactList = new ArrayList<>();
    }

    // get list
    public List<Contact> getContactList() {
        return contactList;
    }

    // add contact
    public void addContactToList(Contact contact) {
        contactList.add(contact);
    }

    // delete contact
    public void deleteContactFromList(int indexOfContactToDelete) {
        contactList.remove(indexOfContactToDelete);
    }
}
