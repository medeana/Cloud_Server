package com.khizriev.chat.client.Controller;

import com.khizriev.chat.client.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Network network;

    @FXML
    TextField msgField;

    @FXML
    TextArea mainArea;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network((args) -> mainArea.appendText((String)args[0]));

    }

    public void sendMsgAction(ActionEvent actionEvent) {
        network.sendMessage(msgField.getText());
        msgField.clear();
        msgField.requestFocus();
    }

    //public void sendAuthData(ActionEvent actionEvent){
       // network.sendCommand(Command.generate(Command.CommandType.AUTH, login.getText(), password.getText()));
    //}
 }


