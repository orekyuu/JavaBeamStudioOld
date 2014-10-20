package net.orekyuu.javatter.core;

import javafx.stage.Stage;
import net.orekyuu.javatter.api.Application;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.core.twitter.LocalClientUser;

public class ApplicationImpl implements Application {

    @Override
    public void onStart(String[] args) {

    }

    @Override
    public void onLoad() {
        loadClientUsers();
    }

    @Override
    public void onCreate(Stage primaryStage) {

    }


    private void loadClientUsers() {
        LocalClientUser.loadClientUsers().stream().forEach(ClientUserRegister.getInstance()::registerUser);
    }
}
