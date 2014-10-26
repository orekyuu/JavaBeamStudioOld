package net.orekyuu.javatter.core;

import javafx.stage.Stage;
import net.orekyuu.javatter.api.Application;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.twitter.LocalClientUser;

public class ApplicationImpl implements Application {

    private Main main;

    public ApplicationImpl(Main main) {
        this.main = main;
    }

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

    @Override
    public void restart() {
        ClientUserRegister.getInstance().removeUsers(user -> true);
        try {
            main.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionDialogBuilder.create(e);
        }
    }


    private void loadClientUsers() {
        LocalClientUser.loadClientUsers().stream().forEach(ClientUserRegister.getInstance()::registerUser);
    }
}
