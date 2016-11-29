package addressBookRoot.addressBookManager.contact;

import java.io.Serializable;
import java.util.UUID;

public class Contact implements Serializable {
    private String firstName;
    private String lastName;
    private String eMail;
    private UUID uuid;


    public Contact(String firstName, String lastName, String eMail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.uuid = UUID.randomUUID();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return eMail;
    }

    public UUID getUuid() {
        return uuid;
    }
}
