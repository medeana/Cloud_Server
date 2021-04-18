package com.khizriev.chat.server.Exception;

public class UserPassException extends Exception{
    public UserPassException() {
        super("Не указан пароль");
    }
}
