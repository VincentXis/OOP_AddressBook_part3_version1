package addressBookRoot.addressBookManager.contact;

import java.io.Serializable;
import java.util.UUID;

public class Contact implements Serializable {
    private String firstName;
    private String lastName;
    private String eMail;
    private UUID uuid;

    // New Contact, constructor local
    public Contact(String firstName, String lastName, String eMail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.uuid = UUID.randomUUID();
    }

    // New Contact, external
    public Contact(String uuid, String firstName, String lastName, String eMail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.uuid = UUID.fromString(uuid);
    }

    // Get First name
    public String getFirstName() {
        return firstName;
    }

    // Get Last name
    public String getLastName() {
        return lastName;
    }

    // Get Email
    public String getEmail() {
        return eMail;
    }

    // get UUID
    public UUID getUuid() {
        return uuid;
    }
}