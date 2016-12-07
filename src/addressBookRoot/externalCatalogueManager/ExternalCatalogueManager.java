package addressBookRoot.externalCatalogueManager;

import addressBookRoot.addressBookManager.contact.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExternalCatalogueManager extends ExternalCatalogueRequester {
    private static final Logger log = Logger.getLogger(ExternalCatalogueManager.class.getName());
    private List<Contact> contactsFromExternalSource = new ArrayList<>();
    private List<String> stringDataFromExternalSource = new ArrayList<>();

    public List<Contact> getContactsFromExternalCatalogue() {
        log.info("Loading available contacts from external catalogue to AddressBookManager");
        return this.contactsFromExternalSource;
    }

    public void getDataFromExternalSource(String ipAddress, int portNumber) {
        this.stringDataFromExternalSource.addAll(super.requestCatalogueFromExternalSource(ipAddress, portNumber));
        if (!stringDataFromExternalSource.isEmpty()) {
            log.info("Data was successfully returned from external source server IP-Address: " + ipAddress + "Port-number: " + portNumber);
        } else {
            log.log(Level.SEVERE, "No data was returned from given source" +
                    "\nserver IP-Address: " + ipAddress + "Port-number: " + portNumber);
        }
    }

    public void createContactsFromExternalCatalogueStrings() {
        String[] splitString;
        int failCount = 0;
        for (String data : stringDataFromExternalSource) {
            splitString = data.split(" ");
            if (validateContactCompatibility(splitString)) {
                this.contactsFromExternalSource.add(new Contact(splitString[0], splitString[1], splitString[2], splitString[3]));
            } else failCount++;
        }
        if (failCount > 0) {
            log.log(Level.WARNING, "Failed to create: " + failCount + " contact/s with data provided by the external catalogue/s");
        } else {
            log.info(contactsFromExternalSource.size() + " new contacts created from data provided by the external catalogue/s");
        }
    }

    private boolean validateContactCompatibility(String[] stringArrayToValidate) {
        for (String stringToValidate : stringArrayToValidate) {
            if (stringToValidate.isEmpty() || stringArrayToValidate.length != 4) { // Step one/two
                log.log(Level.WARNING, "incompatible string for contact creation, failed validation. String length: "
                        + stringToValidate.length() + " Array length: " + stringArrayToValidate.length);
                return false;
            }
        }
        try {
            UUID idChecker = UUID.fromString(stringArrayToValidate[0]); // Step three
        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, "Contact failed validation, error occurred while preforming the UUID.fromString check, string: "
                    + stringArrayToValidate[0] + " is not compatible for contact creation", e);
            return false;
        }
        return true; // input is valid for contact creation
    }
}
