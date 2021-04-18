package com.khizriev.chat.server;

import com.khizriev.chat.server.Exception.DBConnectException;
import com.khizriev.chat.server.Exception.ReadResultSetException;
import com.khizriev.chat.server.Exception.UserExistsException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    PreparedStatement ps;
    ResultSet rs;

    public boolean checkUser(String login, String pass) throws SQLException, DBConnectException {


        try{
            try{
                ps = DBConnect.getInstance().getConnection()
                        .prepareStatement("SELECT login, pass FROM `users` WHERE (`login` = ? and `pass` = ?);");
                ps.setString(1, login);
                ps.setString(2, pass);
                rs = ps.executeQuery();
            } catch (SQLException e){
                throw  new DBConnectException();
            }
            try {
                return rs.next();
            }catch (SQLException e){
                throw new ReadResultSetException();
            }
        }finally {
            ps.close();
        }
    }

    public boolean registerUser(String login, String pass) throws Exception, UserExistsException {
        try{
            try{
                ps = DBConnect.getInstance().getConnection()
                        .prepareStatement("SELECT login FROM `users` WHERE (`login` = ?);");
                ps.setString(1, login);
                rs = ps.executeQuery();
                if (rs.next()){
                    throw new UserExistsException();
                }

                ps = DBConnect.getInstance().getConnection()
                        .prepareStatement("INSERT INTO users (`login`, `pass`) VALUES (?, ?)");
                ps.setString(1,login);
                ps.setString(2, pass);
                return ps.execute();
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }finally {
            ps.close();
        }
    }


}
