package com.khizriev.chat.server.Exception;

public class DBConnectException extends Throwable {
    public DBConnectException() {
        super("Ошибка соединения с базой данных.");
    }
}
