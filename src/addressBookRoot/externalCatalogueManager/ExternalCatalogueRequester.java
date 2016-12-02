package addressBookRoot.externalCatalogueManager;

import addressBookRoot.addressBookManager.contact.Contact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExternalCatalogueRequester {
    private static final Logger log = Logger.getLogger(ExternalCatalogueRequester.class.getName());
    private List<Contact> contactListFromServer;

    // Get external list
    public List<Contact> getExternalContactList() {
        return contactListFromServer;
    }

    // Request Data from external catalogue
    public void requestDataFromExternalCatalogue() {
        System.out.println("Client start");
        String inputLine;
        String[] inputLineSplit;
        this.contactListFromServer = new ArrayList<>();
        try (Socket socket = new Socket("localhost", 61616);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println("get all");
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.isEmpty()) {
                    break;
                }
                inputLineSplit = inputLine.split(" ");
                this.contactListFromServer.add(new Contact(inputLineSplit[0], inputLineSplit[1], inputLineSplit[2], inputLineSplit[3]));
            }
            out.println("exit");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Connection refused, Server unavailable: ", e);
            System.out.println("Could not load external contacts, Server unavailable");
        }
        System.out.println(contactListFromServer.size());
    }
}
