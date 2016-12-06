package addressBookRoot.externalCatalogueManager;

import addressBookRoot.addressBookManager.contact.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExternalCatalogueManager {
    private static final Logger log = Logger.getLogger(ExternalCatalogueManager.class.getName());
    private ExternalCatalogueRequester ecr = new ExternalCatalogueRequester();
    private List<Contact> contactsFromExternalSource = new ArrayList<>();
    private List<String> stringDataFromExternalSource = new ArrayList<>();

    public ExternalCatalogueManager() {
    }

    public void manageDataFromExternalSource() {
        List<String> dataFromExternalSource = new ArrayList<>();
        String[] stringSplitter;
        dataFromExternalSource.addAll(ecr.requestCatalogueFromExternalSource("localhost", 61616));
        for (String data : dataFromExternalSource) {
            stringSplitter = data.split(" ");
            if (stringSplitter.length == 4) {
                this.contactsFromExternalSource.add(new Contact(stringSplitter[0], stringSplitter[1], stringSplitter[2], stringSplitter[3]));
            }
        }
        if (contactsFromExternalSource.get(0).getClass() == Contact.class)
            System.out.println("list from external source contains ContactObjects");
        System.out.println(contactsFromExternalSource.size());
    }

    public List<Contact> getContactsFromExternalCatalogue(){
        createContactsFromExternalData();
        return contactsFromExternalSource;
    }

    public void getDataFromExternalSource(String ipAddress, int portNumber) {
        this.stringDataFromExternalSource.addAll(ecr.requestCatalogueFromExternalSource(ipAddress, portNumber));
        // Containing data check
        if (!stringDataFromExternalSource.isEmpty()) {
            log.info("Data was successfully returned from external source in string form, and added to: stringDataFromExternalSource");
        } else {
            log.log(Level.SEVERE, "No data was returned from given source");
        }
    }

    public void createContactsFromExternalData() {
        String[] splitString;
        int failCount = 0;
        for (String data : stringDataFromExternalSource) {
            splitString = data.split(" ");
            if (validateContactCompatibility(splitString)) {
                this.contactsFromExternalSource.add(new Contact(splitString[0], splitString[1], splitString[2], splitString[3]));
            } else failCount++;
        }
    }

    private boolean validateContactCompatibility(String[] stringArrayToValidate) {
        for (String aStringArrayToValidate : stringArrayToValidate) {
            if (aStringArrayToValidate.isEmpty() || stringArrayToValidate.length != 4)  // Step one/two
                return false;
        }
        try {
            UUID idChecker = UUID.fromString(stringArrayToValidate[1]); // Step three
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true; // input is valid for contact creation
    }
}
