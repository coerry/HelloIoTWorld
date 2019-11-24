package com.cr.helloiotworld.data;

import com.cr.helloiotworld.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private LoggedInUser user;

    public LoginDataSource(String username, String creditCard) {
        login(username, creditCard);
    }

    Result<LoggedInUser> login(String username, String creditCard) {
        try {
            LoggedInUser user =
                    new LoggedInUser(username, creditCard);

            this.user = user;

            return new Result.Success<>(user);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    void logout() {
        this.user = null;
    }
}
