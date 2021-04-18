package ru.khizirev.storage.server.helper;

import ru.khizirev.storage.server.exception.UserLoginException;
import ru.khizirev.storage.server.exception.UserPassException;

public class AuthInfo {

    private String login;
    private String pass;

    public String getName() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public AuthInfo(String [] args) throws UserLoginException, UserPassException {

        if (args.length == 0) {
            throw new UserLoginException();
        }
        if (args.length == 1) {
            throw new UserPassException();
        }
        login = args[0];
        pass = args[1];
    }
}
