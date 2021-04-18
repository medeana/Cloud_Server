package com.khizriev.chat.server;

import com.khizriev.chat.server.Exception.UserLoginException;
import com.khizriev.chat.server.Exception.UserPassException;

public class AuthInfo {
    private String login;
    private String pass;

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getName() {
        return login;
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
