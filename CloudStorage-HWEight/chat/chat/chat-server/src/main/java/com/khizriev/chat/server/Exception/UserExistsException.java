package com.khizriev.chat.server.Exception;

public class UserExistsException extends Exception {

    public UserExistsException() {
        super("Пользователья с таким логином уже существует");
    }
}
