import addressBookRoot.AddressBookRoot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) {
        loggerSetup();
        new AddressBookRoot();
    }

    private static void loggerSetup() {
        String loggingFilePath = "src/logging.properties";
        try (FileInputStream is = new FileInputStream(loggingFilePath)) {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            throw new RuntimeException("Could not load log properties.", e);
        }
    }
}
