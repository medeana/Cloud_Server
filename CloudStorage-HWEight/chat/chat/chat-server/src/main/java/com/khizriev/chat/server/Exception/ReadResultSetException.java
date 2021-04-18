package com.khizriev.chat.server.Exception;


import java.sql.SQLException;

public class ReadResultSetException extends SQLException {

    public ReadResultSetException() {
        super("Ошибка чтения данных");
    }
}