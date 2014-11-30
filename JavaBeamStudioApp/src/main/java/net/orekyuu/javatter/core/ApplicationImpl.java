package net.orekyuu.javatter.core;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.Stylesheet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.Application;
import net.orekyuu.javatter.api.GlobalAccess;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.twitter.LocalClientUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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
        StyleManager.getInstance().addUserAgentStylesheet(Main.class.getResource("javabeamstudio.css").toExternalForm());

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
        GlobalAccess.getInstance().getColumnRegister().registerColumn("タイムライン", Main.class, "userstream.fxml");
        GlobalAccess.getInstance().getColumnRegister().registerColumn("Mentions", Main.class, "mentions.fxml");
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
