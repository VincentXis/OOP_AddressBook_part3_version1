package addressBookRoot.externalCatalogueManager;

import addressBookRoot.addressBookManager.contact.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExternalCatalogueManager {
    private static final Logger log = Logger.getLogger(ExternalCatalogueManager.class.getName());
    private ExternalCatalogueRequester ecr = new ExternalCatalogueRequester();
    private List<Contact> contactsFromExternalSource = new ArrayList<>();
    private List<String> stringDataFromExternalSource = new ArrayList<>();

    public ExternalCatalogueManager(){
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
        if (contactsFromExternalSource.get(0).getClass() ==  Contact.class)
            System.out.println("list from external source contains ContactObjects");
        System.out.println(contactsFromExternalSource.size());
    }
    public void getDataFromExternalSource(String ipAddress, int portNumber) {
        try {
            this.stringDataFromExternalSource.addAll(ecr.requestCatalogueFromExternalSource(ipAddress, portNumber));
            // Containing data check
            if (!stringDataFromExternalSource.isEmpty() && stringDataFromExternalSource.get(0).getClass() == String.class) {
                log.info("Data was successfully returned from external source in string form, and added to: stringDataFromExternalSource");
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Data request returned null" + e);
        }
    }
    private void containingDataCheck(){

    }
}
