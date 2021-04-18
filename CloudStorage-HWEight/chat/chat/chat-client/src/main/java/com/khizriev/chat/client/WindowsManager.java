package com.khizriev.chat.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class WindowManager {
    private static Stage stage = ClientSettings.getInstance().getStage();

    public static void showLogin() {
        try {
            Parent root = FXMLLoader.load(WindowManager.class.getResource("/client_login.fxml"));
            stage.setTitle("MyCloud - Authorization");
            Scene scene = new Scene(root, 400, 150);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }