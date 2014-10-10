package net.orekyuu.javatter.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.Application;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.core.twitter.LocalClientUser;

import java.io.IOException;

public class ApplicationImpl implements Application {

    @Override
    public void onStart(String[] args) {

    }

    @Override
    public void onLoad() {
        loadClientUsers();
    }


    private void loadClientUsers() {
        LocalClientUser.loadClientUsers().stream().forEach(ClientUserRegister.getInstance()::registerUser);
    }

    @Override
    public void onCreate(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        try {
            Scene scene = new Scene(loader.load(getClass().getResourceAsStream("root.fxml")));
            MainWindowPresenter presenter = loader.getController();
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Javaビーム工房");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

}
