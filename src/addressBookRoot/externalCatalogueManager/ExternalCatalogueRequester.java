package addressBookRoot.externalCatalogueManager;

import addressBookRoot.addressBookManager.contact.Contact;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SocketHandler;

public class ExternalCatalogueRequester {
    private static final Logger log = Logger.getLogger(ExternalCatalogueRequester.class.getName());
    private List<Contact> contactListFromServer = new ArrayList<>();

    // Get external list
    public List<Contact> getExternalContactList() {
        return contactListFromServer;
    }

    // Request Data from external catalogue
    public void requestDataFromExternalCatalogue() {
        String inputLine;
        String[] inputLineSplit;

        try (Socket socket = new Socket("localhost", 61616);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             InputStream is = socket.getInputStream();
             InputStreamReader isr = new InputStreamReader(is);
             Scanner read = new Scanner(isr)
//             BufferedReader read = new BufferedReader(in)
        ) {
            out.println("get all");
            while ((inputLine = read.nextLine()) != null) {
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
