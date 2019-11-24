package com.cr.helloiotworld.data.model;

import com.cr.helloiotworld.DatabaseHandler;

import java.io.IOException;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    DatabaseHandler db = DatabaseHandler.getInstance(null);

    private String name;
    private String creditCard;
    private Integer RFID_TN;

    public LoggedInUser(String name, String creditCard) throws IOException {
        if (!db.checkClient(name))
            throw new IOException("No such user");

        this.name = name;
        this.creditCard = creditCard;
        this.RFID_TN = db.getTN(name);
    }

    public String getName() {
        return name;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public Integer getRFID_TN() {
        return RFID_TN;
    }
}
