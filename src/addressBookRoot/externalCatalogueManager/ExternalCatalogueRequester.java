package addressBookRoot.externalCatalogueManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExternalCatalogueRequester {
    private static final Logger log = Logger.getLogger(ExternalCatalogueRequester.class.getName());

    List<String> requestCatalogueFromExternalSource(String ipAddress, int portNumber) {
        List<String> dataFromExternalSource = new ArrayList<>();
        String inputLine;
        try (Socket socket = new Socket(ipAddress, portNumber);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner in = new Scanner(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println("getall");
            while ((inputLine = in.nextLine()) != null) {
                if (inputLine.isEmpty()) {
                    break;
                } else if (inputLine.length() == 0) {
                    break;
                } else if (inputLine.split(" ").length != 4) {
                    break;
                }
                dataFromExternalSource.add(inputLine);
            }
            out.println("exit");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Connection refused, Server unavailable: ", e);
            System.out.format("Could not load external contacts from Server: %s %d\n", ipAddress, portNumber);
        }
        return dataFromExternalSource;
    }

}
