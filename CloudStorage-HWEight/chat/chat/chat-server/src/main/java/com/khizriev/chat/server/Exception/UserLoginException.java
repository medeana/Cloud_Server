package com.khizriev.chat.server.Exception;

public class UserLoginException extends Exception{

    public UserLoginException(){
        super("Не указан логин и пароль пользователя");
    }
}
